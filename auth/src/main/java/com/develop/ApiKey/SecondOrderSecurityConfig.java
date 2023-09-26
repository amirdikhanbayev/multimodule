package com.develop.ApiKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


@Configuration
@EnableWebSecurity
public class SecondOrderSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new ApiKeyAuthenticationMatcher())
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .and()
                .csrf().disable()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    static class ApiKeyAuthenticationMatcher implements RequestMatcher{
        @Override
        public boolean matches(HttpServletRequest request) {
            String apiKey = request.getHeader("X-API-KEY");
            return apiKey != null;
        }
    }



}
