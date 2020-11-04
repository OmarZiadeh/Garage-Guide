package com.TeamOne411.security;

import com.TeamOne411.backend.service.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // url literals for redirections
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        // disable csrf, vaadin has proprietary implementation
        http.csrf().disable()
                // CustomRequestCache saves unauthorized login attempts for redirection to login
                .requestCache().requestCache(new CustomRequestCache())

                // restricts access to application
                .and().authorizeRequests()
                    // allows internal requests
                    .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                    // allow unauthorized access to front page and register
                    .antMatchers("/", "/register").permitAll()
                    // allow all authentication users access to any request
                    // todo add rules
                    .anyRequest().authenticated()

                // configure login page
                .and().formLogin()
                    .loginPage(LOGIN_URL).permitAll()
                    .loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .failureUrl(LOGIN_FAILURE_URL)

                // configure logout page
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/img/**",
                "/styles/**",
                "/h2-console/**");
    }
}