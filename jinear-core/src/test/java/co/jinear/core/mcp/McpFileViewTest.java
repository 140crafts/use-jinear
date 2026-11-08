package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.view.McpFileView;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class McpFileViewTest {

    private final McpViewConverter mcpViewConverter = new McpViewConverter();

    @Test
    void aFileCarriesItsTypeSizeAndCreationInstant() {
        AccessibleMediaDto media = new AccessibleMediaDto();
        media.setContentType("application/pdf");
        media.setSize(20480L);
        MaterialDto material = material(MaterialType.FILE);
        material.setMedia(media);

        McpFileView view = mcpViewConverter.file(material);

        assertThat(view.getContentType()).isEqualTo("application/pdf");
        assertThat(view.getSize()).isEqualTo(20480L);
        assertThat(view.getCreatedAt()).isEqualTo("2026-11-01T09:30:00Z");
    }

    @Test
    void aFolderHasNoTypeOrSizeButKeepsItsCreationInstant() {
        McpFileView view = mcpViewConverter.file(material(MaterialType.FOLDER));

        assertThat(view.getContentType()).isNull();
        assertThat(view.getSize()).isNull();
        assertThat(view.getCreatedAt()).isEqualTo("2026-11-01T09:30:00Z");
    }

    private MaterialDto material(MaterialType materialType) {
        MaterialDto material = new MaterialDto();
        material.setMaterialId("m1");
        material.setMaterialType(materialType);
        material.setCreatedDate(Date.from(Instant.parse("2026-11-01T09:30:00Z")));
        return material;
    }
}
