package com.example.blog.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Value("${auth.app.url}")
    private String apiUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseJwt(request);
            if (token != null) {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<?> responseEntity = restTemplate.getForEntity(apiUrl + "verify-token/" + token, UserAuth.class);
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    UserAuth auth = (UserAuth) responseEntity.getBody();
                    if (auth != null){
                        List<GrantedAuthority> authorities = null;
                        if (auth.getRoles() != null){
                            authorities = auth.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                        }
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(auth, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Execption when try to authentify with message: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}
