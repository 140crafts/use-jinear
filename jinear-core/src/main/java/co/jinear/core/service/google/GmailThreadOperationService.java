package co.jinear.core.service.google;

import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.entity.google.GmailMessage;
import co.jinear.core.model.entity.google.GmailThread;
import co.jinear.core.repository.GmailThreadRepository;
import co.jinear.core.system.gcloud.gmail.model.GmailThreadInfo;
import co.jinear.core.system.gcloud.gmail.model.response.GmailListThreadsResponse;
import co.jinear.core.system.gcloud.googleapis.GoogleApisClient;
import co.jinear.core.system.gcloud.googleapis.model.GmailMessageVo;
import co.jinear.core.system.gcloud.googleapis.model.GmailThreadVo;
import co.jinear.core.system.gcloud.googleapis.model.RetrieveBatchRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailThreadOperationService {

    private final GmailApiCallerService gmailApiCallerService;
    private final GoogleApisClient googleApisClient;
    private final GmailThreadRepository gmailThreadRepository;
    private final GmailThreadRetrieveService gmailThreadRetrieveService;
    private final GmailMessageOperationService gmailMessageOperationService;

    public void fetchAndSaveLatestThreads(GoogleTokenDto googleTokenDto) {
        String googleUserId = googleTokenDto.getGoogleUserInfo().getSub();
        log.info("Fetch and save latest threads has started. googleTokenId: {}, googleUserId: {}", googleTokenDto.getGoogleTokenId(), googleUserId);
        List<GmailThreadInfo> fetchedThreads = fetchedThreads(googleTokenDto, googleUserId);
        Map<String, GmailThread> existingThreadsMap = retrieveExistingThreadsWithGId(fetchedThreads);
        List<GmailThreadInfo> toFetchDetails = checkNeedForThreadFetch(fetchedThreads, existingThreadsMap);
        List<RetrieveBatchRequestVo> retrieveBatchRequestVos = mapToBatchRequestVo(googleUserId, toFetchDetails);
        List<GmailThreadVo> gmailThreadVos = googleApisClient.retrieveBatchThreads(googleTokenDto.getAccessToken(), retrieveBatchRequestVos);
        saveOrUpdateResults(existingThreadsMap, googleTokenDto, gmailThreadVos);
    }

    private void saveOrUpdateResults(Map<String, GmailThread> existingThreadsMap, GoogleTokenDto googleTokenDto, List<GmailThreadVo> gmailThreadVos) {
        gmailThreadVos.forEach(gmailThreadVo -> saveOrUpdateResult(gmailThreadVo, googleTokenDto, existingThreadsMap));
    }

    private void saveOrUpdateResult(GmailThreadVo gmailThreadVo, GoogleTokenDto googleTokenDto, Map<String, GmailThread> existingThreadsMap) {
        String gThreadId = gmailThreadVo.getId();
        GmailThread existingGmailThread = existingThreadsMap.get(gThreadId);
        if (Objects.isNull(existingGmailThread)) {
            log.info("Initializing thread and messages.");
            GmailThread savedThread = initializeThread(gmailThreadVo, googleTokenDto);
            initializeThreadMessages(savedThread, gmailThreadVo);
            return;
        }
        log.info("Updating existing thread and messages.");
        updateExistingThread(existingGmailThread, gmailThreadVo);
        checkAndInitializeNewMessages(existingGmailThread, gmailThreadVo);
    }

    private GmailThread initializeThread(GmailThreadVo gmailThreadVo, GoogleTokenDto googleTokenDto) {
        GmailThread gmailThread = new GmailThread();
        gmailThread.setGoogleTokenId(googleTokenDto.getGoogleTokenId());
        gmailThread.setGId(gmailThreadVo.getId());
        gmailThread.setGHistoryId(gmailThreadVo.getHistoryId());
        gmailThread.setSnippet(gmailThreadVo.getSnippet());
        return gmailThreadRepository.save(gmailThread);
    }

    private void initializeThreadMessages(GmailThread savedThread, GmailThreadVo gmailThreadVo) {
        gmailMessageOperationService.initializeGmailMessages(savedThread.getGmailThreadId(), gmailThreadVo.getMessages());
    }

    private void updateExistingThread(GmailThread existingGmailThread, GmailThreadVo gmailThreadVo) {
        String historyId = gmailThreadVo.getHistoryId();
        log.info("Update thread has stared. gmailThreadId: {}, new historyId: {}", existingGmailThread.getGmailThreadId(), historyId);
        existingGmailThread.setGHistoryId(historyId);
        existingGmailThread.setSnippet(gmailThreadVo.getSnippet());
        gmailThreadRepository.save(existingGmailThread);
    }

    private void checkAndInitializeNewMessages(GmailThread existingGmailThread, GmailThreadVo gmailThreadVo) {
        Map<String, GmailMessage> map = Optional.of(existingGmailThread)
                .map(GmailThread::getMessages)
                .map(gmailMessages -> gmailMessages.stream().collect(Collectors.toMap(GmailMessage::getGId, Function.identity())))
                .orElseGet(Collections::emptyMap);

        List<GmailMessageVo> messages = gmailThreadVo.getMessages();
        List<GmailMessageVo> newMessages = messages.stream()
                .filter(gmailMessageVo -> !map.containsKey(gmailMessageVo.getId()))
                .toList();

        gmailMessageOperationService.initializeGmailMessages(existingGmailThread.getGmailThreadId(), newMessages);
    }

    private List<GmailThreadInfo> checkNeedForThreadFetch(List<GmailThreadInfo> fetchedThreads, Map<String, GmailThread> existingThreadsMap) {
        List<GmailThreadInfo> toFetchDetails = new ArrayList<>();

        fetchedThreads.forEach(gmailThreadInfo -> {
            String threadInfoGId = gmailThreadInfo.getId();
            String threadInfoHistoryId = gmailThreadInfo.getHistoryId();
            if (!existingThreadsMap.containsKey(threadInfoGId)) {
                log.info("Thread not saved before. Adding to fetch list.");
                toFetchDetails.add(gmailThreadInfo);
            } else {
                GmailThread existingGmailThread = existingThreadsMap.get(threadInfoGId);
                if (!threadInfoHistoryId.equals(existingGmailThread.getGHistoryId())) {
                    log.info("Thread exists but history id different. Adding to fetch list.");
                    toFetchDetails.add(gmailThreadInfo);
                }
            }
        });
        return toFetchDetails;
    }

    private Map<String, GmailThread> retrieveExistingThreadsWithGId(List<GmailThreadInfo> threads) {
        if (Objects.isNull(threads)) {
            return Collections.emptyMap();
        }
        List<String> googleTokenIdList = threads
                .stream()
                .map(GmailThreadInfo::getId)
                .toList();
        return gmailThreadRetrieveService.retrieveEntitiesWithGId(googleTokenIdList)
                .stream()
                .collect(Collectors.toMap(GmailThread::getGId, Function.identity()));
    }

    private List<GmailThreadInfo> fetchedThreads(GoogleTokenDto googleTokenDto, String googleUserId) {
        GmailListThreadsResponse gmailListThreadsResponse = gmailApiCallerService.listThreads(googleUserId, googleTokenDto.getAccessToken());
        return gmailListThreadsResponse.getThreads();
    }

    private List<RetrieveBatchRequestVo> mapToBatchRequestVo(String googleUserId, List<GmailThreadInfo> toFetchDetails) {
        return toFetchDetails.stream()
                .map(gmailThreadInfo -> new RetrieveBatchRequestVo(googleUserId, gmailThreadInfo.getId()))
                .toList();
    }
}
