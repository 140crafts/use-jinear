package co.jinear.core.system.oauth;

import lombok.experimental.UtilityClass;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Appends OAuth response parameters to a client's redirect URI. The parameter names are fixed
 * by the specification and vary per outcome, so they arrive as a small ordered map.
 */
@UtilityClass
public class OauthRedirectUriBuilder {

    public static String buildRedirect(String redirectUri, Map<String, String> params, String state) {
        Map<String, String> all = new LinkedHashMap<>(params);
        if (Objects.nonNull(state) && !state.isBlank()) {
            all.put("state", state);
        }
        StringBuilder builder = new StringBuilder(redirectUri);
        builder.append(redirectUri.contains("?") ? "&" : "?");
        boolean first = true;
        for (Map.Entry<String, String> entry : all.entrySet()) {
            if (!first) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            first = false;
        }
        return builder.toString();
    }
}
