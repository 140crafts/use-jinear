package co.jinear.core.converter.google;

import co.jinear.core.model.entity.google.GoogleUserInfo;
import co.jinear.core.model.vo.google.InitializeOrUpdateTokenInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class InitializeOrUpdateTokenInfoVoToEntityConverter {

    public GoogleUserInfo map(InitializeOrUpdateTokenInfoVo initializeOrUpdateTokenInfoVo, Optional<GoogleUserInfo> googleUserInfoOptional) {
        GoogleUserInfo googleUserInfo = googleUserInfoOptional.map(entity -> {
            log.info("Google user info is present mapping new values to existing entity.");
            return entity;
        }).orElseGet(() -> {
            log.info("Initializing new google user info entity.");
            return new GoogleUserInfo();
        });

        googleUserInfo.setSub(initializeOrUpdateTokenInfoVo.getSub());
        googleUserInfo.setEmail(initializeOrUpdateTokenInfoVo.getEmail());
        googleUserInfo.setEmailVerified(initializeOrUpdateTokenInfoVo.getEmailVerified());
        googleUserInfo.setName(initializeOrUpdateTokenInfoVo.getName());
        googleUserInfo.setPicture(initializeOrUpdateTokenInfoVo.getPicture());
        googleUserInfo.setGivenName(initializeOrUpdateTokenInfoVo.getGivenName());
        googleUserInfo.setFamilyName(initializeOrUpdateTokenInfoVo.getFamilyName());
        googleUserInfo.setLocale(initializeOrUpdateTokenInfoVo.getLocale());
        return googleUserInfo;
    }
}
