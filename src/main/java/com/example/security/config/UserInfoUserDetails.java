package com.example.security.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.security.entity.UserInfo;

public class UserInfoUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails() {
    }

    public UserInfoUserDetails(String name, String password, List<GrantedAuthority> authorities) {
        this.name = name;
        this.password = password;
        this.authorities = authorities;
    }

    public UserInfoUserDetails(UserInfo userInfo) {
        this.name = userInfo.getName();
        this.password = userInfo.getPassword();
        if (userInfo.getRoles() != null && !userInfo.getRoles().isBlank()) {
            this.authorities = Arrays.stream(userInfo.getRoles().split(","))
                    .map(String::trim)
                    .filter(r -> !r.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            this.authorities = List.of();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
