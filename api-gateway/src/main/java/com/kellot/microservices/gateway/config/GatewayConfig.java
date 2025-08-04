package com.kellot.microservices.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service", r -> r.path("/orders/**")
                        .filters(f -> f.prefixPath("/api")
                                .addResponseHeader("X-Powered-By", "KST Cloud Gateway for order service"))
                        .uri("http://order_service:8080"))
                .route("inventory-service", r -> r.path("/inventory/**")
                        .filters(f -> f.prefixPath("/api")
                                .addResponseHeader("X-Powered-By", "KST Cloud Gateway for inventory service"))
                        .uri("http://inventory_service:8080"))
                .route("product-service", r -> r.path("/products/**")
                        .filters(f -> f.prefixPath("/api")
                                .addResponseHeader("X-Powered-By", "KST Cloud Gateway for product service"))
                        .uri("http://product_service:8080"))
                .build();
    }
}
