package co.jinear.core.system.gcloud.gmail.model.response;

import co.jinear.core.system.gcloud.gmail.model.GmailThreadInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GmailListThreadsResponse {

    private List<GmailThreadInfo> threads;
    private String nextPageToken;
    private Integer resultSizeEstimate;
}
