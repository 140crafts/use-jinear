package co.jinear.core.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    private static final String LOGOUT_ENDPOINT = "/v1/auth/logout";
    private static final String[] PUBLIC_ENDPOINTS = new String[]{
            "/",
            "/v1/auth/otp/email/initialize",
            "/v1/auth/otp/email/complete",
            "/v1/auth/password/email",
            "/v1/account/confirm-email",
            "/v1/account/resend-confirm-email",
            "/v1/account/register/email",
            "/v1/account/password/reset/initialize",
            "/v1/account/password/reset/complete",
            "/v1/service-record/with-handle/{handle}",
            "/v1/workspace/member/invitation/respond",
            "/v1/workspace/member/invitation/info/{token}",
            "/v1/oauth/google/redirect-info/login",
            "/v1/oauth/google/redirect-info/attach-mail",
            "/v1/oauth/google/redirect-info/attach-calendar",
            "/v1/oauth/google/callback/login",
            "/v1/oauth/google/callback/attach-account",
            "/v1/calendar/event/exports/{shareableKey}",
            "/v1/project/public-feed/**",
            "/v1/debug/**"
    };

    private static final List<String> CORS_ALLOWED_DOMAINS = Arrays.asList(
            "http://localhost:3000/*",
            "*"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/v1/robots/**").hasRole("ROBOT")
                        .requestMatchers("/v1/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .logout()
                .clearAuthentication(true)
                .logoutSuccessUrl("/").deleteCookies("JWT", "JSESSIONID", "SESSION", "SESSIONID")
                .invalidateHttpSession(true)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("swagger-ui/**")).denyAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).denyAll()
                        .requestMatchers(new AntPathRequestMatcher("v3/api-docs/**")).denyAll()
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).denyAll()
                        .anyRequest().authenticated())
                .httpBasic();
        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(CORS_ALLOWED_DOMAINS);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}