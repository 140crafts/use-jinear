package co.jinear.core.config.security;

import co.jinear.ratelimiter.filter.RateLimitingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final DynamicCorsConfigurationSource dynamicCorsConfigurationSource;
    private final RateLimitingFilter rateLimitingFilter;

    private static final String[] SWAGGER_ENDPOINTS = new String[]{
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };
    private static final String[] PUBLIC_ENDPOINTS = new String[]{
            "/",
            "/v1/auth/otp/email/initialize",
            "/v1/auth/otp/email/complete",
            "/v1/auth/password/email",
            "/v1/auth/sign-in-with-apple",
            "/v1/auth/single-use-token",
            "/v1/account/confirm-email",
            "/v1/account/resend-confirm-email",
            "/v1/account/register/email",
            "/v1/account/password/reset/initialize",
            "/v1/account/password/reset/complete",
            "/v1/workspace/member/invitation/respond",
            "/v1/workspace/member/invitation/info/{token}",
            "/v1/oauth/google/redirect-info/login",
            "/v1/oauth/google/redirect-info/attach-mail",
            "/v1/oauth/google/redirect-info/attach-calendar",
            "/v1/oauth/google/callback/login",
            "/v1/oauth/google/callback/attach-account",
            "/v1/calendar/event/exports/{shareableKey}",
            "/v1/project/public-feed/**",
            "/v1/project/post/comment/list/project/{projectId}/post/{postId}",
            "/v1/captcha/generate",
            "/v1/material/media/{materialId}",
            "/v1/debug/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(dynamicCorsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_ENDPOINTS).denyAll()
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/v1/domain/validate").hasRole("DOMAINSERVER")
                        .requestMatchers("/v1/robots/**").hasRole("ROBOT")
                        .requestMatchers("/v1/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .clearAuthentication(Boolean.TRUE)
                        .invalidateHttpSession(Boolean.TRUE)
                        .deleteCookies("JWT", "JSESSIONID", "SESSION", "SESSIONID")
                );

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(rateLimitingFilter, JwtRequestFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}