package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.entity.task.ChecklistItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChecklistItemDtoConverter {

    ChecklistItemDto convert(ChecklistItem checklistItem);
}
