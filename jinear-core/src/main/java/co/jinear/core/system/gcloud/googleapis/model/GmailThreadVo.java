package co.jinear.core.system.gcloud.googleapis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GmailThreadVo {

    private String id;
    private String historyId;
    private String snippet;
    private List<GmailMessageVo> messages;
}
