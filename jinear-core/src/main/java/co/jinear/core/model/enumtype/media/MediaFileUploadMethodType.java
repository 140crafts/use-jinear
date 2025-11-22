package co.jinear.core.model.enumtype.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaFileUploadMethodType {

    LEGACY(0),
    PRESIGNED_URL(1);

    private final int value;
}
