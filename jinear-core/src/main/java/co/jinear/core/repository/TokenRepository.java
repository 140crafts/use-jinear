package co.jinear.core.repository;

import co.jinear.core.model.entity.token.Token;
import co.jinear.core.model.enumtype.token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Token token
                set token.expiresAt = token.expiresAt - :penaltyMillis
                    where
                        token.tokenId = :tokenId and
                        token.expiresAt > :now and
                        token.passiveId is null
                """)
    int chargeAttempt(@Param("tokenId") String tokenId,
                      @Param("penaltyMillis") Long penaltyMillis,
                      @Param("now") Long now);

    List<Token> findAllByRelatedObjectAndTokenTypeAndExpiresAtGreaterThanAndPassiveId(String relatedObject, TokenType tokenType, Long expiresAtGreaterThan, String passiveId);

    Optional<Token> findFirstByRelatedObjectAndTokenTypeAndExpiresAtGreaterThanAndPassiveIdIsNullOrderByCreatedDateDesc(String relatedObject, TokenType tokenType, Long expiresAtGreaterThan);

    Optional<Token> findByUniqueTokenAndTokenTypeAndExpiresAtGreaterThanAndPassiveIdIsNull(String uniqueToken, TokenType tokenType, Long expiresAtGreaterThan);

    Optional<Token> findByUniqueTokenAndTokenType(String uniqueToken, TokenType tokenType);
}
