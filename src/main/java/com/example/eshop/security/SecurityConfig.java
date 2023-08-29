package com.example.eshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((requests) -> requests.requestMatchers("/register", "/login","/index","/")
                        .permitAll().requestMatchers("/order","filter/category","/filter/category/*","/shopping-cart","/my-orders","/add/to/shopping/cart","/remove/from/shopping/cart/*","/payment","/order/delete","/order/delete/*")
                        .hasAnyAuthority("ADMIN","USER")
                        .requestMatchers("/category","/*/category/**","/product","/*/product/**")
                        .hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .formLogin((form) -> form.loginPage("/login").permitAll().successHandler(customAuthenticationSuccessHandler))
                .logout((logout) -> logout.permitAll().logoutSuccessUrl("/index"))
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder);
        dao.setUserDetailsService(userDetailsService);
        return dao;
    }


}

