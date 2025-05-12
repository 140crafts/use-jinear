package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.entity.task.Checklist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChecklistDtoConverter {

    ChecklistDto convert(Checklist checklist);
}
