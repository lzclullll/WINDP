package com.energy.platform.security;


import com.energy.platform.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class LoginUser implements UserDetails {
    private final User user;
    private final List<String> roles;
    private final List<String> permissions;
    public LoginUser(User user, List<String> roles, List<String> permissions) {
        this.user = user; this.roles = roles; this.permissions = permissions;
    }
    public User getUser() { return user; }
    public List<String> getRoles() { return roles; }
    public List<String> getPermissions() { return permissions; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).forEach(authorities::add);
        permissions.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);
        return authorities;
    }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return "ENABLED".equals(user.getStatus()); }
}
