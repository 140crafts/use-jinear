package co.jinear.core.controller.advice;

import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.controller.oauth.provider.OauthDiscoveryController;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.concurrent.TimeUnit;

/**
 * Sets the caching headers the OAuth specifications require: the discovery documents are
 * publicly cacheable, everything else on these endpoints carries a token and must not be
 * stored. Applying it here keeps header handling out of the controllers.
 */
@RestControllerAdvice(basePackages = "co.jinear.core.controller.oauth.provider")
@RequiredArgsConstructor
public class OauthCacheHeaderAdvice implements ResponseBodyAdvice<Object> {

    private final OauthProperties oauthProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        if (headers.containsKey(HttpHeaders.CACHE_CONTROL)) {
            return body;
        }
        if (OauthDiscoveryController.class.equals(returnType.getContainingClass())) {
            headers.setCacheControl(CacheControl
                    .maxAge(oauthProperties.getDiscoveryCacheMinutes(), TimeUnit.MINUTES)
                    .cachePublic());
            return body;
        }
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        return body;
    }
}
