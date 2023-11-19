package co.jinear.core.model.enumtype.google;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserConsentPurposeType {
    LOGIN(0),
    ATTACH_ACCOUNT(1);

    private final int value;
}
