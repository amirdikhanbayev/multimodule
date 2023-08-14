package com.develop.handler;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@NoArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            Authentication authentication = AuthenticationService.getAuthentication((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exception){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpServletResponse.getWriter();
            writer.print(exception.getMessage());
            writer.flush();
            writer.close();
        }
        chain.doFilter(request, response);
    }
}
