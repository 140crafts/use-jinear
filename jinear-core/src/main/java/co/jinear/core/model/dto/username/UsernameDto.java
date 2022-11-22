package co.jinear.core.model.dto.username;

import co.jinear.core.model.enumtype.username.UsernameRelatedObjectType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsernameDto {
    private String username;
    private String relatedObjectId;
    private UsernameRelatedObjectType relatedObjectType;
}
