package co.jinear.core.service.google;

import co.jinear.core.converter.google.GmailMessageEntityMapper;
import co.jinear.core.model.entity.google.GmailMessage;
import co.jinear.core.repository.GmailMessageRepository;
import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailMessageOperationService {

    private final GmailMessageRepository gmailMessageRepository;
    private final GmailMessageEntityMapper gmailMessageEntityMapper;

    public void initializeGmailMessages(String gmailThreadId, List<GmailMessageVo> messages) {
        List<GmailMessage> messageList = messages.stream()
                .map(gmailMessageVo -> gmailMessageEntityMapper.mapToEntity(gmailThreadId, gmailMessageVo))
                .toList();
        gmailMessageRepository.saveAll(messageList);
    }
}
