package co.jinear.core.model.dto.messaging.message;

import co.jinear.core.model.dto.messaging.thread.ThreadDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RichMessageDto extends MessageDto {

    private ThreadDto thread;
}
