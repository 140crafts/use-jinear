package co.jinear.core.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatusType {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;
}
