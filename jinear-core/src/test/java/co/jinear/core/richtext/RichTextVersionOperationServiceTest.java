package co.jinear.core.richtext;

import co.jinear.core.model.entity.richtext.RichTextVersion;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.richtext.RecordRichTextVersionVo;
import co.jinear.core.repository.richtext.RichTextVersionRepository;
import co.jinear.core.service.richtext.version.RichTextVersionOperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class RichTextVersionOperationServiceTest {

    private static final String RICH_TEXT_ID = "rich-text-1";
    private static final String ACCOUNT_ID = "account-1";
    private static final String SESSION_INFO_ID = "session-1";
    private static final String OTHER_SESSION_INFO_ID = "session-2";
    private static final String OLD_HTML = "<p>old</p>";
    private static final String NEW_HTML = "<p>new</p>";
    private static final String OLD_TITLE = "Old title";
    private static final String NEW_TITLE = "New title";

    private final RichTextVersionRepository richTextVersionRepository = Mockito.mock(RichTextVersionRepository.class);
    private final RichTextVersionOperationService richTextVersionOperationService = new RichTextVersionOperationService(richTextVersionRepository);

    @BeforeEach
    void setUp() {
        Mockito.when(richTextVersionRepository.save(any(RichTextVersion.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void aRichTextTypeWithoutVersionsIsNotRecorded() {
        richTextVersionOperationService.record(recordVo(RichTextType.TASK_COMMENT, NEW_HTML, OLD_TITLE));

        Mockito.verify(richTextVersionRepository, Mockito.never()).save(any());
    }

    @Test
    void theSameHtmlAsTheLatestVersionIsNotRecorded() {
        latestVersionIs(version(OLD_HTML, OLD_TITLE));

        richTextVersionOperationService.record(recordVo(RichTextType.NOTE, OLD_HTML, OLD_TITLE));

        Mockito.verify(richTextVersionRepository, Mockito.never()).save(any());
    }

    @Test
    void anEditInTheVersionWindowOverwritesTheOpenVersion() {
        RichTextVersion openVersion = version(OLD_HTML, OLD_TITLE);
        latestVersionIs(openVersion);
        openVersionIs(Optional.of(openVersion));

        richTextVersionOperationService.record(recordVo(RichTextType.NOTE, NEW_HTML, OLD_TITLE));

        RichTextVersion saved = savedVersion();
        assertThat(saved).isSameAs(openVersion);
        assertThat(saved.getHtml()).isEqualTo(NEW_HTML);
    }

    @Test
    void anEditAfterTheVersionWindowCreatesANewVersion() {
        latestVersionIs(version(OLD_HTML, OLD_TITLE));
        openVersionIs(Optional.empty());

        richTextVersionOperationService.record(recordVo(RichTextType.NOTE, NEW_HTML, OLD_TITLE));

        RichTextVersion saved = savedVersion();
        assertThat(saved.getRichTextVersionId()).isNull();
        assertThat(saved.getRichTextId()).isEqualTo(RICH_TEXT_ID);
        assertThat(saved.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(saved.getSessionInfoId()).isEqualTo(SESSION_INFO_ID);
        assertThat(saved.getHtml()).isEqualTo(NEW_HTML);
    }

    @Test
    void anEditFromAnotherSessionInTheVersionWindowCreatesANewVersion() {
        RichTextVersion openVersion = version(OLD_HTML, OLD_TITLE);
        latestVersionIs(openVersion);
        openVersionIs(Optional.of(openVersion));

        RecordRichTextVersionVo vo = recordVo(RichTextType.NOTE, NEW_HTML, OLD_TITLE);
        vo.setSessionInfoId(OTHER_SESSION_INFO_ID);
        richTextVersionOperationService.record(vo);

        RichTextVersion saved = savedVersion();
        assertThat(saved).isNotSameAs(openVersion);
        assertThat(saved.getRichTextVersionId()).isNull();
        assertThat(saved.getSessionInfoId()).isEqualTo(OTHER_SESSION_INFO_ID);
        assertThat(saved.getHtml()).isEqualTo(NEW_HTML);
        assertThat(openVersion.getHtml()).isEqualTo(OLD_HTML);
    }

    @Test
    void theFirstEditCreatesTheFirstVersion() {
        latestVersionIs(null);
        openVersionIs(Optional.empty());

        richTextVersionOperationService.record(recordVo(RichTextType.NOTE, NEW_HTML, OLD_TITLE));

        assertThat(savedVersion().getHtml()).isEqualTo(NEW_HTML);
    }

    @Test
    void aTitleChangeWithTheSameHtmlIsRecorded() {
        RichTextVersion openVersion = version(OLD_HTML, OLD_TITLE);
        latestVersionIs(openVersion);
        openVersionIs(Optional.of(openVersion));

        richTextVersionOperationService.record(recordVo(RichTextType.NOTE, OLD_HTML, NEW_TITLE));

        RichTextVersion saved = savedVersion();
        assertThat(saved.getHtml()).isEqualTo(OLD_HTML);
        assertThat(saved.getTitle()).isEqualTo(NEW_TITLE);
    }

    @Test
    void theTitleIsStoredWithANewVersion() {
        latestVersionIs(null);
        openVersionIs(Optional.empty());

        richTextVersionOperationService.record(recordVo(RichTextType.NOTE, NEW_HTML, NEW_TITLE));

        assertThat(savedVersion().getTitle()).isEqualTo(NEW_TITLE);
    }

    private void latestVersionIs(RichTextVersion version) {
        Mockito.when(richTextVersionRepository.findFirstByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(RICH_TEXT_ID))
                .thenReturn(Optional.ofNullable(version));
    }

    private void openVersionIs(Optional<RichTextVersion> version) {
        Mockito.when(richTextVersionRepository.findFirstByRichTextIdAndAccountIdAndSessionInfoIdAndCreatedDateAfterAndPassiveIdIsNullOrderByCreatedDateDesc(eq(RICH_TEXT_ID), eq(ACCOUNT_ID), eq(SESSION_INFO_ID), any(Date.class)))
                .thenReturn(version);
    }

    private RichTextVersion savedVersion() {
        ArgumentCaptor<RichTextVersion> captor = ArgumentCaptor.forClass(RichTextVersion.class);
        Mockito.verify(richTextVersionRepository).save(captor.capture());
        return captor.getValue();
    }

    private RecordRichTextVersionVo recordVo(RichTextType type, String html, String title) {
        RecordRichTextVersionVo vo = new RecordRichTextVersionVo();
        vo.setRichTextId(RICH_TEXT_ID);
        vo.setType(type);
        vo.setAccountId(ACCOUNT_ID);
        vo.setSessionInfoId(SESSION_INFO_ID);
        vo.setHtml(html);
        vo.setTitle(title);
        return vo;
    }

    private RichTextVersion version(String html, String title) {
        RichTextVersion version = new RichTextVersion();
        version.setRichTextVersionId("version-1");
        version.setRichTextId(RICH_TEXT_ID);
        version.setAccountId(ACCOUNT_ID);
        version.setSessionInfoId(SESSION_INFO_ID);
        version.setHtml(html);
        version.setTitle(title);
        return version;
    }
}
