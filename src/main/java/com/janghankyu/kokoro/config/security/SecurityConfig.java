package com.janghankyu.kokoro.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/image/**","/api/members/{id}").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/sign-in", "/api/sign-up", "/api/refresh-token").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/api/members/{id}/**").authenticated()
                    .anyRequest().hasAnyRole("ROLE_ADMIN")
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().mvcMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**");
    }
}
