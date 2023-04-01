package co.jinear.core.service.token;

import co.jinear.core.converter.token.TokenConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.token.TokenDto;
import co.jinear.core.model.entity.token.Token;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.enumtype.token.TokenType;
import co.jinear.core.model.vo.passive.CreatePassiveVo;
import co.jinear.core.model.vo.token.GenerateTokenVo;
import co.jinear.core.repository.TokenRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final PassiveService passiveService;
    private final TokenConverter tokenConverter;

    public TokenDto generateToken(GenerateTokenVo generateTokenVo) {
        Token token = tokenConverter.map(generateTokenVo);
        token = saveToken(generateTokenVo, token);
        return tokenConverter.map(token);
    }

    public TokenDto retrieveValidToken(String uniqueToken, TokenType tokenType) {
        Long expiresAtGreaterThan = DateHelper.now().getTime();
        log.info("Get valid token has started. uniqueToken: {}, tokenType: {}, expiresAtGreaterThan: {}", uniqueToken, tokenType, expiresAtGreaterThan);
        Token token = tokenRepository.findByUniqueTokenAndTokenTypeAndExpiresAtGreaterThanAndPassiveIdIsNull(uniqueToken, tokenType, expiresAtGreaterThan)
                .orElseThrow(NotFoundException::new);
        log.info("Token found. tokenId: {}", token.getTokenId());
        return tokenConverter.map(token);
    }

    public Optional<TokenDto> retrieveValidTokenWithRelatedObject(String relatedObject, TokenType tokenType) {
        Long expiresAtGreaterThan = DateHelper.now().getTime();
        return tokenRepository.findFirstByRelatedObjectAndTokenTypeAndExpiresAtGreaterThanAndPassiveIdIsNullOrderByCreatedDateDesc(relatedObject, tokenType, expiresAtGreaterThan)
                .map(tokenConverter::map);
    }

    public TokenDto retrieveToken(String uniqueToken, TokenType tokenType) {
        log.info("Retrieve token has started. uniqueToken: {}, tokenType: {}", uniqueToken, tokenType);
        return tokenRepository.findByUniqueTokenAndTokenType(uniqueToken, tokenType)
                .map(tokenConverter::map)
                .orElseThrow(NotFoundException::new);

    }

    public void passivizeToken(String tokenId, String accountId, PassiveReason passiveReason) {
        log.info("Passive token has started for tokenId: {}, accountId: {}, passiveReason: {}", tokenId, accountId, passiveReason);
        Token token = tokenRepository.findById(tokenId).orElseThrow(NotFoundException::new);
        generateAndSetPassiveId(token, passiveReason, accountId);
        tokenRepository.save(token);
    }

    public void passivizeToken(String tokenId, String existingPassiveId) {
        log.info("Passive token has started for tokenId: {}, existingPassiveId: {}", tokenId, existingPassiveId);
        Token token = tokenRepository.findById(tokenId).orElseThrow(NotFoundException::new);
        token.setPassiveId(existingPassiveId);
        tokenRepository.save(token);
    }

    public void shortenTokenLifespan(TokenDto tokenDto) {
        log.info("Shortening token lifespan tokenId: {}", tokenDto.getTokenId());
        shortenTokenLifespan(tokenDto, (long) (10 * 60 * 1000));
    }

    public void shortenTokenLifespan(TokenDto tokenDto, Long subtractMillis) {
        Long expiresAt = tokenDto.getExpiresAt();
        updateExpiresAt(tokenDto.getTokenId(), Long.sum(expiresAt, -subtractMillis));
    }

    private void updateExpiresAt(String tokenId, Long newExpiresAt) {
        Token token = tokenRepository.findById(tokenId).orElseThrow(NotFoundException::new);
        token.setExpiresAt(newExpiresAt);
        tokenRepository.save(token);
        log.info("Token expires at updated tokenId: {}, new expiresAt: {}", token.getTokenId(), newExpiresAt);
    }

    private void generateAndSetPassiveId(Token token, PassiveReason passiveReason, String accountId) {
        String passiveId = passiveService.createPassive(
                CreatePassiveVo
                        .builder()
                        .reasonType(passiveReason)
                        .responsibleAccountId(accountId)
                        .build());
        token.setPassiveId(passiveId);
    }

    private Token saveToken(GenerateTokenVo generateTokenVo, Token token) {
        setExpiresAt(generateTokenVo, token);
        token = tokenRepository.save(token);
        return token;
    }

    private void setExpiresAt(GenerateTokenVo generateTokenVo, Token token) {
        Optional.ofNullable(generateTokenVo.getTtl())
                .map(ttl -> Long.sum(ttl, DateHelper.now().getTime()))
                .ifPresent(token::setExpiresAt);
    }

}
