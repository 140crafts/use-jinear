package co.jinear.core.model.dto.token;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.token.TokenType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class TokenDto extends BaseDto {
    private String tokenId;
    private String relatedObject;
    private TokenType tokenType;
    private String uniqueToken;
    @ToString.Exclude
    private String commonToken;
    private String additionalData;
    private Long expiresAt;
}