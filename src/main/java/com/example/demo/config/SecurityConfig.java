package com.example.demo.config;

import com.example.demo.model.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("vahe")
                        .password("vahe")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();
        http.sessionManagement().maximumSessions(1);

        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        http.formLogin()
                .successHandler(this::onAuthenticationSuccess);

        http.logout().logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.getOutputStream().write("Logout successfull".getBytes());
        });

        http.exceptionHandling()
                .authenticationEntryPoint(this::authenticationEntryPoint);
    }

    private void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setHeaderName("X-Auth-Token");
        loginResponse.setToken(httpServletRequest.getSession().getId());

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON.toString());
        httpServletResponse.getOutputStream().write(this.objectMapper.writeValueAsString(loginResponse).getBytes());
    }

    private void authenticationEntryPoint(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.getOutputStream().write("Access denied".getBytes());
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


}
