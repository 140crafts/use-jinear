package co.jinear.core.converter.workspace;

import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.request.workspace.WorkspaceMemberInvitationRespondRequest;
import co.jinear.core.model.vo.workspace.WorkspaceInvitationRespondVo;
import co.jinear.core.service.account.AccountRetrieveService;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface WorkspaceInvitationRespondVoConverter {

    @Mapping(source = "request.locale", target = "preferredLocale")
    WorkspaceInvitationRespondVo convert(WorkspaceMemberInvitationRespondRequest request, Optional<String> optionalCurrentAccountId, @Context AccountRetrieveService accountRetrieveService);

    @AfterMapping
    default void afterMap(@MappingTarget WorkspaceInvitationRespondVo vo, Optional<String> optionalCurrentAccountId, @Context AccountRetrieveService accountRetrieveService) {
        optionalCurrentAccountId.map(accountRetrieveService::retrieve)
                .map(AccountDto::getEmail)
                .ifPresent(vo::setCurrentAccountEmail);
    }
}
