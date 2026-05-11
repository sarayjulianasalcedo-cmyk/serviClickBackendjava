package com.sinquinto.serviclick.Config.Infrastructure;

import com.sinquinto.serviclick.Config.Infrastructure.Filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authRequest -> authRequest

                .requestMatchers(
                        "/api/auth/**",
                        "/api/chat/**",
                        "/api/categories/**",
                        "/api/service-offers/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                ).permitAll()

                .anyRequest().authenticated()
        );

        http.sessionManagement(sessionManager ->
                sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authenticationProvider(authenticationProvider);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}