package co.jinear.core.converter.task;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.task.TaskCollaboratorDto;
import co.jinear.core.model.entity.task.TaskCollaborator;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                PlainAccountProfileDtoConverter.class
        })
public interface TaskCollaboratorDtoConverter {

    TaskCollaboratorDto convert(TaskCollaborator taskCollaborator);
}
