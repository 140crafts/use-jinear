package co.jinear.core.converter.account;

import co.jinear.core.converter.media.MediaDtoConverter;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.username.Username;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MediaDtoConverter.class)
public interface PlainAccountProfileDtoConverter {

    @Mapping(source = "accountProfileMedia.media", target = "profilePicture")
    @Mapping(source = "username.username", target = "username")
    PlainAccountProfileDto map(Account account);

    PlainAccountProfileDto map(AccountDto accountDto);

    String map(Username value);
}
