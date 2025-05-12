package co.jinear.core.converter.account;

import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.entity.account.AccountCommunicationPermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountCommunicationPermissionDtoConverter {

    AccountCommunicationPermissionDto convert(AccountCommunicationPermission accountCommunicationPermission);
}
