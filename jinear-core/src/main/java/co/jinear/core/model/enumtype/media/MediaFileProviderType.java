package co.jinear.core.model.enumtype.media;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaFileProviderType {

    GCLOUD(0),
    MINIO(1);

    private final int value;
}
