package co.jinear.core.model.dto.media;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingMediaResultDto {

    private String presignedUrl;
    private String mediaId;
    private ZonedDateTime uploadWindowExpiresAt;
}
