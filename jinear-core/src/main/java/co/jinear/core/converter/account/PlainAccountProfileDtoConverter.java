package co.jinear.core.converter.account;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.entity.account.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlainAccountProfileDtoConverter {

    @Mapping(source = "username.username", target = "username")
    PlainAccountProfileDto map(Account account);

    PlainAccountProfileDto map(AccountDto accountDto);
}
