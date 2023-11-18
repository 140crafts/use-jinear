package co.jinear.core.service.google;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.google.GoogleToken;
import co.jinear.core.repository.GoogleTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleTokenRetrieveService {

    private final GoogleTokenRepository googleTokenRepository;

    public Optional<GoogleToken> retrieveEntityOptional(String googleUserInfoId) {
        log.info("Retrieve optional google token entity with user info id has started. googleUserInfoId: {}", googleUserInfoId);
        return googleTokenRepository.findByGoogleUserInfoIdAndPassiveIdIsNull(googleUserInfoId);
    }

    public GoogleToken retrieveEntity(String googleUserInfoId) {
        log.info("Retrieve google token entity with user info id has started. googleUserInfoId: {}", googleUserInfoId);
        return googleTokenRepository.findByGoogleUserInfoIdAndPassiveIdIsNull(googleUserInfoId)
                .orElseThrow(NotFoundException::new);
    }
}
