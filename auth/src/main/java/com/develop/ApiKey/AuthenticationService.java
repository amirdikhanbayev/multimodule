package com.develop.ApiKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationService {

    @Value("${security.api-key}")
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    @Value("${security.api-key-value}")
    public static final String AUTH_TOKEN = "Shrek";

    public static Authentication getAuthentication(HttpServletRequest request){
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if(apiKey == null || !apiKey.equals(AUTH_TOKEN)){
            throw new BadCredentialsException("Invalid API Key");
        }
        return new ApiKeyAuthentication(AuthorityUtils.NO_AUTHORITIES, apiKey);
    }
}
