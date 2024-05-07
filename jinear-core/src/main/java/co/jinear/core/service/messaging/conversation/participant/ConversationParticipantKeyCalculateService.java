package co.jinear.core.service.messaging.conversation.participant;

import co.jinear.core.model.dto.messaging.conversation.PlainConversationParticipantDto;
import co.jinear.core.system.NormalizeHelper;
import com.google.common.hash.Hashing;
import de.huxhorn.sulky.ulid.ULID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationParticipantKeyCalculateService {

    private final ConversationParticipantListingService conversationParticipantListingService;

    public String retrieveAndCalculate(String conversationId) {
        log.info("Retrieve and calculate has started. conversationId: {}", conversationId);
        List<String> accountIds = conversationParticipantListingService.retrieveActiveParticipants(conversationId).stream()
                .map(PlainConversationParticipantDto::getAccountId)
                .toList();
        return calculate(accountIds);
    }

    public String calculate(List<String> accountIds) {
        log.info("Calculate has started. accountIds: {}", NormalizeHelper.listToString(accountIds));
        if (Objects.isNull(accountIds)) {
            throw new UnsupportedOperationException();
        }
        StringBuilder sb = new StringBuilder();
        accountIds.stream()
                .map(ULID::parseULID)
                .sorted()
                .map(ULID.Value::toString)
                .forEach(sb::append);
        return hash(sb.toString());
    }

    private String hash(String input) {
        return Hashing.sha512().hashString(input, StandardCharsets.UTF_8).toString();
    }
}
