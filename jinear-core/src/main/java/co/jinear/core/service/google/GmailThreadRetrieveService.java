package co.jinear.core.service.google;

import co.jinear.core.converter.google.GmailThreadDtoConverter;
import co.jinear.core.model.dto.google.GmailThreadDto;
import co.jinear.core.model.entity.google.GmailThread;
import co.jinear.core.repository.GmailThreadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailThreadRetrieveService {

    private final Integer PAGE_SIZE = 100;
    private final GmailThreadRepository gmailThreadRepository;
    private final GmailThreadDtoConverter gmailThreadDtoConverter;

    public Page<GmailThreadDto> listThreads(String googleTokenId, int page) {
        log.info("Listing threads with googleTokenId: {}", googleTokenId);
        return gmailThreadRepository.findAllByGoogleTokenIdAndPassiveIdIsNullOrderByLastUpdatedDateDesc(googleTokenId, PageRequest.of(page, PAGE_SIZE))
                .map(gmailThreadDtoConverter::convert);
    }

    public List<GmailThread> retrieveEntitiesWithGId(List<String> googleTokenIdList) {
        return gmailThreadRepository.findAllByGIdInAndPassiveIdIsNull(googleTokenIdList);
    }
}
