package co.jinear.core.system.gcloud.googleapis.converter;

import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BatchRequestConverter {

    private static final String GMAIL_MESSAGES_REQ = """
            --%s
            Content-Type: application/http
                        
            GET /gmail/v1/users/%s/messages/%s
            """;
    private static final String GMAIL_THREAD_REQ = """
            --%s
            Content-Type: application/http
                        
            GET /gmail/v1/users/%s/threads/%s
            """;
    private static final String BOUNDARY_END = "--%s--";

    public String convertToMessagesRequestBody(List<RetrieveBatchRequestVo> messageVoList, String boundary) {
        String body = messageVoList.stream()
                .map(retrieveBatchMessageVo -> GMAIL_MESSAGES_REQ.formatted(boundary, retrieveBatchMessageVo.getUserId(), retrieveBatchMessageVo.getResourceId()))
                .collect(Collectors.joining(NormalizeHelper.NEW_LINE));
        body += BOUNDARY_END.formatted(boundary);
        return body;
    }

    public String convertToThreadsRequestBody(List<RetrieveBatchRequestVo> messageVoList, String boundary) {
        String body = messageVoList.stream()
                .map(retrieveBatchMessageVo -> GMAIL_THREAD_REQ.formatted(boundary, retrieveBatchMessageVo.getUserId(), retrieveBatchMessageVo.getResourceId()))
                .collect(Collectors.joining(NormalizeHelper.NEW_LINE));
        body += BOUNDARY_END.formatted(boundary);
        return body;
    }

}
