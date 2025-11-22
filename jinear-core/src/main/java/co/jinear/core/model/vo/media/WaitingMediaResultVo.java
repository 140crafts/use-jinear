package co.jinear.core.model.vo.media;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingMediaResultVo extends MediaInitializeResultVo {
    private URL presignedUrl;
    private ZonedDateTime uploadWindowExpiresAt;
}
