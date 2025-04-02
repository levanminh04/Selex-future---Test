package com.levanminh.car_rental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( request -> {
                            request
                                    .requestMatchers( // auth
                                            "/",
                                            "/auth/**", // phục vụ cho xác thực (login, đăng ký, ...).
                                            "/v2/api-docs",
                                            "/v3/api-docs",
                                            "/v3/api-docs/**",
                                            "/swagger-resources",
                                            "/swagger-resources/**",
                                            "/configuration/ui",
                                            "/configuration/security",
                                            "/swagger-ui/**",
                                            "/webjars/**",
                                            "/swagger-ui.html",
                                            "/ws/**"  // WebSocket
                                    )
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        }

                )
                .oauth2ResourceServer(auth ->
                        auth.jwt(token -> token.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));
        // Keycloak làm nhiệm vụ IDP (cấp token). Sau khi có token, tất cả các request API tiếp theo sẽ gửi kèm token trong header
        // Spring security được tích hợp Oauth2, Oauth2 chịu trách nhiệm xác thực (authentication) token, nếu xác thực token thành công thì spring security sẽ thực hiện phân quyền (authorization)
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH")
        );
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
