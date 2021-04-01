package com.devtech.config;

import com.devtech.filter.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTFilter filter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.requiresChannel().anyRequest().requiresSecure()
                //.and()
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/users/auth", "/api/users/create", "/api/utils/*").permitAll()
                .regexMatchers(".*/create", ".*/update/.*", ".*/delete/.*", ".*/rate/.*",
                        ".*/cartwish/.*").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
