package co.jinear.core.service.google;

import co.jinear.core.model.entity.google.GoogleUserInfo;
import co.jinear.core.repository.GoogleUserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleUserInfoRetrieveService {

    private final GoogleUserInfoRepository googleUserInfoRepository;

    public Optional<GoogleUserInfo> retrieveEntity(String sub, String email) {
        return googleUserInfoRepository.findBySubAndEmailAndPassiveIdIsNull(sub, email);
    }
}
