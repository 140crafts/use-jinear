package co.jinear.core.model.mcp.view;

import co.jinear.core.model.mcp.schema.McpField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class McpNoteVersionDetailView extends McpNoteVersionView {

    @McpField("Note title in this version, or null when the version has no title.")
    private String title;

    @McpField("Note body as HTML in this version.")
    private String body;
}
