package com.lkats.wishlist.books.security.model;

import com.lkats.wishlist.books.model.User;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authenticated user details model.
 */
public class GrantedUser implements UserDetails {

    @Setter
    private String username;
    @Setter
    private String password;

    @Setter
    private Set<String> authRoleTypes;

    public GrantedUser(final User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authRoleTypes = user.getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authRoleTypes
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
