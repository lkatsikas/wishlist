package com.lkats.wishlist.books.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Entry point for api requests.
 */
class ApiAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final String headerValue;
    ApiAuthenticationEntryPoint(final String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        serverWebExchange.getResponse().getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, this.headerValue);
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.empty();
    }
}
