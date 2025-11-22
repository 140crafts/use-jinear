package co.jinear.core.model.dto.media;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@ToString
public class WaitingMediaResultDto {

    private String presignedUrl;
    private String mediaId;
    private ZonedDateTime uploadWindowExpiresAt;
}
