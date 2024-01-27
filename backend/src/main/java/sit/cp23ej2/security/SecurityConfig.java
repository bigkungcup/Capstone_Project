package sit.cp23ej2.security;

// import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.cors(cors -> cors.disable());

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                .requestMatchers("/api/user/**").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/api/book").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/api/book/**").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/api/review").hasAnyAuthority("ROLE_USER")
                .requestMatchers("/api/review/**").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .anyRequest().authenticated());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/**"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    // @Bean
    // public CorsConfigurationSource corsConfiguration() {
    //     CorsConfiguration corsConfig = new CorsConfiguration();
    //     corsConfig.applyPermitDefaultValues();
    //     corsConfig.setAllowCredentials(true);
    //     corsConfig.addAllowedMethod("GET");
    //     corsConfig.addAllowedMethod("PATCH");
    //     corsConfig.addAllowedMethod("POST");
    //     corsConfig.addAllowedMethod("OPTIONS");
    //     corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
    //     corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type"));
    //     corsConfig.setExposedHeaders(Arrays.asList("X-Get-Header"));
    //     UrlBasedCorsConfigurationSource source =
    //             new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", corsConfig);
    //     return source;
    // }

    // @Bean
    // public CorsWebFilter corsWebFilter() {
    //     return new CorsWebFilter(corsConfiguration());
    // }

    

    

}
