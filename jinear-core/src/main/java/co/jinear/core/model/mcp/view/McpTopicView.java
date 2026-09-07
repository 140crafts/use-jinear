package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpTopicView implements McpToolPayload {

    @McpField("Topic id, the value create_task takes as topicId.")
    private String topicId;

    @McpField("Team this topic belongs to.")
    private String teamId;

    @McpField("Display name.")
    private String name;

    @McpField("Short prefix used in task references.")
    private String tag;

    @McpField("Display colour.")
    private String color;
}
