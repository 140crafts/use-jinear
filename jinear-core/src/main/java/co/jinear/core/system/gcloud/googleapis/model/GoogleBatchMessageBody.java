package co.jinear.core.system.gcloud.googleapis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleBatchMessageBody {
    private Long size;
    private String data;
}
