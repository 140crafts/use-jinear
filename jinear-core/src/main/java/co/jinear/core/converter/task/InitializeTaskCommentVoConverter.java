package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.InitializeTaskCommentRequest;
import co.jinear.core.model.vo.task.InitializeTaskCommentVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitializeTaskCommentVoConverter {

    InitializeTaskCommentVo convert(InitializeTaskCommentRequest initializeTaskCommentRequest, String ownerId);
}
