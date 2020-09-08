package com.winteree.uaa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

/**
 * <p>Title: SwaggerConfig</p>
 * <p>Description: Swagger配置</p>
 *
 * @author RenFei
 * @date : 2020-04-21 16:08
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;
    public static final String VERSION = "1.0.0";
    private final WintereeUaaConfig wintereeUaaConfig;

    public SwaggerConfig(WintereeUaaConfig wintereeUaaConfig) {
        this.wintereeUaaConfig = wintereeUaaConfig;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(wintereeUaaConfig.getEnableSwagger())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.winteree.uaa.controller"))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema()));
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("Browser")
                .clientSecret("w4nHQGH8oBsj4HMrgcYVyZRFPFbMUuLK")
                .realm("signed")
                .appName("Browser")
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }

    private OAuth securitySchema() {

        List<AuthorizationScope> authorizationScopeList = newArrayList();
        authorizationScopeList.add(new AuthorizationScope("WinterEE-UAA-Serve", "身份认证服务"));
        List<GrantType> grantTypes = newArrayList();
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(accessTokenUri);
        grantTypes.add(passwordCredentialsGrant);

        return new OAuth("oauth2", authorizationScopeList, grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {

        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("WinterEE-UAA-Serve", "WinterEE-UAA-Serve");

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("WinterEE-UAA-Serve API")
                // 设置文档的描述
                .description("WinterEE-UAA-Serve RESTful API 文档 Powered By RenFei.Net")
                // 设置文档的版本信息-> 1.0.0 Version information
                .version(VERSION)
                .termsOfServiceUrl("https://www.renfei.net")
                .build();
    }
}
