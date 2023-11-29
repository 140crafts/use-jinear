package co.jinear.core.controller;

import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.service.google.GoogleOAuthApiCallerService;
import co.jinear.core.service.google.GoogleRedirectInfoService;
import co.jinear.core.service.google.GoogleTokenValidatedRetrieveService;
import co.jinear.core.system.gcloud.gmail.GmailApiClient;
import co.jinear.core.system.gcloud.googleapis.RestGoogleApisClient;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final GoogleOAuthApiCallerService googleOAuthApiCallerService;
    private final GoogleTokenValidatedRetrieveService googleTokenValidatedRetrieveService;
    private final RestGoogleApisClient restGoogleApisClient;
    private final GmailApiClient gmailApiClient;
    private final GoogleRedirectInfoService googleRedirectInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {


    }

    @GetMapping
    public String debug2(HttpServletResponse response) throws Exception {
//        return googleRedirectInfoService.retrieveAttachMailUrl("01hfhyn839211scjc8hfv0es0t");


        GoogleTokenDto token = googleTokenValidatedRetrieveService.retrieveValidatedToken("01hfhxh7yhyr9b7213dzmdcwm8");
        String accessToken = token.getAccessToken();
//        RetrieveBatchMessageVo retrieveBatchMessageVo = new RetrieveBatchMessageVo();
//        retrieveBatchMessageVo.setUserId("108296555126733890191");
//        retrieveBatchMessageVo.setMessageId("18be89a31ba4db97");
//
//        RetrieveBatchMessageVo retrieveBatchMessageVo2 = new RetrieveBatchMessageVo();
//        retrieveBatchMessageVo2.setUserId("108296555126733890191");
//        retrieveBatchMessageVo2.setMessageId("18be897706b1b3c2");
//
//        List<GoogleBatchResponse> responses = restGoogleApisClient.retrieveBatchMessages(token, List.of(retrieveBatchMessageVo, retrieveBatchMessageVo2));

//        String userId = token.getGoogleUserInfo().getSub();
//        GmailListThreadsResponse gmailListThreadsResponse = gmailApiClient.listThreads(userId, accessToken);
//        List<RetrieveBatchRequestVo> messageVoList = gmailListThreadsResponse.getThreads()
//                .stream()
//                .map(gmailThreadInfo -> new RetrieveBatchRequestVo(userId, gmailThreadInfo.getId()))
//                .toList();
//        List<GmailThreadVo> threads = restGoogleApisClient.retrieveBatchThreads(accessToken, messageVoList);

//        gmailThreadOperationService.fetchAndSaveLatestThreads(token);
//        List<GmailThread> threadList = gmailThreadRepository.findAll();
//        System.out.println(threadList);
        return accessToken;
//        return googleRedirectInfoService.retrieveAttachMailUrl("01hfhyn839211scjc8hfv0es0t");
    }

    public static void main(String[] args) {
    }
}
