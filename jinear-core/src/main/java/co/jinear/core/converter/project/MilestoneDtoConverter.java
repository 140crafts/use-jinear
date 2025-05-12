package co.jinear.core.converter.project;

import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.entity.project.Milestone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RichTextConverter.class})
public interface MilestoneDtoConverter {

    MilestoneDto convert(Milestone milestone);
}
