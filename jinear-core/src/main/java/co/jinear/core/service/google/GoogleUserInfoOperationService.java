package co.jinear.core.service.google;

import co.jinear.core.converter.google.GoogleUserInfoToDtoConverter;
import co.jinear.core.converter.google.InitializeOrUpdateTokenInfoVoToEntityConverter;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.entity.google.GoogleUserInfo;
import co.jinear.core.model.vo.google.InitializeOrUpdateTokenInfoVo;
import co.jinear.core.repository.GoogleUserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleUserInfoOperationService {

    private final GoogleUserInfoRepository googleUserInfoRepository;
    private final GoogleUserInfoRetrieveService googleUserInfoRetrieveService;
    private final InitializeOrUpdateTokenInfoVoToEntityConverter initializeOrUpdateTokenInfoVoToEntityConverter;
    private final GoogleUserInfoToDtoConverter googleUserInfoToDtoConverter;

    public GoogleUserInfoDto initializeOrUpdateGoogleUserInfo(InitializeOrUpdateTokenInfoVo initializeOrUpdateTokenInfoVo) {
        log.info("Initialize or update google user info has started.");
        Optional<GoogleUserInfo> googleUserInfoOptional = googleUserInfoRetrieveService.retrieveEntity(initializeOrUpdateTokenInfoVo.getSub(), initializeOrUpdateTokenInfoVo.getEmail());
        GoogleUserInfo googleUserInfo = initializeOrUpdateTokenInfoVoToEntityConverter.map(initializeOrUpdateTokenInfoVo, googleUserInfoOptional);
        GoogleUserInfo saved = googleUserInfoRepository.save(googleUserInfo);
        return googleUserInfoToDtoConverter.convert(saved);
    }
}
