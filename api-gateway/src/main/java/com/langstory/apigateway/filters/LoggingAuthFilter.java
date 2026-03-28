package com.langstory.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingAuthFilter extends AbstractGatewayFilterFactory<LoggingAuthFilter.Config> {

    public LoggingAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Auth filter Pre: {}", exchange.getRequest().getURI());
            return  chain.filter(exchange);
        };
    }

    public static class Config {

    }
}
