package co.jinear.core.model.vo.token;

import co.jinear.core.model.enumtype.token.TokenType;
import lombok.*;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GenerateTokenVo {
    private String relatedObject;
    private TokenType tokenType;
    private String uniqueToken;
    private String commonToken;
    private String additionalData;
    private Long ttl;
}
