package domain.yourmodule.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * ResourceServerConfig
 *
 * @author RenFei
 */
@Configuration
@EnableResourceServer
@EnableOAuth2Client
@EnableConfigurationProperties
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "WinterEE";
    private final TokenStore tokenStore;
    private final OAuth2ClientContext oAuth2ClientContext;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    ResourceServerConfiguration(TokenStore tokenStore, OAuth2ClientContext oAuth2ClientContext, PasswordEncoder passwordEncoder) {
        this.tokenStore = tokenStore;
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //重点，设置资源id
        resources.resourceId(RESOURCE_ID)
                .tokenStore(tokenStore)
                .stateless(true);
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

//    @Bean
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, clientCredentialsResourceDetails());
//    }

    @Bean
    public OAuth2RestOperations restTemplate(OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails(), oauth2ClientContext);
    }

    @Bean
    public CustomAccessDeniedHandlerImpl customAccessDeniedHandler() {
        return new CustomAccessDeniedHandlerImpl();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        // 配置一个拦截器，对于每一个外来的请求，都会在request域内创建一个AccessTokenRequest类型的bean。
        return new OAuth2FeignRequestInterceptor(
                new DefaultOAuth2ClientContext(),
                clientCredentialsResourceDetails());
    }

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        // 用于向认证服务器服务请求token
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable()
                .sessionManagement().disable()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
    }
}
