package co.jinear.core.model.dto.media;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class AccessibleMediaDto extends MediaDto {
    private String mediaKey;
    private String storagePath;
}