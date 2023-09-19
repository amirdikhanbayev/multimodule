package com.develop.Handler;


import com.google.common.base.Strings;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(Strings.isNullOrEmpty(authorizationHeader)){
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = authorizationHeader.replace("Bearer", "");
        }
    }
}
