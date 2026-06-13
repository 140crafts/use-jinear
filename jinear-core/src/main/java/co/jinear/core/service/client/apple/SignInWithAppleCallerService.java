package co.jinear.core.service.client.apple;

import co.jinear.core.config.properties.SignInWithAppleProperties;
import co.jinear.core.service.client.apple.model.IdTokenPayload;
import co.jinear.core.service.client.apple.model.TokenResponse;
import co.jinear.core.system.util.DateHelper;
import com.google.gson.Gson;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "signinwithapple.enabled", havingValue = "true")
public class SignInWithAppleCallerService {

    private final PrivateKey signInWithApplePrivateKey;
    private final SignInWithAppleProperties signInWithAppleProperties;
    private final Gson gson;

    private final RestTemplate signInWithAppleRestTemplate;

    public IdTokenPayload appleAuth(String authorizationCode, boolean forWeb) {
        String clientId = forWeb ? signInWithAppleProperties.getWebClientId() : signInWithAppleProperties.getClientId();
        String clientSecret = generateClientSecret(clientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "authorization_code");
        formData.add("code", authorizationCode);
        if (forWeb) {
            formData.add("redirect_uri", signInWithAppleProperties.getWebRedirectUrl());
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        ResponseEntity<TokenResponse> response = signInWithAppleRestTemplate.postForEntity("/auth/token", requestEntity, TokenResponse.class);

        return Optional.ofNullable(response.getBody())
                .map(TokenResponse::getIdToken)
                .map(this::decodeIdToken)
                .orElseThrow(() -> new IllegalStateException("Could not get a valid id_token from Apple's response."));
    }

    private IdTokenPayload decodeIdToken(String idToken) {
        try {
            // JWTs use Base64URL encoding for the payload.
            String payload = idToken.split("\\.")[1];
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);
            return gson.fromJson(decodedPayload, IdTokenPayload.class);
        } catch (Exception e) {
            log.error("Failed to decode Apple id_token", e);
            throw new IllegalStateException("Failed to decode Apple id_token", e);
        }
    }

    private String generateClientSecret(String clientId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, signInWithAppleProperties.getKeyId())
                .setIssuer(signInWithAppleProperties.getTeamId())
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .setExpiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                .setIssuedAt(Date.from(now))
//                .signWith(signInWithApplePrivateKey) // The modern JJWT API infers the algorithm (ES256) from the key type
                .signWith(SignatureAlgorithm.ES256, signInWithApplePrivateKey)
                .compact();
    }

    private String generateJWT() {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, signInWithAppleProperties.getKeyId())
                .setIssuer(signInWithAppleProperties.getTeamId())
                .setAudience("https://appleid.apple.com")
                .setSubject(signInWithAppleProperties.getClientId())
                .setExpiration(DateHelper.addMinutes(DateHelper.now(), 5))
                .setIssuedAt(DateHelper.now())
                .signWith(SignatureAlgorithm.ES256, signInWithApplePrivateKey)
                .compact();
    }

    private String generateWebJWT() {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, signInWithAppleProperties.getKeyId())
                .setIssuer(signInWithAppleProperties.getTeamId())
                .setAudience("https://appleid.apple.com")
                .setSubject(signInWithAppleProperties.getWebClientId())
                .setExpiration(DateHelper.addMinutes(DateHelper.now(), 5))
                .setIssuedAt(DateHelper.now())
                .signWith(SignatureAlgorithm.ES256, signInWithApplePrivateKey)
                .compact();
    }
}
