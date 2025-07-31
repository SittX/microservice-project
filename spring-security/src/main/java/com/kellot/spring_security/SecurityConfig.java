package com.kellot.spring_security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomUserDetailsService customUserDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);
        AuthenticationManager publicEndpointAuthManager = authenticationManagerBuilder.build();

        return httpSecurity.authorizeHttpRequests((request) -> {
                    request.requestMatchers("/private").authenticated();
                    request.requestMatchers("/public/**").permitAll();
                    request.anyRequest().authenticated();
                })
                .securityMatcher("/**")
                .formLogin(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .authenticationManager( publicEndpointAuthManager)
                .addFilterBefore(new CustomFilter(), AuthorizationFilter.class)
                .build();
    }

    @Bean
    public SecurityFilterChain anotherSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager internalEndpointAuthManager = authenticationManagerBuilder.build();

        return httpSecurity.authorizeHttpRequests((request) -> {
                   request.requestMatchers("/api/user").authenticated();
                })
                .securityMatcher("/api/**")
                .authenticationManager(internalEndpointAuthManager)
                .formLogin(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
