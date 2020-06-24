package domain.yourmodule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>Title: SwaggerConfig</p>
 * <p>Description: Swagger配置</p>
 *
 * @author RenFei
 * @date : 2020-06-24 21:08
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final String VERSION = "1.0.0";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("domain.yourmodule.controller"))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("Your-Module-Serve API")
                // 设置文档的描述
                .description("Your-Module-Serve RESTful API 文档 Powered By RenFei.Net")
                // 设置文档的版本信息-> 1.0.0 Version information
                .version(VERSION)
                .termsOfServiceUrl("https://www.renfei.net")
                .build();
    }
}
