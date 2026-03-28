package com.langstory.apigateway.filters;

import com.langstory.apigateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            // Passing data from application.yml to here by Config class
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }

            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            // Check if authorizationHeader is null or not
            // If null, it will return from here with UNAUTHORIZED Status Code
            if (authorizationHeader == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeader.split("Bearer ")[1];

            Long userId = jwtService.getUserIdFromToken(token);

            // Mutating the request to pass userIf to auth-service
            exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId.toString())
                    .build();

            return chain.filter(exchange);
        });
    }

    @Data
    public static class Config {
        private boolean enabled;
    }
}
