package co.jinear.core.converter.messaging.channel;

import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.service.workspace.member.WorkspaceMemberRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType.*;

@Component
@RequiredArgsConstructor
public class WorkspaceAccountRoleTypeChannelVisibilityListMapper {

    private static final Map<WorkspaceAccountRoleType, List<ChannelVisibilityType>> WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP;

    static {
        WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP = new EnumMap<>(WorkspaceAccountRoleType.class);
        WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP.put(OWNER, List.of(ChannelVisibilityType.MEMBERS_ONLY, ChannelVisibilityType.EVERYONE));
        WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP.put(ADMIN, List.of(ChannelVisibilityType.MEMBERS_ONLY, ChannelVisibilityType.EVERYONE));
        WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP.put(MEMBER, List.of(ChannelVisibilityType.EVERYONE));
        WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP.put(GUEST, List.of(ChannelVisibilityType.PUBLIC_WITH_GUESTS));
    }

    private final WorkspaceMemberRetrieveService workspaceMemberRetrieveService;

    public List<ChannelVisibilityType> map(String workspaceId, String accountId) {
        WorkspaceAccountRoleType workspaceAccountRoleType = workspaceMemberRetrieveService.retrieveAccountWorkspaceRole(accountId, workspaceId);
        return WORKSPACE_ROLE_CHANNEL_VISIBILITY_MAP.get(workspaceAccountRoleType);
    }
}
