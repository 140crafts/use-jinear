package co.jinear.core.service.richtext.crdt;

import co.jinear.core.exception.note.NoteCrdtException;
import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.note.NoteCrdtResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteCrdtEngine {

    private static final String READ = "read";
    private static final String EDIT = "edit";

    private final NoteCrdtContextPool noteCrdtContextPool;
    private final ObjectMapper objectMapper;

    public NoteContentDto read(List<String> states) {
        String serializedStates = serialize(states);
        String result = noteCrdtContextPool.execute(READ, serializedStates);
        return deserialize(result, NoteContentDto.class);
    }

    public NoteCrdtResultDto edit(List<String> states, String html, String title) {
        String serializedStates = serialize(states);
        String result = noteCrdtContextPool.execute(EDIT, serializedStates, html, title);
        return deserialize(result, NoteCrdtResultDto.class);
    }

    private String serialize(List<String> values) {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException exception) {
            throw new NoteCrdtException("note.error.document.serialize-failed");
        }
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return objectMapper.readerFor(type)
                    .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(json);
        } catch (JsonProcessingException exception) {
            throw new NoteCrdtException("note.error.document.read-failed");
        }
    }
}
