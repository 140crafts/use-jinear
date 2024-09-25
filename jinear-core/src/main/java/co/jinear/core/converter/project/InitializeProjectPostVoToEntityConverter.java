package co.jinear.core.converter.project;

import co.jinear.core.model.entity.project.ProjectPost;
import co.jinear.core.model.vo.project.InitializeProjectPostVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeProjectPostVoToEntityConverter {

    ProjectPost convert(InitializeProjectPostVo initializeProjectPostVo);
}
