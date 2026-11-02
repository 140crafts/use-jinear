package co.jinear.core.converter.richtext;

import co.jinear.core.model.dto.richtext.RichTextDocumentDto;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.entity.richtext.RichTextUpdate;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class RichTextDocumentConverter {

    public RichTextDocumentDto convert(RichText richText, List<RichTextUpdate> updates) {
        RichTextDocumentDto dto = new RichTextDocumentDto();
        dto.setFormat(richText.getFormat());
        dto.setHeadSeq(richText.getHeadSeq());
        dto.setValue(richText.getValue());
        dto.setStates(mapStates(richText, updates));
        return dto;
    }

    private List<String> mapStates(RichText richText, List<RichTextUpdate> updates) {
        Stream<byte[]> snapshot = Optional.ofNullable(richText.getYjsState()).stream();
        Stream<byte[]> log = updates.stream().map(RichTextUpdate::getUpdateBytes);
        return Stream.concat(snapshot, log).map(Base64.getEncoder()::encodeToString).toList();
    }
}
