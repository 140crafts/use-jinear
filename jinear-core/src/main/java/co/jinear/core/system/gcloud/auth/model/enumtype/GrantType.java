package co.jinear.core.system.gcloud.auth.model.enumtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrantType {

    @JsonProperty("authorization_code")
    AUTHORIZATION_CODE("authorization_code"),

    @JsonProperty("refresh_token")
    REFRESH_TOKEN("refresh_token");

    private final String value;
}
