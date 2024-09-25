package co.jinear.core.model.vo.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class InitializeProjectPostVo {

    private String projectId;
    private String accountId;
    private String body;
    private List<MultipartFile> files;
}
