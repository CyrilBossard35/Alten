package com.example.Alten.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdaptater {


    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v3/api-docs", "/v3/api-docs/*").permitAll()

                                // Allow access for the different method
                                .requestMatchers(HttpMethod.GET, "/products").permitAll() // for the method getall
                                .requestMatchers(HttpMethod.GET, "/products/*").permitAll() //for the method get by id
                                .requestMatchers(HttpMethod.POST, "/products/*").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/products/*").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/products/*").permitAll()


                                // Access to other endpoints is restricted to authenticated users
                                .anyRequest().authenticated())
                //Deactivate the csrf par pas besoin en mode stateless
                .csrf(AbstractHttpConfigurer::disable)
                // On s'assure d'être en mode stateless (No COOKIE !!!!)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // S'il y a une erreur, on utilise le point d'entrée authenticationEntryPoint
                // On renvoie un code d'erreur 401, mais on pourrait très bien renvoyer vers la page d'authentification
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(authenticationEntryPoint));
        return http.build();
    }


}