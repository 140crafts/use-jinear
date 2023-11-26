package co.jinear.core.converter.google;

import co.jinear.core.model.dto.google.GmailThreadDto;
import co.jinear.core.model.entity.google.GmailThread;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GmailThreadDtoConverter {

    GmailThreadDto convert(GmailThread gmailThread);
}
