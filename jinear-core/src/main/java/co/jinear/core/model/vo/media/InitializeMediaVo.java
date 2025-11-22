package co.jinear.core.model.vo.media;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class InitializeMediaVo extends BaseInitializeMediaVo {

    private MultipartFile file;
}
