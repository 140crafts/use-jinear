package co.jinear.core.config.security;

import co.jinear.core.config.properties.CorsProperties;
import co.jinear.core.service.project.domain.ProjectDomainRetrieveService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DynamicCorsConfigurationSource implements CorsConfigurationSource {

    private final ProjectDomainRetrieveService projectDomainRetrieveService;
    private final Cache<String, Boolean> domainCache;
    private final CorsProperties corsProperties;

    public DynamicCorsConfigurationSource(ProjectDomainRetrieveService projectDomainRetrieveService, CorsProperties corsProperties) {
        this.projectDomainRetrieveService = projectDomainRetrieveService;
        this.corsProperties = corsProperties;
        this.domainCache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).maximumSize(10000).build();
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        String origin = request.getHeader("Origin");

        if (origin == null) return null;

        if (isValidInternalDomain(origin)) {
            return buildConfig(origin);
        }

        try {
            boolean isAllowed = domainCache.get(origin, () -> {
                String cleanDomain = removeProtocol(origin);
                return projectDomainRetrieveService.checkIfDomainIsExistsAndSetupCompleted(cleanDomain);
            });
            if (isAllowed) {
                return buildConfig(origin);
            }
        } catch (ExecutionException e) {
            log.error("Domain check for CORS failed.", e);
            return null;
        }
        return null;
    }

    private CorsConfiguration buildConfig(String origin) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(origin));
        config.setAllowedMethods(corsProperties.getAllowedMethods());
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(corsProperties.getAllowCredentials());
        return config;
    }

    private boolean isValidInternalDomain(String origin) {
        List<String> allowedOriginPatterns = corsProperties.getAllowedOriginPatterns();
        if (allowedOriginPatterns == null) {
            return false;
        }
        for (String pattern : allowedOriginPatterns) {
            if (PatternMatchUtils.simpleMatch(pattern, origin)) {
                return true;
            }
        }
        return false;
    }

    private String removeProtocol(String url) {
        String http = "http://";
        String https = "https://";
        if (url.startsWith(https)) {
            return url.substring(https.length());
        }
        if (url.startsWith(http)) {
            return url.substring(http.length());
        }
        return url;
    }
}
