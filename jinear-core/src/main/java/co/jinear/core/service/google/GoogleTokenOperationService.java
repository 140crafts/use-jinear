package co.jinear.core.service.google;

import co.jinear.core.converter.google.GoogleTokenToDtoConverter;
import co.jinear.core.converter.google.InitializeOrUpdateGoogleAuthTokenVoToEntityConverter;
import co.jinear.core.model.dto.google.GoogleTokenDto;
import co.jinear.core.model.entity.google.GoogleToken;
import co.jinear.core.model.vo.google.InitializeOrUpdateGoogleAuthTokenVo;
import co.jinear.core.repository.GoogleTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenOperationService {

    private final GoogleTokenRepository googleTokenRepository;
    private final InitializeOrUpdateGoogleAuthTokenVoToEntityConverter initializeOrUpdateGoogleAuthTokenVoToEntityConverter;
    private final GoogleTokenToDtoConverter googleTokenToDtoConverter;
    private final GoogleTokenRetrieveService googleTokenRetrieveService;

    public GoogleTokenDto initializeOrUpdateGoogleToken(InitializeOrUpdateGoogleAuthTokenVo initializeOrUpdateGoogleAuthTokenVo) {
        log.info("Initialize or update google token has started.");
        Optional<GoogleToken> optionalGoogleToken = googleTokenRetrieveService.retrieveEntityOptional(initializeOrUpdateGoogleAuthTokenVo.getGoogleUserInfoId());
        GoogleToken googleToken = initializeOrUpdateGoogleAuthTokenVoToEntityConverter.map(initializeOrUpdateGoogleAuthTokenVo, optionalGoogleToken);
        GoogleToken saved = googleTokenRepository.save(googleToken);
        log.info("Initialize or update google token has completed.");
        return googleTokenToDtoConverter.convert(saved);
    }

}
