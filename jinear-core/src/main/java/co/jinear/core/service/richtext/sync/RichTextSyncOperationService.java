package co.jinear.core.service.richtext.sync;

import co.jinear.core.model.dto.richtext.RichTextAppendDto;
import co.jinear.core.model.dto.richtext.RichTextDocumentDto;
import co.jinear.core.model.dto.richtext.RichTextStateDto;
import co.jinear.core.model.vo.richtext.AppendRichTextUpdateVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Orchestrates the lock-guarded CRDT write operations (append / seed / snapshot): acquires the
 * {@code RICH_TEXT_SYNC} distributed lock, then delegates the actual read-modify-write to the
 * transactional {@link RichTextSyncService}. Append is locked too so per-rich-text update seqs are
 * assigned serially (contiguous, no reuse) and appends never interleave with a snapshot. Keeping the
 * {@code lock -> transaction -> unlock} sequencing here (rather than in the manager) yields the
 * correct ordering (the transactional core runs on a separate bean, so its proxy still opens a
 * transaction) and keeps locking concerns out of the request/auth layer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RichTextSyncOperationService {

    private final RichTextSyncLockService richTextSyncLockService;
    private final RichTextSyncService richTextSyncService;

    public RichTextDocumentDto retrieveDocument(String richTextId) {
        richTextSyncLockService.lock(richTextId);
        try {
            return richTextSyncService.retrieveDocument(richTextId);
        } finally {
            richTextSyncLockService.unlock(richTextId);
        }
    }

    public RichTextAppendDto appendUpdate(AppendRichTextUpdateVo vo) {
        richTextSyncLockService.lock(vo.getRichTextId());
        try {
            return richTextSyncService.appendUpdate(vo);
        } finally {
            richTextSyncLockService.unlock(vo.getRichTextId());
        }
    }

    public RichTextAppendDto appendUpdateAtHead(AppendRichTextUpdateVo vo, long expectedHeadSeq) {
        richTextSyncLockService.lock(vo.getRichTextId());
        try {
            return richTextSyncService.appendUpdateAtHead(vo, expectedHeadSeq);
        } finally {
            richTextSyncLockService.unlock(vo.getRichTextId());
        }
    }

    public RichTextStateDto seed(String richTextId, String seedBase64) {
        richTextSyncLockService.lock(richTextId);
        try {
            return richTextSyncService.seed(richTextId, seedBase64);
        } finally {
            richTextSyncLockService.unlock(richTextId);
        }
    }

    public void snapshot(String richTextId, String mergedBase64, long upToSeq) {
        richTextSyncLockService.lock(richTextId);
        try {
            richTextSyncService.snapshot(richTextId, mergedBase64, upToSeq);
        } finally {
            richTextSyncLockService.unlock(richTextId);
        }
    }
}
