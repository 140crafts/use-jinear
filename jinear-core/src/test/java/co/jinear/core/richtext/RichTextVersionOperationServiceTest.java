package co.jinear.core.richtext;

import co.jinear.core.model.entity.richtext.RichTextVersion;
import co.jinear.core.model.enumtype.richtext.RichTextType;
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
    private static final String OLD_HTML = "<p>old</p>";
    private static final String NEW_HTML = "<p>new</p>";

    private final RichTextVersionRepository richTextVersionRepository = Mockito.mock(RichTextVersionRepository.class);
    private final RichTextVersionOperationService richTextVersionOperationService = new RichTextVersionOperationService(richTextVersionRepository);

    @BeforeEach
    void setUp() {
        Mockito.when(richTextVersionRepository.save(any(RichTextVersion.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void aRichTextTypeWithoutVersionsIsNotRecorded() {
        richTextVersionOperationService.record(RICH_TEXT_ID, RichTextType.TASK_COMMENT, ACCOUNT_ID, NEW_HTML);

        Mockito.verify(richTextVersionRepository, Mockito.never()).save(any());
    }

    @Test
    void theSameHtmlAsTheLatestVersionIsNotRecorded() {
        latestVersionIs(version(OLD_HTML));

        richTextVersionOperationService.record(RICH_TEXT_ID, RichTextType.NOTE, ACCOUNT_ID, OLD_HTML);

        Mockito.verify(richTextVersionRepository, Mockito.never()).save(any());
    }

    @Test
    void anEditInTheVersionWindowOverwritesTheOpenVersion() {
        RichTextVersion openVersion = version(OLD_HTML);
        latestVersionIs(openVersion);
        openVersionIs(Optional.of(openVersion));

        richTextVersionOperationService.record(RICH_TEXT_ID, RichTextType.NOTE, ACCOUNT_ID, NEW_HTML);

        RichTextVersion saved = savedVersion();
        assertThat(saved).isSameAs(openVersion);
        assertThat(saved.getHtml()).isEqualTo(NEW_HTML);
    }

    @Test
    void anEditAfterTheVersionWindowCreatesANewVersion() {
        latestVersionIs(version(OLD_HTML));
        openVersionIs(Optional.empty());

        richTextVersionOperationService.record(RICH_TEXT_ID, RichTextType.NOTE, ACCOUNT_ID, NEW_HTML);

        RichTextVersion saved = savedVersion();
        assertThat(saved.getRichTextVersionId()).isNull();
        assertThat(saved.getRichTextId()).isEqualTo(RICH_TEXT_ID);
        assertThat(saved.getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(saved.getHtml()).isEqualTo(NEW_HTML);
    }

    @Test
    void theFirstEditCreatesTheFirstVersion() {
        latestVersionIs(null);
        openVersionIs(Optional.empty());

        richTextVersionOperationService.record(RICH_TEXT_ID, RichTextType.NOTE, ACCOUNT_ID, NEW_HTML);

        assertThat(savedVersion().getHtml()).isEqualTo(NEW_HTML);
    }

    private void latestVersionIs(RichTextVersion version) {
        Mockito.when(richTextVersionRepository.findFirstByRichTextIdAndPassiveIdIsNullOrderByCreatedDateDesc(RICH_TEXT_ID))
                .thenReturn(Optional.ofNullable(version));
    }

    private void openVersionIs(Optional<RichTextVersion> version) {
        Mockito.when(richTextVersionRepository.findFirstByRichTextIdAndAccountIdAndCreatedDateAfterAndPassiveIdIsNullOrderByCreatedDateDesc(eq(RICH_TEXT_ID), eq(ACCOUNT_ID), any(Date.class)))
                .thenReturn(version);
    }

    private RichTextVersion savedVersion() {
        ArgumentCaptor<RichTextVersion> captor = ArgumentCaptor.forClass(RichTextVersion.class);
        Mockito.verify(richTextVersionRepository).save(captor.capture());
        return captor.getValue();
    }

    private RichTextVersion version(String html) {
        RichTextVersion version = new RichTextVersion();
        version.setRichTextVersionId("version-1");
        version.setRichTextId(RICH_TEXT_ID);
        version.setAccountId(ACCOUNT_ID);
        version.setHtml(html);
        return version;
    }
}
