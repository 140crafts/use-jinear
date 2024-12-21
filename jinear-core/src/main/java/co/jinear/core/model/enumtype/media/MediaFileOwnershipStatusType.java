package co.jinear.core.model.enumtype.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaFileOwnershipStatusType {

    WAITING(0),
    OWNED(1);

    private final int value;
}
