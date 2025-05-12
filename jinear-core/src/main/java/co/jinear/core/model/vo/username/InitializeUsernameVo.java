package co.jinear.core.model.vo.username;

import co.jinear.core.model.enumtype.username.UsernameRelatedObjectType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeUsernameVo {
    private String relatedObjectId;
    private UsernameRelatedObjectType relatedObjectType;
    private String username;
    private Boolean appendRandomStrOnCollision;
}
