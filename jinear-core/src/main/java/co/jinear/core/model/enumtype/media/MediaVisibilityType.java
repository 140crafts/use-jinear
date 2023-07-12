package co.jinear.core.model.enumtype.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaVisibilityType {

    PUBLIC(0),
    PRIVATE(1),
    TEMP_PUBLIC(2);

    private final int value;
}
