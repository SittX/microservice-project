package com.kellot.microservices.gateway.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class GatewayConfig {
    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Or restrict to specific origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order-service", r -> r.path("/orders/**")
                        .filters(f -> f.prefixPath("/api")
                                .addResponseHeader("X-Powered-By", "KST Cloud Gateway for order service"))
                        .uri("lb://order-service"))
                .route("inventory-service", r -> r.path("/inventory/**")
                        .filters(f -> f.prefixPath("/api")
                                .addResponseHeader("X-Powered-By", "KST Cloud Gateway for inventory service"))
                        .uri("lb://inventory-service"))
                .route("product-service", r -> r.path("/products/**")
                        .filters(f -> f.prefixPath("/api")
                                .addResponseHeader("X-Powered-By", "KST Cloud Gateway for product service"))
                        .uri("lb://product-service"))
                .build();
    }

//    @Bean
//    @Primary
//    public WebClient webClient() {
//        HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
//        return WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
//    }
}
