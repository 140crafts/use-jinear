package co.jinear.core.manager.richtext;

import co.jinear.core.model.dto.richtext.RichTextAppendDto;
import co.jinear.core.model.dto.richtext.RichTextStateDto;
import co.jinear.core.model.dto.richtext.RichTextUpdatesDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.request.richtext.AppendRichTextUpdateRequest;
import co.jinear.core.model.request.richtext.SeedRichTextRequest;
import co.jinear.core.model.request.richtext.SnapshotRichTextRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.richtext.RichTextAppendResponse;
import co.jinear.core.model.response.richtext.RichTextStateResponse;
import co.jinear.core.model.response.richtext.RichTextUpdatesResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.richtext.RichTextRetrieveService;
import co.jinear.core.service.richtext.sync.RichTextSyncAuthResolver;
import co.jinear.core.service.richtext.sync.RichTextSyncOperationService;
import co.jinear.core.service.richtext.sync.RichTextSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextSyncManager {

    private final SessionInfoService sessionInfoService;
    private final RichTextRetrieveService richTextRetrieveService;
    private final RichTextSyncAuthResolver richTextSyncAuthResolver;
    private final RichTextSyncOperationService richTextSyncOperationService;
    private final RichTextSyncService richTextSyncService;

    public RichTextStateResponse retrieveState(String richTextId) {
        authorizeRead(richTextId);
        log.info("Retrieve rich text state has started. richTextId: {}", richTextId);
        RichTextStateDto state = richTextSyncService.retrieveState(richTextId);
        RichTextStateResponse response = new RichTextStateResponse();
        response.setRichTextStateDto(state);
        return response;
    }

    public RichTextUpdatesResponse retrieveUpdates(String richTextId, long since) {
        authorizeRead(richTextId);
        log.info("Retrieve rich text updates has started. richTextId: {}, since: {}", richTextId, since);
        RichTextUpdatesDto richTextUpdatesDto = richTextSyncService.getUpdates(richTextId, since);
        RichTextUpdatesResponse response = new RichTextUpdatesResponse();
        response.setRichTextUpdatesDto(richTextUpdatesDto);
        return response;
    }

    public RichTextAppendResponse appendUpdate(String richTextId, AppendRichTextUpdateRequest request) {
        authorizeWrite(richTextId);
        log.info("Append rich text update has started. richTextId: {}", richTextId);
        RichTextAppendDto richTextAppendDto = richTextSyncOperationService.appendUpdate(richTextId, request.getUpdate(), request.getHtml());
        RichTextAppendResponse response = new RichTextAppendResponse();
        response.setRichTextAppendDto(richTextAppendDto);
        return response;
    }

    public RichTextStateResponse seed(String richTextId, SeedRichTextRequest request) {
        authorizeWrite(richTextId);
        log.info("Seed rich text has started. richTextId: {}", richTextId);
        RichTextStateDto richTextStateDto = richTextSyncOperationService.seed(richTextId, request.getState());
        RichTextStateResponse response = new RichTextStateResponse();
        response.setRichTextStateDto(richTextStateDto);
        return response;
    }

    public BaseResponse snapshot(String richTextId, SnapshotRichTextRequest request) {
        authorizeWrite(richTextId);
        log.info("Snapshot rich text has started. richTextId: {}, upToSeq: {}", richTextId, request.getUpToSeq());
        richTextSyncOperationService.snapshot(richTextId, request.getState(), request.getUpToSeq());
        return new BaseResponse();
    }

    private void authorizeRead(String richTextId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        RichText richText = richTextRetrieveService.retrieveEntity(richTextId);
        richTextSyncAuthResolver.validateCanRead(currentAccountId, richText);
    }

    private void authorizeWrite(String richTextId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        RichText richText = richTextRetrieveService.retrieveEntity(richTextId);
        richTextSyncAuthResolver.validateCanWrite(currentAccountId, richText);
    }
}
