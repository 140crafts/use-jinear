package co.jinear.core.service.google;

import co.jinear.core.converter.google.GenerateUserConsentUrlVoToUrlConverter;
import co.jinear.core.model.enumtype.google.UserConsentPurposeType;
import co.jinear.core.model.vo.google.GenerateUserConsentUrlVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleRedirectInfoService {

    private final GenerateUserConsentUrlVoToUrlConverter generateUserConsentUrlVoToUrlConverter;

    public String retrieveLoginUrl() {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.LOGIN);
        generateUserConsentUrlVo.setIncludeEmailScopes(Boolean.FALSE);
        generateUserConsentUrlVo.setIncludeCalendarScopes(Boolean.FALSE);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }

    public String retrieveAttachMailUrl() {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.ATTACH_MAIL);
        generateUserConsentUrlVo.setIncludeEmailScopes(Boolean.TRUE);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }

    public String retrieveAttachCalendarUrl() {
        GenerateUserConsentUrlVo generateUserConsentUrlVo = new GenerateUserConsentUrlVo();
        generateUserConsentUrlVo.setUserConsentPurposeType(UserConsentPurposeType.ATTACH_CALENDAR);
        generateUserConsentUrlVo.setIncludeCalendarScopes(Boolean.TRUE);
        return generateUserConsentUrlVoToUrlConverter.convert(generateUserConsentUrlVo);
    }
}
