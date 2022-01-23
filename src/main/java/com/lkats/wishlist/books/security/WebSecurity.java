package com.lkats.wishlist.books.security;

import com.lkats.wishlist.books.repository.UserRepository;
import com.lkats.wishlist.books.security.config.Constants;
import com.lkats.wishlist.books.security.handler.AuthenticationFailureHandler;
import com.lkats.wishlist.books.security.handler.AuthenticationSuccessHandler;
import com.lkats.wishlist.books.security.handler.LogoutHandler;
import com.lkats.wishlist.books.security.model.GrantedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@EnableWebFluxSecurity
public class WebSecurity {

    private final LogoutHandler logoutHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    public WebSecurity(final LogoutHandler logoutHandler,
                       final AuthenticationSuccessHandler authenticationSuccessHandler,
                       final AuthenticationFailureHandler authenticationFailureHandler) {
        this.logoutHandler = logoutHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf()
                    .disable()
                .authorizeExchange()
                    .anyExchange()
                    .authenticated()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new ApiAuthenticationEntryPoint(Constants.X_AUTH_TOKEN))
                    .accessDeniedHandler((exchange, exception) -> Mono.error(exception))
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .authenticationSuccessHandler(authenticationSuccessHandler)
                    .authenticationFailureHandler(authenticationFailureHandler)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(logoutHandler)
                .and()
                    .requestCache()
                    .requestCache(NoOpServerRequestCache.getInstance())
                .and()
                .build();

    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository userRepository) {
        return (username) -> userRepository
                .findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(format("No user found. [%s] ", username))))
                .map(GrantedUser::new);
    }

}
