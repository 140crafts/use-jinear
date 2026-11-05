package co.jinear.core.topic;

import co.jinear.core.converter.topic.TopicInitializeVoConverter;
import co.jinear.core.converter.topic.TopicUpdateVoConverterImpl;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.topic.TopicManager;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.request.topic.TopicInitializeRequest;
import co.jinear.core.model.request.topic.TopicUpdateRequest;
import co.jinear.core.model.vo.topic.TopicUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.topic.TopicInitializeService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.topic.TopicUpdateService;
import co.jinear.core.validator.common.ColorValidator;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TopicManagerTest {

    private final TopicInitializeService topicInitializeService = Mockito.mock(TopicInitializeService.class);
    private final TopicRetrieveService topicRetrieveService = Mockito.mock(TopicRetrieveService.class);
    private final TopicUpdateService topicUpdateService = Mockito.mock(TopicUpdateService.class);
    private final SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
    private final TopicInitializeVoConverter topicInitializeVoConverter = Mockito.mock(TopicInitializeVoConverter.class);
    private final TopicManager topicManager = new TopicManager(
            topicInitializeService,
            topicRetrieveService,
            topicUpdateService,
            sessionInfoService,
            Mockito.mock(WorkspaceValidator.class),
            Mockito.mock(TeamAccessValidator.class),
            new ColorValidator(),
            topicInitializeVoConverter,
            new TopicUpdateVoConverterImpl());

    @ParameterizedTest
    @ValueSource(strings = {"QA-1", "qa 1", "ÇAY", "a_b", "abcdefghijklmnopq"})
    void initializeRefusesATagThatIsNotLettersAndDigits(String tag) {
        TopicInitializeRequest request = initializeRequest(tag);

        assertThatThrownBy(() -> topicManager.initializeTopic(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("topic.tag-invalid");
        Mockito.verifyNoInteractions(topicInitializeService);
    }

    @Test
    void initializeAcceptsLettersAndDigits() {
        TopicInitializeRequest request = initializeRequest("QA1");

        topicManager.initializeTopic(request);

        Mockito.verify(topicInitializeService).initializeTopic(Mockito.any());
    }

    @Test
    void updateRefusesATagWithASymbol() {
        Mockito.when(topicRetrieveService.retrieve("topic-1")).thenReturn(new TopicDto());
        TopicUpdateRequest request = updateRequest("QA-1");

        assertThatThrownBy(() -> topicManager.updateTopic(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("topic.tag-invalid");
        Mockito.verifyNoInteractions(topicUpdateService);
    }

    @Test
    void updateWithoutATagSkipsTheTagCheck() {
        Mockito.when(topicRetrieveService.retrieve("topic-1")).thenReturn(new TopicDto());
        TopicUpdateRequest request = updateRequest(null);
        request.setName("Renamed");

        topicManager.updateTopic(request);

        ArgumentCaptor<TopicUpdateVo> captor = ArgumentCaptor.forClass(TopicUpdateVo.class);
        Mockito.verify(topicUpdateService).updateTopic(captor.capture());
        assertThat(captor.getValue().getTag()).isNull();
    }

    @Test
    void updateStoresTheTagInTheSameFormAsInitialize() {
        Mockito.when(topicRetrieveService.retrieve("topic-1")).thenReturn(new TopicDto());
        TopicUpdateRequest request = updateRequest("QA1");

        topicManager.updateTopic(request);

        ArgumentCaptor<TopicUpdateVo> captor = ArgumentCaptor.forClass(TopicUpdateVo.class);
        Mockito.verify(topicUpdateService).updateTopic(captor.capture());
        assertThat(captor.getValue().getTag()).isEqualTo("qa1");
    }

    @Test
    void initializeRefusesAColorThatIsNotHex() {
        TopicInitializeRequest request = initializeRequest("QATWO");
        request.setColor("ZZZZZZ");

        assertThatThrownBy(() -> topicManager.initializeTopic(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("common.error.invalid-color");
        Mockito.verifyNoInteractions(topicInitializeService);
    }

    @Test
    void updateRefusesAColorThatIsNotHex() {
        Mockito.when(topicRetrieveService.retrieve("topic-1")).thenReturn(new TopicDto());
        TopicUpdateRequest request = updateRequest(null);
        request.setColor("#FF6600");

        assertThatThrownBy(() -> topicManager.updateTopic(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("common.error.invalid-color");
        Mockito.verifyNoInteractions(topicUpdateService);
    }

    private TopicInitializeRequest initializeRequest(String tag) {
        TopicInitializeRequest request = new TopicInitializeRequest();
        request.setWorkspaceId("workspace-1");
        request.setTeamId("team-1");
        request.setName("QA Topic");
        request.setTag(tag);
        return request;
    }

    private TopicUpdateRequest updateRequest(String tag) {
        TopicUpdateRequest request = new TopicUpdateRequest();
        request.setTopicId("topic-1");
        request.setTag(tag);
        return request;
    }
}
