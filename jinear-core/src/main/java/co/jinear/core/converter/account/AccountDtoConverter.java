package co.jinear.core.converter.account;

import co.jinear.core.converter.media.MediaDtoConverter;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.entity.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MediaDtoConverter.class)
public interface AccountDtoConverter {

    @Mapping(source = "username.username", target = "username")
    @Mapping(source = "accountProfileMedia.media", target = "profilePicture")
    AccountDto map(Account account);
}
