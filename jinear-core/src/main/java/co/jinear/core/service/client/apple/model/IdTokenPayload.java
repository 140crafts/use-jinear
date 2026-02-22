package co.jinear.core.service.client.apple.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IdTokenPayload {

    @JsonProperty("iss")
    private String iss;

    @JsonProperty("aud")
    private String aud;

    @JsonProperty("exp")
    private Long exp;

    @JsonProperty("iat")
    private Long iat;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("at_hash")
    private String atHash;

    @JsonProperty("auth_time")
    private Long authTime;

    @JsonProperty("nonce_supported")
    private Boolean nonceSupported;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("email")
    private String email;

}
