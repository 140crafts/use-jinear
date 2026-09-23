package co.jinear.core.mcp;

import co.jinear.core.model.mcp.schema.McpField;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

final class McpStubInputs {

    private McpStubInputs() {
    }

    static class NoArguments {
    }

    @Getter
    @Setter
    static class TitleOnly {

        @NotBlank
        @McpField("What to write.")
        private String title;
    }
}
