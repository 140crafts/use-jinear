package co.jinear.core.model.dto.captcha;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptchaChallengeDto {

    private String[] prefixes;
    private int difficulty;
}
