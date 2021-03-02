package com.devtech.jwt;

import com.devtech.entity.User;
import com.devtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends GenericFilterBean {
    private final JWTProvider provider;
    private final UserRepository userRepo;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String authHeader = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (provider.validateToken(token)) {
                    User user = userRepo.findByLogin(provider.getLogin(token)).orElse(null);
                    if (user != null) {
                        UsernamePasswordAuthenticationToken authToken
                                = new UsernamePasswordAuthenticationToken(user, null);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
