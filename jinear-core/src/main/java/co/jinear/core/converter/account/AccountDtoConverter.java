package co.jinear.core.converter.account;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.entity.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountDtoConverter {

    @Mapping(source = "username.username", target = "username")
    AccountDto map(Account account);
}
