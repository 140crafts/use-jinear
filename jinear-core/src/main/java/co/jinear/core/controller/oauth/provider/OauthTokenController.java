package co.jinear.core.controller.oauth.provider;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.oauth.provider.OauthTokenManager;
import co.jinear.core.model.vo.oauth.OauthClientMetadataVo;
import co.jinear.core.service.oauth.provider.OauthErrorMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "v1/oauth")
@RequiredArgsConstructor
public class OauthTokenController {

    private final OauthTokenManager oauthTokenManager;
    private final OauthErrorMapper oauthErrorMapper;

    @PostMapping(value = "/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> token(@RequestParam Map<String, String> form) {
        try {
            return ResponseEntity.ok()
                    .header("Cache-Control", "no-store")
                    .header("Pragma", "no-cache")
                    .body(oauthTokenManager.token(form));
        } catch (BusinessException exception) {
            return oauthError(exception);
        }
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> register(@RequestBody JsonNode body) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(oauthTokenManager.register(toMetadata(body)));
        } catch (BusinessException exception) {
            return oauthError(exception);
        }
    }

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> revoke(@RequestParam Map<String, String> form) {
        oauthTokenManager.revoke(form.get("token"));
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Map<String, Object>> oauthError(BusinessException exception) {
        String errorCode = oauthErrorMapper.errorCodeFor(exception.getMessage());
        HttpStatus status = oauthErrorMapper.statusFor(errorCode);
        log.warn("[OAUTH] OAuth endpoint returning {} {}", status.value(), errorCode);
        return ResponseEntity.status(status)
                .header("Cache-Control", "no-store")
                .body(oauthErrorMapper.body(errorCode, null));
    }

    private OauthClientMetadataVo toMetadata(JsonNode body) {
        OauthClientMetadataVo vo = new OauthClientMetadataVo();
        vo.setClientName(text(body, "client_name"));
        vo.setClientUri(text(body, "client_uri"));
        vo.setLogoUri(text(body, "logo_uri"));
        vo.setPolicyUri(text(body, "policy_uri"));
        vo.setTosUri(text(body, "tos_uri"));
        vo.setRedirectUris(textList(body, "redirect_uris"));
        vo.setGrantTypes(textList(body, "grant_types"));
        vo.setTokenEndpointAuthMethod(text(body, "token_endpoint_auth_method"));
        vo.setSoftwareId(text(body, "software_id"));
        vo.setSoftwareVersion(text(body, "software_version"));
        return vo;
    }

    private String text(JsonNode node, String field) {
        JsonNode value = Objects.isNull(node) ? null : node.get(field);
        return Objects.nonNull(value) && value.isTextual() ? value.asText() : null;
    }

    private List<String> textList(JsonNode node, String field) {
        List<String> values = new ArrayList<>();
        JsonNode value = Objects.isNull(node) ? null : node.get(field);
        if (Objects.nonNull(value) && value.isArray()) {
            value.forEach(item -> {
                if (item.isTextual()) {
                    values.add(item.asText());
                }
            });
        }
        return values;
    }

}
