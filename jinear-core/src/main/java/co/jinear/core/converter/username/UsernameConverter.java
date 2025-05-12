package co.jinear.core.converter.username;

import co.jinear.core.model.dto.username.UsernameDto;
import co.jinear.core.model.entity.username.Username;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsernameConverter {
    UsernameDto map(Username username);
}
