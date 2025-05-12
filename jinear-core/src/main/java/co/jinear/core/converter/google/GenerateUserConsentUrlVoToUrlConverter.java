package co.jinear.core.converter.google;

import co.jinear.core.config.properties.GCloudProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.google.GoogleScopeType;
import co.jinear.core.model.vo.google.GenerateUserConsentUrlVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static co.jinear.core.system.NormalizeHelper.SPACE_STRING;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateUserConsentUrlVoToUrlConverter {

    private static final String OAUTH_CONSENT_URL = "https://accounts.google.com/o/oauth2/auth";

    private final GCloudProperties gCloudProperties;

    public String convert(GenerateUserConsentUrlVo generateUserConsentUrlVo) {
        String redirectUrl = getRedirectUrl(generateUserConsentUrlVo);
        String scope = getScopes(generateUserConsentUrlVo);
        URIBuilder uriBuilder = getUriBuilder();
        addParameters(generateUserConsentUrlVo, redirectUrl, scope, uriBuilder);
        return getUrl(uriBuilder);
    }

    private static String getUrl(URIBuilder uriBuilder) {
        try {
            URL url = uriBuilder.build().toURL();
            return url.toString();
        } catch (MalformedURLException | URISyntaxException e) {
            log.error("Get url failed.", e);
            throw new BusinessException();
        }
    }

    private URIBuilder getUriBuilder() {
        try {
            return new URIBuilder(OAUTH_CONSENT_URL);
        } catch (URISyntaxException e) {
            log.error("Uri builder failed.", e);
            throw new BusinessException();
        }
    }

    private void addParameters(GenerateUserConsentUrlVo generateUserConsentUrlVo, String redirectUrl, String scope, URIBuilder uriBuilder) {
        uriBuilder.addParameter("client_id", gCloudProperties.getOauthClientId());
        uriBuilder.addParameter("redirect_uri", redirectUrl);
        uriBuilder.addParameter("scope", scope);
        uriBuilder.addParameter("response_type", generateUserConsentUrlVo.getResponseType());
        uriBuilder.addParameter("access_type", generateUserConsentUrlVo.getAccessType().getValue());
        uriBuilder.addParameter("include_granted_scopes", generateUserConsentUrlVo.getIncludeGrantedScopes());
        uriBuilder.addParameter("state", generateUserConsentUrlVo.getState());
    }

    private String getScopes(GenerateUserConsentUrlVo generateUserConsentUrlVo) {
        List<String> basicScopes = GoogleScopeType.getBasicScopes();
        List<String> scopes = new ArrayList<>(basicScopes);
        if (Boolean.TRUE.equals(generateUserConsentUrlVo.getIncludeCalendarScopes())) {
            List<String> calendarScopes = GoogleScopeType.getCalendarScopes();
            scopes.addAll(calendarScopes);
        }
        if (Boolean.TRUE.equals(generateUserConsentUrlVo.getIncludeEmailScopes())) {
            List<String> mailScopes = GoogleScopeType.getMailScopes();
            scopes.addAll(mailScopes);
        }
        return StringUtils.join(scopes, SPACE_STRING);
    }

    private String getRedirectUrl(GenerateUserConsentUrlVo generateUserConsentUrlVo) {
        return switch (generateUserConsentUrlVo.getUserConsentPurposeType()) {
            case LOGIN -> gCloudProperties.getLoginRedirectUrl();
            case ATTACH_MAIL -> gCloudProperties.getAttachMailRedirectUrl();
            case ATTACH_CALENDAR -> gCloudProperties.getAttachCalendarRedirectUrl();
        };
    }
}
