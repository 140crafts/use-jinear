package co.jinear.core.system.gcloud.googleapis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GmailMessageVo {

    private String id;
    private String threadId;
    private List<String> labelIds;
    private String snippet;
    private GoogleMessagePart payload;
    private Long sizeEstimate;
    private String historyId;
    private String internalDate;
}
