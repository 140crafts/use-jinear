package co.jinear.core.repository;

import co.jinear.core.model.entity.token.Token;
import co.jinear.core.model.enumtype.token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

    List<Token> findAllByRelatedObjectAndTokenTypeAndExpiresAtGreaterThanAndPassiveId(String relatedObject, TokenType tokenType, Long expiresAtGreaterThan, String passiveId);

    Optional<Token> findFirstByRelatedObjectAndTokenTypeAndExpiresAtGreaterThanAndPassiveIdIsNullOrderByCreatedDateDesc(String relatedObject, TokenType tokenType, Long expiresAtGreaterThan);

    Optional<Token> findByUniqueTokenAndTokenTypeAndExpiresAtGreaterThanAndPassiveIdIsNull(String uniqueToken, TokenType tokenType, Long expiresAtGreaterThan);

    Optional<Token> findByUniqueTokenAndTokenType(String uniqueToken, TokenType tokenType);
}
