package sit.cp23ej2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
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

        
        http.cors(withDefaults()).authorizeHttpRequests(authorize -> authorize

                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/files/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/user/ranking").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/user/ranking/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/user/forgetPassword").permitAll()
                
                .requestMatchers(HttpMethod.GET,"/api/user/all").hasAnyAuthority( "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/user/all/**").hasAnyAuthority( "ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/user/resetPassword").hasAnyAuthority( "ROLE_USER","ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/user/{userId}").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/user/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/user/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/user/admin/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/user/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")

                .requestMatchers(HttpMethod.GET,"/api/book/guest").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book/guest/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book/mostview").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book/random").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book/newBook").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book/ranking").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book/similar/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/book").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/book").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/book/**").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/book/**").hasAnyAuthority( "ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/book/**").hasAnyAuthority( "ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/book/**").hasAnyAuthority( "ROLE_ADMIN")

                .requestMatchers("/api/review/guest").permitAll()
                .requestMatchers("/api/review/guest/**").permitAll()
                .requestMatchers("/api/review/newReview").permitAll()
                .requestMatchers("/api/review/ratingCount").permitAll()
                .requestMatchers("/api/review").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/review/me").permitAll()
                .requestMatchers("/api/review/**").hasAnyAuthority("ROLE_GUEST", "ROLE_USER", "ROLE_ADMIN")

                .requestMatchers("/api/history").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            
                .requestMatchers(HttpMethod.GET,"/api/bookmark").permitAll()
                .requestMatchers("/api/bookmark/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .requestMatchers("/api/booktype").permitAll()
                .requestMatchers("/api/booktype/**").permitAll()

                .requestMatchers("/api/follow").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/follow/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/follow/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/follow/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .requestMatchers("/api/likeStatus").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/api/likeStatus/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .requestMatchers("/api/notification").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/api/notification/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .requestMatchers("/api/report").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/api/report/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // .requestMatchers("/api/bookmarkStatus").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // .requestMatchers("/api/bookmarkStatus/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // .requestMatchers("/api/followerStatus").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                // .requestMatchers("/api/followerStatus/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .anyRequest().authenticated());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    //   @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "*"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
    //     configuration.setAllowedHeaders(Arrays.asList("*"));
    //     configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
    //     configuration.setAllowCredentials(true);
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }


    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("*"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
    //             "DELETE", "OPTIONS"));
    //     configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type",
    //             "x-auth-token"));
    //     configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);

    //     return source;
    // }

}
