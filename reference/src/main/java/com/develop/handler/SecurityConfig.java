package com.develop.handler;

import com.develop.handler.Jwt.CustomAuthorizationFilter;
import com.develop.handler.apiKey.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .requestMatcher(new ApiKeyMatcher(true))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api/dictionary/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AuthenticationFilter());
        return http.build();
    }

    @Bean
    public SecurityFilterChain JwtFilterChain(HttpSecurity http) throws Exception{
        http
                .requestMatcher(new ApiKeyMatcher(false))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/api/dictionary/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



    static class ApiKeyMatcher implements RequestMatcher {
        private final boolean hasApiKey;

        public ApiKeyMatcher(boolean hasApiKey) {
            this.hasApiKey = hasApiKey;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            String apiKey = request.getHeader("X-API-KEY");
            return (hasApiKey && apiKey != null) || (!hasApiKey && apiKey == null);
        }
    }
}
