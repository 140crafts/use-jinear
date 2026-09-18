package co.jinear.core.model.vo.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class OauthErrorVo {

    private final String error;
    private final String errorDescription;
}
