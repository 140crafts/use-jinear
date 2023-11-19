package co.jinear.core.system.gcloud.auth.model.enumtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessType {

    @JsonProperty("online")
    ONLINE("online"),
    @JsonProperty("offline")
    OFFLINE("offline");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
