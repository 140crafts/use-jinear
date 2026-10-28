package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.controller.advice.McpApiAdvice;
import co.jinear.core.controller.mcp.McpController;
import co.jinear.core.converter.mcp.McpContextViewConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.manager.account.AccountContextManager;
import co.jinear.core.manager.mcp.McpManager;
import co.jinear.core.manager.mcp.McpProtocolManager;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.McpToolRegistry;
import co.jinear.core.manager.mcp.tool.workspace.GetContextTool;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDisplayPreferenceDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.enumtype.account.RoleType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.team.TeamStateType;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.enumtype.workspace.WorkspaceAccountRoleType;
import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaNode;
import co.jinear.core.model.mcp.view.McpContextTeamView;
import co.jinear.core.model.mcp.view.McpContextView;
import co.jinear.core.model.mcp.view.McpContextWorkflowStatusView;
import co.jinear.core.model.mcp.view.McpContextWorkspaceView;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.account.AccountRetrieveService;
import co.jinear.core.service.mcp.McpDiscoveryService;
import co.jinear.core.service.mcp.McpToolCallLogService;
import co.jinear.core.service.oauth.provider.OauthAccessTokenResolver;
import co.jinear.core.service.oauth.provider.OauthConnectionService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.topic.TopicRetrieveService;
import co.jinear.core.service.workspace.WorkspaceDisplayPreferenceService;
import co.jinear.core.service.workspace.member.WorkspaceMemberListingService;
import co.jinear.core.system.oauth.OauthTokenHelper;
import co.jinear.core.system.util.ZonedDateHelper;
import co.jinear.core.validator.mcp.McpEnabledValidator;
import co.jinear.core.validator.mcp.McpToolScopeValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class McpGetContextToolTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String ACCOUNT_ID = "account-1";

    private final SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
    private final WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
    private final AccountRetrieveService accountRetrieveService = Mockito.mock(AccountRetrieveService.class);
    private final WorkspaceDisplayPreferenceService workspaceDisplayPreferenceService = Mockito.mock(WorkspaceDisplayPreferenceService.class);
    private final WorkspaceMemberListingService workspaceMemberListingService = Mockito.mock(WorkspaceMemberListingService.class);
    private final TeamMemberRetrieveService teamMemberRetrieveService = Mockito.mock(TeamMemberRetrieveService.class);
    private final TopicRetrieveService topicRetrieveService = Mockito.mock(TopicRetrieveService.class);

    private GetContextTool tool;

    @BeforeEach
    void setUp() {
        AccountContextManager manager = new AccountContextManager(sessionInfoService, workspaceValidator, accountRetrieveService,
                workspaceDisplayPreferenceService, workspaceMemberListingService, teamMemberRetrieveService, topicRetrieveService);
        tool = new GetContextTool(manager, new McpContextViewConverter());

        Mockito.when(sessionInfoService.currentAccountId()).thenReturn(ACCOUNT_ID);
        Mockito.when(accountRetrieveService.retrieve(ACCOUNT_ID)).thenReturn(account("GMT+03:00"));
        Mockito.when(workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspaceOptional(ACCOUNT_ID))
                .thenReturn(Optional.of(preference("ws-1", "team-eng")));
        Mockito.when(workspaceMemberListingService.retrieveAccountsWorkspaceMemberships(ACCOUNT_ID)).thenReturn(List.of(
                workspaceMembership("ws-1", "acme", "Acme", WorkspaceTier.PRO, WorkspaceAccountRoleType.OWNER),
                workspaceMembership("ws-2", "side", "Side Project", WorkspaceTier.BASIC, WorkspaceAccountRoleType.MEMBER)));
        Mockito.when(teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(ACCOUNT_ID)).thenReturn(List.of(
                teamMembership("team-eng", "ws-1", "Engineering", "ENG", TeamMemberRoleType.ADMIN, TeamStateType.ACTIVE, List.of(
                        workflowStatus("done", TeamWorkflowStateGroup.COMPLETED, 0),
                        workflowStatus("review", TeamWorkflowStateGroup.STARTED, 1),
                        workflowStatus("backlog", TeamWorkflowStateGroup.BACKLOG, 0),
                        workflowStatus("todo", TeamWorkflowStateGroup.NOT_STARTED, 0),
                        workflowStatus("doing", TeamWorkflowStateGroup.STARTED, 0),
                        workflowStatus("cancelled", TeamWorkflowStateGroup.CANCELLED, 0))),
                teamMembership("team-old", "ws-1", "Old", "OLD", TeamMemberRoleType.ADMIN, TeamStateType.ARCHIVED, List.of()),
                teamMembership("team-ops", "ws-2", "Operations", "OPS", TeamMemberRoleType.MEMBER, TeamStateType.ACTIVE, List.of(
                        workflowStatus("ops-backlog", TeamWorkflowStateGroup.BACKLOG, 0)))));
        Mockito.when(workspaceMemberListingService.workspaceActiveMemberCount("ws-1")).thenReturn(3L);
        Mockito.when(workspaceMemberListingService.workspaceActiveMemberCount("ws-2")).thenReturn(1L);
        Mockito.when(topicRetrieveService.retrieveAllTeamTopics("team-eng")).thenReturn(List.of(
                topic("topic-bug", "team-eng", "Bug")));
        Mockito.when(topicRetrieveService.retrieveAllTeamTopics("team-ops")).thenReturn(List.of());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, VALIDATOR);
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId(ACCOUNT_ID).scopes(Set.of("workspace:read", "tasks:read")).build();
    }

    private McpContextView call(String json) throws Exception {
        McpToolResult result = tool.call(context(), args(json));
        assertThat(result.isError()).isFalse();
        return (McpContextView) result.getStructuredContent();
    }

    private AccountDto account(String timeZone) {
        AccountDto account = new AccountDto();
        account.setAccountId(ACCOUNT_ID);
        account.setUsername("cagdas");
        account.setLocaleType(LocaleType.EN);
        account.setTimeZone(timeZone);
        account.setEmail("private@example.com");
        return account;
    }

    private WorkspaceDisplayPreferenceDto preference(String workspaceId, String teamId) {
        WorkspaceDisplayPreferenceDto preference = new WorkspaceDisplayPreferenceDto();
        preference.setAccountId(ACCOUNT_ID);
        preference.setPreferredWorkspaceId(workspaceId);
        preference.setPreferredTeamId(teamId);
        return preference;
    }

    private DetailedWorkspaceMemberDto workspaceMembership(String workspaceId, String handle, String title,
                                                           WorkspaceTier tier, WorkspaceAccountRoleType role) {
        WorkspaceDto workspace = new WorkspaceDto();
        workspace.setWorkspaceId(workspaceId);
        workspace.setUsername(handle);
        workspace.setTitle(title);
        workspace.setTier(tier);
        DetailedWorkspaceMemberDto membership = new DetailedWorkspaceMemberDto();
        membership.setWorkspaceId(workspaceId);
        membership.setAccountId(ACCOUNT_ID);
        membership.setRole(role);
        membership.setWorkspace(workspace);
        return membership;
    }

    private TeamMemberDto teamMembership(String teamId, String workspaceId, String name, String tag, TeamMemberRoleType role,
                                         TeamStateType state, List<TeamWorkflowStatusDto> workflowStatuses) {
        TeamDto team = new TeamDto();
        team.setTeamId(teamId);
        team.setWorkspaceId(workspaceId);
        team.setName(name);
        team.setTag(tag);
        team.setTeamState(state);
        team.setWorkflowStatuses(new LinkedHashSet<>(workflowStatuses));
        TeamMemberDto membership = new TeamMemberDto();
        membership.setTeamId(teamId);
        membership.setWorkspaceId(workspaceId);
        membership.setAccountId(ACCOUNT_ID);
        membership.setRole(role);
        membership.setTeam(team);
        return membership;
    }

    private TeamWorkflowStatusDto workflowStatus(String id, TeamWorkflowStateGroup group, int order) {
        TeamWorkflowStatusDto status = new TeamWorkflowStatusDto();
        status.setTeamWorkflowStatusId(id);
        status.setName(id);
        status.setWorkflowStateGroup(group);
        status.setOrder(order);
        return status;
    }

    private WorkspaceMemberDto member(String workspaceId, String accountId, String username) {
        AccountDto account = new AccountDto();
        account.setAccountId(accountId);
        account.setUsername(username);
        account.setEmail(username + "@example.com");
        WorkspaceMemberDto member = new WorkspaceMemberDto();
        member.setWorkspaceId(workspaceId);
        member.setAccountId(accountId);
        member.setAccount(account);
        return member;
    }

    private TopicDto topic(String topicId, String teamId, String name) {
        TopicDto topic = new TopicDto();
        topic.setTopicId(topicId);
        topic.setTeamId(teamId);
        topic.setName(name);
        topic.setColor("#ff0000");
        return topic;
    }

    private List<String> statusIds(McpContextTeamView team) {
        return team.getStatuses().stream().map(McpContextWorkflowStatusView::getWorkflowStatusId).toList();
    }

    @Test
    void returnsTheAccountItsDefaultsAndEveryWorkspaceWithTeamsStatusesAndTopics() throws Exception {
        McpContextView view = call("{}");

        assertThat(view.getAccount().getAccountId()).isEqualTo(ACCOUNT_ID);
        assertThat(view.getAccount().getUsername()).isEqualTo("cagdas");
        assertThat(view.getAccount().getLocale()).isEqualTo("EN");
        assertThat(view.getAccount().getTimeZone()).isEqualTo("Etc/GMT-3");
        assertThat(view.getAccount().getNow()).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+03:00");
        assertThat(view.getDefaults().getWorkspaceId()).isEqualTo("ws-1");
        assertThat(view.getDefaults().getTeamId()).isEqualTo("team-eng");
        assertThat(view.getGrantedScopes()).containsExactly("tasks:read", "workspace:read");

        assertThat(view.getWorkspaces()).extracting(McpContextWorkspaceView::getWorkspaceId).containsExactly("ws-1", "ws-2");
        McpContextWorkspaceView acme = view.getWorkspaces().get(0);
        assertThat(acme.getUsername()).isEqualTo("acme");
        assertThat(acme.getTitle()).isEqualTo("Acme");
        assertThat(acme.getRole()).isEqualTo("OWNER");
        assertThat(acme.getTier()).isEqualTo("PRO");
        assertThat(acme.getMemberCount()).isEqualTo(3L);
        assertThat(acme.getMembers()).isNull();

        McpContextTeamView engineering = acme.getTeams().get(0);
        assertThat(engineering.getName()).isEqualTo("Engineering");
        assertThat(engineering.getTag()).isEqualTo("ENG");
        assertThat(engineering.getRole()).isEqualTo("ADMIN");
        assertThat(statusIds(engineering)).containsExactly("backlog", "todo", "doing", "review", "done", "cancelled");
        assertThat(engineering.getTopics()).extracting("topicId").containsExactly("topic-bug");

        McpContextTeamView operations = view.getWorkspaces().get(1).getTeams().get(0);
        assertThat(statusIds(operations)).containsExactly("ops-backlog");
        assertThat(operations.getTopics()).isEmpty();

        Mockito.verify(workspaceValidator, Mockito.never()).validateHasAccess(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        Mockito.verify(workspaceMemberListingService, Mockito.never()).listAllWorkspaceMembers(ArgumentMatchers.anyString());
    }

    @Test
    void serializesWithoutPrivateFieldsOrNullPadding() throws Exception {
        String json = OBJECT_MAPPER.writeValueAsString(call("{}"));

        assertThat(json).doesNotContain("private@example.com", "email", "members", "color", "order",
                "profilePicture", "createdDate", "passiveId", "settings", "ghost");
        assertThat(json).contains("\"defaults\":{\"workspaceId\":\"ws-1\",\"teamId\":\"team-eng\"}");
    }

    @Test
    void aWorkspaceIdNarrowsTheResultToThatWorkspace() throws Exception {
        McpContextView view = call("{\"workspaceId\":\"ws-2\"}");

        assertThat(view.getWorkspaces()).extracting(McpContextWorkspaceView::getWorkspaceId).containsExactly("ws-2");
        assertThat(view.getWorkspaces().get(0).getTeams()).extracting(McpContextTeamView::getTeamId).containsExactly("team-ops");
        assertThat(view.getDefaults().getWorkspaceId()).isEqualTo("ws-1");
        Mockito.verify(workspaceValidator).validateHasAccess(ACCOUNT_ID, "ws-2");
        Mockito.verify(workspaceMemberListingService, Mockito.never()).workspaceActiveMemberCount("ws-1");
        Mockito.verify(topicRetrieveService, Mockito.never()).retrieveAllTeamTopics("team-eng");
    }

    @Test
    void aWorkspaceTheAccountIsNotInIsRefusedByTheSameMembershipCheck() {
        Mockito.doThrow(new NoAccessException()).when(workspaceValidator).validateHasAccess(ACCOUNT_ID, "ws-foreign");

        assertThatThrownBy(() -> tool.call(context(), args("{\"workspaceId\":\"ws-foreign\"}")))
                .isInstanceOf(NoAccessException.class);
        Mockito.verify(workspaceMemberListingService, Mockito.never()).retrieveAccountsWorkspaceMemberships(ArgumentMatchers.anyString());
    }

    @Test
    void includeMembersListsThePeopleOfEachWorkspace() throws Exception {
        Mockito.when(workspaceMemberListingService.listAllWorkspaceMembers("ws-1")).thenReturn(List.of(
                member("ws-1", ACCOUNT_ID, "cagdas"),
                member("ws-1", "account-2", "deniz")));
        Mockito.when(workspaceMemberListingService.listAllWorkspaceMembers("ws-2")).thenReturn(List.of(
                member("ws-2", ACCOUNT_ID, "cagdas")));

        McpContextView view = call("{\"includeMembers\":true}");
        String json = OBJECT_MAPPER.writeValueAsString(view);

        assertThat(view.getWorkspaces().get(0).getMembers()).extracting("accountId").containsExactly(ACCOUNT_ID, "account-2");
        assertThat(view.getWorkspaces().get(0).getMembers()).extracting("username").containsExactly("cagdas", "deniz");
        assertThat(view.getWorkspaces().get(1).getMembers()).extracting("accountId").containsExactly(ACCOUNT_ID);
        assertThat(json).doesNotContain("@example.com");
    }

    @Test
    void anArchivedTeamIsLeftOut() throws Exception {
        McpContextView view = call("{}");

        assertThat(view.getWorkspaces().get(0).getTeams()).extracting(McpContextTeamView::getTeamId).containsExactly("team-eng");
        Mockito.verify(topicRetrieveService, Mockito.never()).retrieveAllTeamTopics("team-old");
    }

    @Test
    void anArchivedDefaultTeamIsNotReported() throws Exception {
        Mockito.when(workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspaceOptional(ACCOUNT_ID))
                .thenReturn(Optional.of(preference("ws-1", "team-old")));

        McpContextView view = call("{}");

        assertThat(view.getDefaults().getWorkspaceId()).isEqualTo("ws-1");
        assertThat(view.getDefaults().getTeamId()).isNull();
    }

    @Test
    void defaultsAreNullWhenNoPreferenceIsStored() throws Exception {
        Mockito.when(workspaceDisplayPreferenceService.retrieveAccountPreferredWorkspaceOptional(ACCOUNT_ID)).thenReturn(Optional.empty());

        String json = OBJECT_MAPPER.writeValueAsString(call("{}"));

        assertThat(json).contains("\"defaults\":{\"workspaceId\":null,\"teamId\":null}");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Europe/Istanbul, Europe/Istanbul",
            "GMT+03:00, Etc/GMT-3",
            "GMT-05:00, Etc/GMT+5",
            "GMT, UTC",
            "GMT+05:30, UTC",
            "not-a-zone, UTC",
            "NULL, UTC"}, nullValues = "NULL")
    void storedTimeZonesBecomeIanaZoneIds(String stored, String expected) {
        ZoneId zoneId = ZonedDateHelper.zoneIdOrUtc(stored);

        assertThat(ZonedDateHelper.ianaZoneIdOf(zoneId)).isEqualTo(expected);
    }

    @Test
    void nowKeepsTheRealOffsetWhenNoIanaZoneMatchesIt() throws Exception {
        Mockito.when(accountRetrieveService.retrieve(ACCOUNT_ID)).thenReturn(account("GMT+05:30"));

        McpContextView view = call("{}");

        assertThat(view.getAccount().getTimeZone()).isEqualTo("UTC");
        assertThat(view.getAccount().getNow()).endsWith("+05:30");
    }

    @Test
    void theOutputSchemaDescribesTheNestedShape() {
        McpSchemaNode schema = tool.definition().getOutputSchema();
        McpSchemaNode workspace = schema.getProperties().get("workspaces").getItems();
        McpSchemaNode team = workspace.getProperties().get("teams").getItems();

        assertThat(schema.getProperties()).containsOnlyKeys("account", "defaults", "grantedScopes", "workspaces");
        assertThat(schema.getProperties().get("grantedScopes").getItems().getType()).isEqualTo(McpSchemaNode.TYPE_STRING);
        assertThat(workspace.getProperties()).containsKeys("workspaceId", "memberCount", "members", "teams");
        assertThat(team.getProperties().get("statuses").getItems().getProperties()).containsOnlyKeys("workflowStatusId", "name", "stateGroup");
        assertThat(team.getProperties().get("topics").getItems().getProperties()).containsOnlyKeys("topicId", "name");
    }

    @Test
    void aTokenWithoutWorkspaceReadIsRefusedBeforeTheToolRuns() throws Exception {
        MockMvc mockMvc = mcpEndpointServing(tool);
        authenticateWith(Set.of("tasks:read"));

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"get_context","arguments":{}}}
                        """))
                .andExpect(status().isForbidden())
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("error=\"insufficient_scope\"")))
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("workspace:read")));
        Mockito.verify(sessionInfoService, Mockito.never()).currentAccountId();
    }

    private MockMvc mcpEndpointServing(GetContextTool getContextTool) {
        McpProperties properties = new McpProperties();
        properties.setEnabled(Boolean.TRUE);
        properties.setResourceUrl("https://api.jinear.test/mcp");
        OauthProperties oauthProperties = new OauthProperties();
        oauthProperties.setIssuerUrl("https://api.jinear.test");

        McpToolRegistry registry = new McpToolRegistry(List.of(getContextTool));
        McpToolCallLogService logService = Mockito.mock(McpToolCallLogService.class);
        McpProtocolManager protocolManager = new McpProtocolManager(registry, logService, OBJECT_MAPPER, VALIDATOR);
        McpDiscoveryService discoveryService = new McpDiscoveryService(properties, oauthProperties);
        OauthAccessTokenResolver accessTokenResolver = new OauthAccessTokenResolver(
                Mockito.mock(OauthTokenHelper.class),
                Mockito.mock(OauthConnectionService.class));
        McpToolScopeValidator scopeValidator = new McpToolScopeValidator(logService, discoveryService);
        McpManager manager = new McpManager(protocolManager, scopeValidator, registry, accessTokenResolver,
                new McpEnabledValidator(properties));
        return MockMvcBuilders.standaloneSetup(new McpController(manager))
                .setControllerAdvice(new McpApiAdvice())
                .build();
    }

    private void authenticateWith(Set<String> scopes) {
        OauthAccessTokenVo vo = new OauthAccessTokenVo();
        vo.setAccountId(ACCOUNT_ID);
        vo.setConnectionId("connection-1");
        vo.setClientId("https://claude.ai/client.json");
        vo.setScopes(scopes);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                ACCOUNT_ID, null, List.of(new SimpleGrantedAuthority(RoleType.USER.getAuthority())));
        authentication.setDetails(vo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
