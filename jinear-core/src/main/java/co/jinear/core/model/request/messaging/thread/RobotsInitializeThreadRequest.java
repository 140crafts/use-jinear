package co.jinear.core.model.request.messaging.thread;

import co.jinear.core.model.request.BaseRequest;
import co.jinear.core.model.vo.captcha.CaptchaResolveVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RobotsInitializeThreadRequest extends BaseRequest {

    @NotBlank
    private String initialMessageBody;

    @NotNull
    private List<CaptchaResolveVo> captchaResolveVos;
}
