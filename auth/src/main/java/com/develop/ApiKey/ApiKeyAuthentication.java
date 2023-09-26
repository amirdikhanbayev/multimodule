package com.develop.ApiKey;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthentication(Collection<? extends GrantedAuthority> authorities, String apiKey) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
