package co.jinear.core.service.google;

import co.jinear.core.converter.google.GoogleTokenScopeDtoConverter;
import co.jinear.core.converter.google.InitializeGoogleTokenScopeVoToEntityConverter;
import co.jinear.core.model.dto.google.GoogleTokenScopeDto;
import co.jinear.core.model.entity.google.GoogleTokenScope;
import co.jinear.core.model.vo.google.InitializeGoogleTokenScopeVo;
import co.jinear.core.repository.GoogleTokenScopeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenScopeOperationService {

    private final GoogleTokenScopeRepository googleTokenScopeRepository;
    private final InitializeGoogleTokenScopeVoToEntityConverter initializeGoogleTokenScopeVoToEntityConverter;
    private final GoogleTokenScopeDtoConverter googleTokenScopeDtoConverter;

    public GoogleTokenScopeDto initialize(InitializeGoogleTokenScopeVo initializeGoogleTokenScopeVo) {
        log.info("Initialize google token scope has started. initializeGoogleTokenScopeVo: {}", initializeGoogleTokenScopeVo);
        GoogleTokenScope googleTokenScope = initializeGoogleTokenScopeVoToEntityConverter.convert(initializeGoogleTokenScopeVo);
        GoogleTokenScope saved = googleTokenScopeRepository.save(googleTokenScope);
        return googleTokenScopeDtoConverter.convert(saved);
    }

    public List<GoogleTokenScopeDto> initializeAll(List<InitializeGoogleTokenScopeVo> initializeGoogleTokenScopeVos) {
        log.info("Initialize google token scope has started.");
        List<GoogleTokenScope> googleTokenScopes = initializeGoogleTokenScopeVos
                .stream()
                .map(initializeGoogleTokenScopeVoToEntityConverter::convert)
                .toList();
        return googleTokenScopeRepository.saveAll(googleTokenScopes)
                .stream()
                .map(googleTokenScopeDtoConverter::convert)
                .toList();
    }

    @Transactional
    public void removeAllWithGoogleTokenId(String googleTokenId, String passiveId) {
        log.info("Remove all with google token id has started. googleTokenId: {}, passiveId: {}", googleTokenId, passiveId);
        googleTokenScopeRepository.updateAllAsPassiveWithGoogleTokenId(googleTokenId, passiveId);
    }
}
