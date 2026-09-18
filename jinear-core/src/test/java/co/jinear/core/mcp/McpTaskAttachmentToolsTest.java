package co.jinear.core.mcp;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.mcp.McpLinkConverter;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.task.AddTaskAttachmentTool;
import co.jinear.core.manager.mcp.tool.task.ListTaskAttachmentsTool;
import co.jinear.core.manager.task.TaskMediaManager;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.view.McpListView;
import co.jinear.core.model.mcp.view.McpTaskAttachmentAcknowledgementView;
import co.jinear.core.model.mcp.view.McpTaskAttachmentView;
import co.jinear.core.model.response.task.TaskMediaResponse;
import co.jinear.core.model.response.task.TaskMediaUploadResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class McpTaskAttachmentToolsTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String DOWNLOAD_URL = "https://api.jinear.co/v1/task/media/task-1/download/media-1";

    private final TaskMediaManager taskMediaManager = Mockito.mock(TaskMediaManager.class);
    private final McpLinkConverter linkConverter = new McpLinkConverter(new FeProperties(), McpRealCatalog.oauthProperties());

    @BeforeEach
    void uploadSucceeds() {
        Mockito.when(taskMediaManager.uploadTaskMedia(Mockito.eq("task-1"), Mockito.any()))
                .thenReturn(new TaskMediaUploadResponse("media-1"));
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("account-1").scopes(Set.of()).build();
    }

    private McpToolResult add(String json) throws Exception {
        return new AddTaskAttachmentTool(taskMediaManager, linkConverter).call(context(), args(json));
    }

    private MultipartFile uploaded() {
        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);
        Mockito.verify(taskMediaManager).uploadTaskMedia(Mockito.eq("task-1"), captor.capture());
        return captor.getValue();
    }

    private void assertNothingUploaded() {
        Mockito.verify(taskMediaManager, Mockito.never()).uploadTaskMedia(Mockito.any(), Mockito.any());
    }

    @Test
    void textIsStoredAsUtf8WithAContentTypeGuessedFromTheName() throws Exception {
        McpToolResult result = add("{\"taskId\":\"task-1\",\"fileName\":\"notes.txt\",\"text\":\"Grüße\"}");

        byte[] expected = "Grüße".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = uploaded();
        assertThat(file.getBytes()).isEqualTo(expected);
        assertThat(file.getOriginalFilename()).isEqualTo("notes.txt");
        assertThat(file.getContentType()).isEqualTo("text/plain");
        McpTaskAttachmentAcknowledgementView view = (McpTaskAttachmentAcknowledgementView) result.getStructuredContent();
        assertThat(result.isError()).isFalse();
        assertThat(view.getMediaId()).isEqualTo("media-1");
        assertThat(view.getSize()).isEqualTo(expected.length);
        assertThat(view.getDownloadUrl()).isEqualTo(DOWNLOAD_URL);
    }

    @Test
    void base64IsDecodedEvenWhenItIsWrapped() throws Exception {
        McpToolResult result = add("{\"taskId\":\"task-1\",\"fileName\":\"chart.png\",\"contentBase64\":\"iVBO\\nRw==\"}");

        MultipartFile file = uploaded();
        assertThat(result.isError()).isFalse();
        assertThat(file.getBytes()).isEqualTo(new byte[]{(byte) 0x89, 'P', 'N', 'G'});
        assertThat(file.getContentType()).isEqualTo("image/png");
    }

    @Test
    void anExplicitContentTypeWins() throws Exception {
        add("{\"taskId\":\"task-1\",\"fileName\":\"notes.txt\",\"contentType\":\"text/markdown\",\"text\":\"# Title\"}");

        assertThat(uploaded().getContentType()).isEqualTo("text/markdown");
    }

    @Test
    void anUnknownExtensionFallsBackToAGenericBinaryType() throws Exception {
        add("{\"taskId\":\"task-1\",\"fileName\":\"blob.jinearunknown\",\"contentBase64\":\"AAEC\"}");

        assertThat(uploaded().getContentType()).isEqualTo("application/octet-stream");
    }

    @Test
    void bothContentFieldsAreRefused() throws Exception {
        McpToolResult result = add("{\"taskId\":\"task-1\",\"fileName\":\"a.txt\",\"text\":\"a\",\"contentBase64\":\"YQ==\"}");

        assertThat(result.isError()).isTrue();
        assertNothingUploaded();
    }

    @Test
    void missingContentIsRefused() throws Exception {
        McpToolResult result = add("{\"taskId\":\"task-1\",\"fileName\":\"a.txt\"}");

        assertThat(result.isError()).isTrue();
        assertNothingUploaded();
    }

    @Test
    void invalidBase64IsRefused() throws Exception {
        McpToolResult result = add("{\"taskId\":\"task-1\",\"fileName\":\"a.png\",\"contentBase64\":\"not base64!\"}");

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).contains("contentBase64");
        assertNothingUploaded();
    }

    @Test
    void emptyContentIsRefused() throws Exception {
        McpToolResult result = add("{\"taskId\":\"task-1\",\"fileName\":\"a.txt\",\"text\":\"\"}");

        assertThat(result.isError()).isTrue();
        assertNothingUploaded();
    }

    @Test
    void everyListedAttachmentCarriesItsDownloadUrl() throws Exception {
        MediaDto media = new MediaDto();
        media.setMediaId("media-1");
        media.setRelatedObjectId("task-1");
        media.setOriginalName("notes.txt");
        media.setContentType("text/plain");
        media.setSize(12L);
        media.setVisibility(MediaVisibilityType.PRIVATE);
        TaskMediaResponse response = new TaskMediaResponse();
        response.setData(List.of(media));
        Mockito.when(taskMediaManager.retrieveTaskMediaList("task-1")).thenReturn(response);

        McpToolResult result = new ListTaskAttachmentsTool(taskMediaManager, new McpViewConverter(), linkConverter)
                .call(context(), args("{\"taskId\":\"task-1\"}"));

        McpListView<?> view = (McpListView<?>) result.getStructuredContent();
        McpTaskAttachmentView item = (McpTaskAttachmentView) view.getItems().get(0);
        assertThat(view.getCount()).isEqualTo(1);
        assertThat(item.getName()).isEqualTo("notes.txt");
        assertThat(item.getSize()).isEqualTo(12L);
        assertThat(item.getVisibility()).isEqualTo("PRIVATE");
        assertThat(item.getDownloadUrl()).isEqualTo(DOWNLOAD_URL);
    }
}
