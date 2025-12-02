package co.jinear.core.model.vo.captcha;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResolveVo {

    private String prefix;
    private long nonce;
}
