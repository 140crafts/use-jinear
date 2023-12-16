package co.jinear.core.system.gcloud.googleapis;

import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;

import java.util.List;

public interface GoogleApisClient {

    List<GmailMessageVo> retrieveBatchMessages(String token, List<RetrieveBatchRequestVo> messageVoList);

    List<GmailThreadVo> retrieveBatchThreads(String token, List<RetrieveBatchRequestVo> messageVoList);

}
