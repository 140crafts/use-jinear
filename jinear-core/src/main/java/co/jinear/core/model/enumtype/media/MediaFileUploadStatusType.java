package co.jinear.core.model.enumtype.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaFileUploadStatusType {

    WAITING(0),
    COMPLETED(1),
    FAILED(2);

    private final int value;
}
