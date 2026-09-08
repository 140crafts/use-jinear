package co.jinear.core.mcp;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the published tool catalog. This test only reads: regenerating is
 * {@link McpToolManifestExporter}'s job, run by hand.
 */
class McpToolManifestExportTest {

    private static final String REGENERATE_HINT =
            "The MCP tool catalog changed but jinear-site/lib/mcp-tools.generated.json was not regenerated. "
                    + "Run McpToolManifestExporter.main and commit the refreshed file with the tool change, "
                    + "so the published documentation matches the server.";

    @Test
    void theCheckedInManifestMatchesTheLiveToolCatalog() throws Exception {
        assertThat(McpToolManifestExporter.PUBLISHED)
                .as("the published manifest is missing. " + REGENERATE_HINT)
                .exists();

        String committed = Files.readString(McpToolManifestExporter.PUBLISHED, StandardCharsets.UTF_8);

        assertThat(committed).as(REGENERATE_HINT).isEqualTo(McpToolManifestExporter.render());
    }

    @Test
    void theManifestCarriesNoInstanceSpecificUrl() {
        assertThat(McpToolManifestExporter.render())
                .as("a tool description or schema leaked a host from the test fixture into the "
                        + "published catalog, which self hosted instances also read")
                .doesNotContain("api.jinear.co", "jinear.test");
    }
}
