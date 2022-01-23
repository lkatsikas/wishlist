package com.lkats.wishlist.books;

import com.lkats.wishlist.books.model.User;
import com.lkats.wishlist.books.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class DataInitializer {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        initUsers();
    }

    private void initUsers() {
        log.info("start users initialization  ...");
        this.users
                .deleteAll()
                .thenMany(
                        Flux
                                .just("user", "admin")
                                .flatMap(
                                        username -> {
                                            Set<String> roles = "user".equals(username)
                                                    ? new HashSet(Arrays.asList("ROLE_USER"))
                                                    : new HashSet(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));

                                            User user = User.builder()
                                                    .roles(roles)
                                                    .username(username)
                                                    .password(passwordEncoder.encode("password"))
                                                    .email(username + "@example.com")
                                                    .build();
                                            return this.users.save(user);
                                        }
                                )
                )
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done users initialization...")
                );
    }
}
