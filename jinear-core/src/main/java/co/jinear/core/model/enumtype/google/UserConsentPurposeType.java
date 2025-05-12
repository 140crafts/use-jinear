package co.jinear.core.model.enumtype.google;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserConsentPurposeType {
    LOGIN(0),
    ATTACH_MAIL(1),
    ATTACH_CALENDAR(2);

    private final int value;
}
