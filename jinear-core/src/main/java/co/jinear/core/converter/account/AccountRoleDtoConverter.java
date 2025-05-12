package co.jinear.core.converter.account;

import co.jinear.core.model.dto.account.AccountRoleDto;
import co.jinear.core.model.entity.account.AccountRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountRoleDtoConverter {

    AccountRoleDto map(AccountRole accountRole);
}
