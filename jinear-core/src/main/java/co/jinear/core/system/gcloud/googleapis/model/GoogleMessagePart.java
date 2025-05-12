package co.jinear.core.system.gcloud.googleapis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleMessagePart {
    private String partId;
    private String mimeType;
    private String filename;
    private List<GoogleBatchResponseHeader> headers;
    private GoogleBatchMessageBody body;
    private List<GoogleMessagePart> parts;
}
