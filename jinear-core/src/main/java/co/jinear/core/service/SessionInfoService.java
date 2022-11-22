package co.jinear.core.service;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.entity.SessionInfo;
import co.jinear.core.model.enumtype.account.RoleType;
import co.jinear.core.model.enumtype.auth.ProviderType;
import co.jinear.core.repository.SessionInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionInfoService {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ANONYMOUS_USER = "anonymousUser";
    private final SessionInfoRepository sessionInfoRepository;

    public String currentAccountId() {
        AbstractAuthenticationToken auth = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(auth)
                .map(AbstractAuthenticationToken::getPrincipal)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .filter(accountId -> !ANONYMOUS_USER.equalsIgnoreCase(accountId))
                .orElseThrow(BusinessException::new);
    }

    public String currentAccountIdInclAnonymous() {
        AbstractAuthenticationToken auth = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(auth)
                .map(AbstractAuthenticationToken::getPrincipal)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseThrow(BusinessException::new);
    }

    public String initialize(ProviderType provider, String accountId) {
        log.info("New session, provider: {} account: {}", provider, accountId);
        return saveSessionInfo(provider, accountId);
    }

    public void validateOwnership(String ownerId) {
        String currentAccountId = currentAccountId();
        if (!currentAccountId.equalsIgnoreCase(ownerId)) {
            log.warn("Validate ownership failed, current account: {} owner: {}", currentAccountId, ownerId);
            throw new NoAccessException();
        }
    }

    public void validateOwnership(String... ownerIdList) {
        String currentAccountId = currentAccountId();
        boolean exists = Arrays.stream(ownerIdList)
                .anyMatch(currentAccountId::equalsIgnoreCase);
        if (!exists) {
            log.warn("Validate ownership failed, current account: {} owner: {}", currentAccountId, ownerIdList);
            throw new NoAccessException();
        }
    }

    public void updateCurrentAccountRoles(List<GrantedAuthority> authorities) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public Set<String> retrieveCurrentAccountRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.contains(ROLE_PREFIX))
                .map(authority -> authority.split(ROLE_PREFIX)[1])
                .collect(Collectors.toSet());
    }

    public boolean accountHasRole(RoleType roleType) {
        return retrieveCurrentAccountRoles().stream().anyMatch(role -> roleType.name().equalsIgnoreCase(role));
    }

    private String saveSessionInfo(ProviderType provider, String accountId) {
        log.info("New session info save has started. provider: {}, accountId: {}", provider, accountId);
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setProvider(provider);
        sessionInfo.setAccountId(accountId);
        sessionInfo.setSessionId(sessionId);
        SessionInfo saved = sessionInfoRepository.save(sessionInfo);
        log.info("New session info saved. sessionInfoId: {}", saved.getSessionInfoId());
        return saved.getSessionInfoId();
    }
}
