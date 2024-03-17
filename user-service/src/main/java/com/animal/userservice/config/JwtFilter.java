package com.animal.userservice.config;

import com.animal.common.constant.Constants;
import com.animal.userservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        jwtService.resolveToken(request).ifPresentOrElse(claims -> {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    Set<SimpleGrantedAuthority> authorities = ((List<LinkedHashMap<String, String>>) claims.get(Constants.SPRING_SECURITY_AUTHORITIES)).stream().map(a -> a.get("authority"))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            claims.get(Constants.SPRING_SECURITY_SUBJECT).toString(),
                            null,
                            authorities
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            },
            () -> log.error("Jwt token authentication failed")
        );

        filterChain.doFilter(request, response);
    }
}
