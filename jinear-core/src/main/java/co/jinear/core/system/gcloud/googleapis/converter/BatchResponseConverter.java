package co.jinear.core.system.gcloud.googleapis.converter;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class BatchResponseConverter {

    private static final String JSON_START = "{";
    private static final String JSON_END = "}";
    private static final String SUCCESSFUL_PART_INDICATOR = "200 OK";

    private final Gson gson;

    public List<GmailMessageVo> mapToMessagesResponse(ResponseEntity<String> response) {
        String responseBoundary = extractResponseBoundary(response);
        return Optional.of(response)
                .map(HttpEntity::getBody)
                .map(responseBody -> parseMessagesResponseBody(responseBody, responseBoundary))
                .orElseThrow(BusinessException::new);
    }

    public List<GmailThreadVo> mapToThreadsResponse(ResponseEntity<String> response) {
        String responseBoundary = extractResponseBoundary(response);
        return Optional.of(response)
                .map(HttpEntity::getBody)
                .map(responseBody -> parseThreadsResponseBody(responseBody, responseBoundary))
                .orElseThrow(BusinessException::new);
    }

    private String extractResponseBoundary(ResponseEntity<String> response) {
        return Optional.of(response)
                .map(HttpEntity::getHeaders)
                .map(httpHeaders -> httpHeaders.getFirst(HttpHeaders.CONTENT_TYPE))
                .map(val -> val.split(Pattern.quote("boundary=")))
                .map(Arrays::stream)
                .map(stringStream -> stringStream.reduce((first, second) -> second))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(BusinessException::new);
    }

    private List<GmailMessageVo> parseMessagesResponseBody(String resultBody, String boundary) {
        String[] chunks = resultBody.split(Pattern.quote("--%s".formatted(boundary)));
        return Arrays.stream(chunks)
                .filter(chunk -> chunk.contains(SUCCESSFUL_PART_INDICATOR))
                .map(this::parseMessage)
                .toList();
    }

    private List<GmailThreadVo> parseThreadsResponseBody(String resultBody, String boundary) {
        String[] chunks = resultBody.split(Pattern.quote("--%s".formatted(boundary)));
        return Arrays.stream(chunks)
                .filter(chunk -> chunk.contains(SUCCESSFUL_PART_INDICATOR))
                .map(this::parseThread)
                .toList();
    }

    private GmailMessageVo parseMessage(String raw) {
        String jsonPart = raw.substring(raw.indexOf(JSON_START), raw.lastIndexOf(JSON_END) + 1);
        return gson.fromJson(jsonPart, GmailMessageVo.class);
    }

    private GmailThreadVo parseThread(String raw) {
        String jsonPart = raw.substring(raw.indexOf(JSON_START), raw.lastIndexOf(JSON_END) + 1);
        return gson.fromJson(jsonPart, GmailThreadVo.class);
    }
}
