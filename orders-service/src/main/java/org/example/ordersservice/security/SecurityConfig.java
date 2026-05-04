package org.example.ordersservice.security;

import lombok.RequiredArgsConstructor;
import org.example.ordersservice.services.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUsuarioDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        return http
                .cors(cors->cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOrigin("http://localhost:4200");
                    corsConfiguration.addAllowedOrigin("http://www.delieats.com");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**", "/uploads/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        
                        // Endpoints públicos de lectura (ver menú, locales, etc.)
                        .requestMatchers(HttpMethod.GET, "/productos/**", "/empresas/**", "/categorias/**", "/aperturas/**").permitAll()
                        
                        // Endpoints de gestión (crear/editar productos y empresas)
                        .requestMatchers(HttpMethod.POST, "/clientes", "/empresas").permitAll()
                        .requestMatchers(HttpMethod.POST, "/productos/**", "/empresas/**", "/categorias/**").hasAnyRole("ADMIN", "RESTAURANTE")
                        .requestMatchers(HttpMethod.PUT, "/productos/**", "/empresas/**").hasAnyRole("ADMIN", "RESTAURANTE")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**", "/empresas/**", "/categorias/**").hasRole("ADMIN")
                        
                        // Endpoints propios del cliente (operaciones de compra)
                        .requestMatchers("/carrito/**", "/pedidos/**", "/clientes/**").hasAnyRole("ADMIN", "CLIENTE")

                        // 4. Cerrar el acceso
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Para que el controllador de login pueda autenticar al usuario
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUsuarioDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}
