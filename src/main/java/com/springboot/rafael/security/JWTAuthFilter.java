package com.springboot.rafael.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.springboot.rafael.service.implementation.UserServiceImplementation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTService jwtService;

    private UserServiceImplementation userService;

    public JWTAuthFilter(JWTService jwtService, UserServiceImplementation serviceImplementation) {
        this.jwtService = jwtService;
        this.userService = serviceImplementation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1];
            boolean isValid = jwtService.isTokenValid(token);

            if (isValid) {
                String username = jwtService.getUsername(token);

                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }

        filterChain.doFilter(request, response);
        //dps dessa função ele vai para 
        //config.SecurityConfig.configure(HttpSecurity http)
    }

}
