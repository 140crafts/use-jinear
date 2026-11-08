package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpContextTopicView {

    @McpField("Topic id, the value create_task takes as topicId.")
    private String topicId;

    @McpField("Display name.")
    private String name;
}
