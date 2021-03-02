package com.devtech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel().anyRequest().requiresSecure()
                .and()
                .formLogin().loginPage("/api/users/auth")
                .and()
                .authorizeRequests().antMatchers("/api/users/auth", "/api/users/create:", "*/css", "*/js").permitAll()
                .and()
                .authorizeRequests().antMatchers("*/api/*").authenticated();
    }
}
