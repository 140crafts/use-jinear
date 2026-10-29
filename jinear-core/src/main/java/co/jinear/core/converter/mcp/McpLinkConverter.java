package co.jinear.core.converter.mcp;

import co.jinear.core.config.properties.OauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class McpLinkConverter {

    private final OauthProperties oauthProperties;

    public String taskAttachmentDownloadUrl(String taskId, String mediaId) {
        return oauthProperties.getIssuerUrl() + "/v1/task/media/" + taskId + "/download/" + mediaId;
    }
}
