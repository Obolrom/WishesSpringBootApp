package io.romix.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String secret;

  private JwtAuthFilter jwtAuthFilter;

  public static final String[] PUBLIC_ENDPOINTS = new String[] {
      "/api/v1/auth/login",
      "/api/v1/auth/register",
      "/ws/**",
      "/gs-guide-websocket/**",
      "/health",
      "/",
      "/index.html",
      "/app.js"
  };

  @Autowired
  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain configure(@NonNull HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement((sessionManagement) -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((httpRequests) ->
            httpRequests
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest()
                .authenticated())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  private AuthenticationFilter bearerAuthenticationFilter(
      AuthenticationManager authenticationManager) {
    BearerTokenAuthenticationConverter bearerTokenAuthenticationConverter =
        new BearerTokenAuthenticationConverter(new JwtHandler(secret));
    AuthenticationFilter filter = new AuthenticationFilter(authenticationManager, bearerTokenAuthenticationConverter);
    filter.setRequestMatcher(new AntPathRequestMatcher("/**"));

    return filter;
  }
}
