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
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**", "/uploads/**", "/error", "/ws-chat/**").permitAll() // ¡Permitir WS!

                        // Reglas de ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/repartidores/*/aprobar").hasRole("ADMIN") // endpoint de aprobar
                        .requestMatchers(HttpMethod.GET, "/repartidores/aprobado**").hasRole("ADMIN")

                        // Endpoints públicos de lectura
                        .requestMatchers(HttpMethod.GET, "/productos/**", "/empresas/**", "/categorias/**", "/aperturas/**", "/tiposcocina/**").permitAll()

                        // Registro (Público)
                        .requestMatchers(HttpMethod.POST, "/clientes", "/empresas").permitAll()

                        // Operaciones de gestión (Admin o Restaurante)
                        .requestMatchers(HttpMethod.POST, "/productos/**", "/empresas/**", "/categorias/**").hasAnyRole("ADMIN", "EMPRESA")
                        .requestMatchers(HttpMethod.PUT, "/productos/**", "/empresas/**").hasAnyRole("ADMIN", "EMPRESA")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**", "/empresas/**", "/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pedidos/**").hasAnyRole("CLIENTE", "EMPRESA", "ADMIN", "REPARTIDOR")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/*/cancelar").hasAnyRole("CLIENTE", "EMPRESA", "ADMIN")
                        
                        // Mensajes (Solo Cliente y Repartidor)
                        .requestMatchers("/mensajes/**").hasAnyRole("CLIENTE", "REPARTIDOR")

                        // Acceso compartido (Admin y Cliente)
                        // Esta regla ahora cubrirá GET /clientes y cualquier subruta
                        .requestMatchers("/carrito/**",  "/clientes/**").hasAnyRole("ADMIN", "CLIENTE", "REPARTIDOR")

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
