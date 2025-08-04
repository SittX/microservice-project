package com.kellot.microservices.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Component
public class JwtLogFilter implements WebFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        List<String> authorizationHeaders  = exchange.getRequest().getHeaders().get("Authorization");
        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            String authorizationHeader = authorizationHeaders.get(0);
            LOGGER.info("Authorization Header: {}", authorizationHeader);
        } else {
            LOGGER.warn("No Authorization Header found in the request");
        }

        return chain.filter(exchange);
    }
}
