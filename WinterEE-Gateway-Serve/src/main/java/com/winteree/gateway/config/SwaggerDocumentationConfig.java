package com.winteree.gateway.config;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: SwaggerDocumentationConfig</p>
 * <p>Description: Swagger网关配置</p>
 *
 * @author RenFei
 * @date : 2020-04-21 16:05
 */
@Primary
@Component
public class SwaggerDocumentationConfig implements SwaggerResourcesProvider {
    private final RouteLocator routeLocator;

    public SwaggerDocumentationConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        routes.forEach(route -> {
            //从各个服务里获取文档
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "1.0"));
        });
        return resources;
    }
}
