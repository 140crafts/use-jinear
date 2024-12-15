package co.jinear.core.controller;

import co.jinear.core.model.entity.project.Project;
import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import co.jinear.core.model.vo.project.ProjectFeedSettingsInitializeVo;
import co.jinear.core.repository.project.ProjectRepository;
import co.jinear.core.service.project.ProjectFeedSettingsOperationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/v1/debug")
@RequiredArgsConstructor
public class DebugController {

    private final ProjectRepository projectRepository;
    private final ProjectFeedSettingsOperationService projectFeedSettingsOperationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) throws Exception{

    }

    @GetMapping
    public Object debug2(HttpServletResponse response) {
//
        List<Project> projects = projectRepository.findAll();
        projects.forEach(project -> {
            if (Objects.isNull(project.getProjectFeedSettings())) {
                ProjectFeedSettingsInitializeVo projectFeedSettingsInitializeVo = new ProjectFeedSettingsInitializeVo();
                projectFeedSettingsInitializeVo.setProjectId(project.getProjectId());
                projectFeedSettingsInitializeVo.setProjectTitle(project.getTitle());
                projectFeedSettingsInitializeVo.setProjectFeedAccessType(ProjectFeedAccessType.PRIVATE);
                projectFeedSettingsInitializeVo.setProjectPostInitializeAccessType(ProjectPostInitializeAccessType.PROJECT_TEAM_MEMBERS);
                projectFeedSettingsOperationService.initialize(projectFeedSettingsInitializeVo);
            }
        });
        return null;
    }
}
