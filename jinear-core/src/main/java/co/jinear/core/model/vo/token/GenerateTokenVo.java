package co.jinear.core.model.vo.token;

import co.jinear.core.model.enumtype.token.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GenerateTokenVo {
    private String relatedObject;
    private TokenType tokenType;
    private String uniqueToken;
    private String commonToken;
    private String additionalData;
    private Long ttl;
}
