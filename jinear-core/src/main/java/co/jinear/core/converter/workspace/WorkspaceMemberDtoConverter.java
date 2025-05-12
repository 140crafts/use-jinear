package co.jinear.core.converter.workspace;

import co.jinear.core.converter.account.AccountDtoConverter;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {WorkspaceDtoConverter.class, AccountDtoConverter.class})
public interface WorkspaceMemberDtoConverter {

    WorkspaceMemberDto map(WorkspaceMember workspaceMember);
}
