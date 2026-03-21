package co.jinear.core.system.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlContentInfo {

    private String contentType;
    private String fileExtension;
    private Long contentLength;
}
