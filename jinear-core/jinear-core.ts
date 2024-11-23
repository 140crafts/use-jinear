/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.0.1157 on 2024-11-23 12:57:23.

export interface BaseDto {
    createdDate: Date;
    lastUpdatedDate: Date;
    passiveId: string;
}

export interface PageDto<T> {
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
    numberOfElements: number;
    content: T[];
    hasContent: boolean;
    hasNext: boolean;
    hasPrevious: boolean;
    last: boolean;
    first: boolean;
}

export interface AccountCommunicationPermissionDto extends BaseDto {
    accountId: string;
    email: boolean;
    pushNotification: boolean;
}

export interface AccountDeleteEligibilityDto {
    workspacesWithActiveMembers: DetailedWorkspaceMemberDto[];
    eligible: boolean;
}

export interface AccountDto extends BaseDto {
    accountId: string;
    email: string;
    emailConfirmed: boolean;
    localeType: LocaleType;
    timeZone: string;
    ghost: boolean;
    username?: string | null;
    roles: AccountRoleDto[];
    profilePicture?: AccessibleMediaDto | null;
    workspaces: AccountsWorkspacePerspectiveDto[];
    workspaceDisplayPreference?: WorkspaceDisplayPreferenceDto | null;
}

export interface AccountRoleDto {
    role: RoleType;
}

export interface PlainAccountDto extends BaseDto {
    accountId: string;
    username: string;
}

export interface PlainAccountProfileDto extends BaseDto {
    accountId: string;
    email: string;
    username: string;
    localeType: LocaleType;
    timeZone: string;
    profilePicture?: AccessibleMediaDto | null;
}

export interface InMemoryCacheItem {
    item: any;
    expiresAt: Date;
    expired: boolean;
    notExpired: boolean;
}

export interface CalendarDto {
    calendarId: string;
    workspaceId: string;
    initializedBy: string;
    integrationInfoId: string;
    integrationInfo: IntegrationInfoDto;
    name: string;
    provider: IntegrationProvider;
    scopes: IntegrationScopeType[];
    calendarSources?: ExternalCalendarSourceDto[] | null;
}

export interface CalendarEventDto {
    workspaceId: string;
    calendarId: string;
    calendarEventId: string;
    title: string;
    assignedDate: Date;
    dueDate: Date;
    hasPreciseAssignedDate: boolean;
    hasPreciseDueDate: boolean;
    calendarEventSourceType: CalendarEventSourceType;
    description?: RichTextDto | null;
    location?: string | null;
    externalLink?: string | null;
    externalCalendarSourceDto?: ExternalCalendarSourceDto | null;
    relatedTask?: TaskDto | null;
    relatedGoogleCalendarEventInfo?: GoogleCalendarEventInfo | null;
}

export interface CalendarMemberDto {
    calendarMemberId: string;
    accountId: string;
    workspaceId: string;
    calendarId: string;
    account: AccountDto;
    calendar: CalendarDto;
}

export interface CalendarShareKeyDto extends BaseDto {
    calendarShareKeyId: string;
    accountId: string;
    workspaceId: string;
    shareableKey: string;
}

export interface ExternalCalendarSourceDto {
    externalCalendarSourceId: string;
    summary?: string | null;
    description?: string | null;
    location?: string | null;
    timeZone?: string | null;
    readOnly?: boolean | null;
}

export interface TaskExternalCalendarFilterDto {
    calendarId: string;
    calendarSourceId: string;
}

export interface FeedDto extends BaseDto {
    feedId: string;
    workspaceId: string;
    initializedBy: string;
    integrationInfoId: string;
    name: string;
    provider: IntegrationProvider;
    scopes: IntegrationScopeType[];
}

export interface FeedMemberDto extends BaseDto {
    feedMemberId: string;
    accountId: string;
    workspaceId: string;
    feedId: string;
    account: AccountDto;
    feed: FeedDto;
}

export interface GmailMessageDto extends BaseDto {
    gmailMessageId: string;
    gmailThreadId: string;
    snippet: string;
    from: string;
    to: string;
    subject: string;
    body: string;
    gid: string;
    ginternalDate: string;
    ghistoryId: string;
    gthreadId: string;
}

export interface GoogleHandleTokenDto {
    googleUserInfoDto: GoogleUserInfoDto;
    passiveIdForScopeDeletion: string;
}

export interface GoogleTokenDto extends BaseDto {
    googleTokenId: string;
    expiresAt: Date;
    accessToken: string;
    refreshToken: string;
    idToken: string;
    tokenType: string;
    googleUserInfoId: string;
    lastMailCheck: Date;
    scopes: GoogleTokenScopeDto[];
    googleUserInfo: GoogleUserInfoDto;
}

export interface GoogleTokenScopeDto extends BaseDto {
    googleTokenScopeId: string;
    googleTokenId: string;
    scope: GoogleScopeType;
}

export interface GoogleUserInfoDto extends BaseDto {
    googleUserInfoId: string;
    sub: string;
    email: string;
    emailVerified: string;
    name: string;
    picture: string;
    givenName: string;
    familyName: string;
    locale: string;
}

export interface DetailedFeedContentItemDto extends FeedContentItemDto {
}

export interface FeedContentDto {
    feedItemList: FeedContentItemDto[];
    nextPageToken: string;
    feed: FeedDto;
}

export interface FeedContentItemDto {
    participants?: FeedItemParticipant[] | null;
    title?: string | null;
    text?: string | null;
    externalId?: string | null;
    messages?: FeedItemMessage[] | null;
    date?: Date | null;
    feed: FeedDto;
}

export interface FeedItemMessage {
    externalId?: string | null;
    externalGroupId?: string | null;
    from?: FeedItemParticipant | null;
    to?: FeedItemParticipant | null;
    subject?: string | null;
    body?: string | null;
    date?: Date | null;
    detailDataList?: FeedItemMessageData[] | null;
}

export interface FeedItemMessageData {
    mimeType?: string | null;
    data?: string | null;
    parts?: FeedItemMessageData[] | null;
}

export interface FeedItemParticipant {
    name: string;
    address: string;
}

export interface IntegrationInfoDto extends BaseDto {
    integrationInfoId: string;
    provider: IntegrationProvider;
    accountId: string;
    relatedObjectId: string;
    scopes: IntegrationScopeType[];
    googleUserInfo: GoogleUserInfoDto;
}

export interface IntegrationScopeDto extends BaseDto {
    integrationScopeId: string;
    integrationInfoId: string;
    scope: IntegrationScopeType;
    integrationInfo: IntegrationInfoDto;
}

export interface AccessibleMediaDto extends MediaDto {
    mediaKey: string;
    storagePath: string;
}

export interface MediaDto extends BaseDto {
    mediaId: string;
    ownerId: string;
    relatedObjectId: string;
    mediaOwnerType: MediaOwnerType;
    fileType: FileType;
    bucketName: string;
    originalName: string;
    size: number;
    contentType?: string | null;
    providerType: MediaFileProviderType;
    url: string;
}

export interface ChannelDto extends PlainChannelDto {
    settings: ChannelSettingsDto[];
    members: ChannelMemberDto[];
}

export interface ChannelInfoDto {
    channelId: string;
    lastChannelActivity?: Date | null;
}

export interface ChannelMemberDto extends BaseDto {
    channelMemberId: string;
    channelId: string;
    accountId: string;
    roleType: ChannelMemberRoleType;
    silentUntil?: Date | null;
    lastCheck: Date;
    robotId?: string | null;
    account?: PlainAccountProfileDto | null;
    robot?: RobotDto | null;
    channel: PlainChannelDto;
}

export interface ChannelMembershipInfoDto {
    channel: PlainChannelDto;
    isJoined: boolean;
}

export interface ChannelSettingsDto extends BaseDto {
    channelSettingsId: string;
    channelId: string;
    settingsType: ChannelSettingType;
    settingsValue: string;
}

export interface InitialChannelSettingDto {
    settingsType: ChannelSettingType;
    settingsValue: string;
}

export interface PlainChannelDto extends BaseDto {
    channelId: string;
    workspaceId: string;
    title: string;
    participationType: ChannelParticipationType;
    channelVisibilityType: ChannelVisibilityType;
    channelInfo: ChannelInfoDto;
}

export interface ConversationDto {
    conversationId: string;
    workspaceId: string;
    lastActivityTime: Date;
    conversationMessageInfo: ConversationMessageInfoDto;
    participants: PlainConversationParticipantDto[];
}

export interface ConversationMessageInfoDto {
    conversationId: string;
    lastMessageId: string;
    lastMessage: MessageDto;
    initialMessage: MessageDto;
}

export interface ConversationParticipantDto extends PlainConversationParticipantDto {
    conversation: ConversationDto;
}

export interface ConversationParticipantInfoDto {
    conversationParticipantId: string;
    conversationId: string;
    unreadCount: number;
}

export interface PlainConversationParticipantDto extends BaseDto {
    conversationParticipantId: string;
    conversationId: string;
    accountId: string;
    lastCheck: Date;
    leftAt: Date;
    silentUntil: Date;
    account: PlainAccountProfileDto;
    conversationParticipantInfo: ConversationParticipantInfoDto;
}

export interface MessageDataDto extends BaseDto {
    messageDataId: string;
    messageId: string;
    dataKey: string;
    dataValue: string;
}

export interface MessageDto extends BaseDto {
    messageId: string;
    accountId: string;
    robotId: string;
    richTextId: string;
    messageType: MessageType;
    threadId?: string | null;
    conversationId?: string | null;
    account: PlainAccountProfileDto;
    robot: RobotDto;
    richText?: RichTextDto | null;
    messageData?: MessageDataDto[] | null;
    messageReactions?: MessageReactionDto[] | null;
}

export interface MessageReactionDto extends BaseDto {
    messageReactionId: string;
    messageId: string;
    accountId: string;
    reactionType: MessageReactionType;
    unicode: string;
    account: PlainAccountProfileDto;
}

export interface RichMessageDto extends MessageDto {
    thread: ThreadDto;
    conversation: ConversationDto;
}

export interface PlainThreadDto extends BaseDto {
    threadId: string;
    threadType: ThreadType;
    ownerId: string;
    channelId: string;
    lastActivityTime: Date;
}

export interface ThreadDto extends PlainThreadDto {
    threadMessageInfo: ThreadMessageInfoDto;
    account?: PlainAccountProfileDto | null;
    robot?: RobotDto | null;
    channel: PlainChannelDto;
}

export interface ThreadMessageInfoDto {
    threadId: string;
    initialMessageId: string;
    latestMessageId: string;
    messageCount: number;
    initialMessage: MessageDto;
    latestMessage: MessageDto;
}

export interface MessagingTokenDto {
    token: string;
}

export interface NotificationEventDto extends BaseDto {
    notificationEventId: string;
    accountId: string;
    isRead: boolean;
    eventState: NotificationEventState;
    notificationType: NotificationType;
    localeType: LocaleType;
    title: string;
    text: string;
    launchUrl: string;
}

export interface NotificationEventParamDto extends BaseDto {
    notificationEventParamId: string;
    notificationEventId: string;
    paramKey: string;
    paramValue: string;
}

export interface NotificationMessageExternalDataDto {
    workspaceId: string;
    teamId: string;
    taskId: string;
    taskTag: string;
    notificationType: NotificationType;
    senderSessionId: string;
}

export interface NotificationTargetDto extends BaseDto {
    externalTargetId: string;
    accountId: string;
    sessionInfoId: string;
    targetType: NotificationTargetType;
    providerType: NotificationProviderType;
}

export interface SubscriptionDto extends BaseDto {
    subscriptionId: string;
    paymentsServiceSubscriptionId: string;
    subscriptionStatus: SubscriptionStatus;
    workspaceId: string;
    accountId: string;
}

export interface SubscriptionEditDto extends BaseDto {
    cancelUrl: string;
    updateUrl: string;
}

export interface SubscriptionExternalDto extends BaseDto {
    subscriptionId: string;
    paymentsServiceSubscriptionId: string;
    subscriptionStatus: SubscriptionStatus;
    passthroughDetails: PassthroughDetailDto[];
}

export interface SubscriptionInfoDto extends BaseDto {
    cancelsAfter: Date;
    retrieveSubscriptionEditInfo: SubscriptionEditDto;
    subscriptionPaymentInfoList: SubscriptionPaymentInfoDto[];
}

export interface SubscriptionPaymentInfoDto extends BaseDto {
    relatedEntityId: string;
    balanceCurrency: string;
    balanceGross: string;
    saleGross: string;
    unitPrice: string;
    currency: string;
    receiptUrl: string;
    parsedEventTime: Date;
    parsedNextBillDate: Date;
}

export interface AccountProjectPermissionFlags {
    canInitializePost: boolean;
    canComment: boolean;
    accountWorkspaceAdminOrOwner: boolean;
    accountIsProjectTeamsMember: boolean;
    accountIsProjectTeamsAdmin: boolean;
}

export interface AccountProjectPermissionFlagsBuilder {
}

export interface MilestoneDto extends BaseDto {
    milestoneId: string;
    projectId: string;
    title: string;
    descriptionRichTextId: string;
    milestoneOrder: number;
    targetDate: Date;
    description: RichTextDto;
    milestoneState: MilestoneStateType;
}

export interface MilestoneInitializeDto {
    title: string;
    targetDate?: Date | null;
}

export interface ProjectDto extends BaseDto {
    projectId: string;
    workspaceId: string;
    title: string;
    descriptionRichTextId: string;
    projectState: ProjectStateType;
    projectPriority: ProjectPriorityType;
    leadWorkspaceMemberId: string;
    startDate: Date;
    targetDate: Date;
    archived: boolean;
    leadWorkspaceMember: WorkspaceMemberDto;
    description: RichTextDto;
    projectTeams: ProjectTeamDto[];
    milestones: MilestoneDto[];
    workspace: WorkspaceDto;
    projectFeedSettings: ProjectFeedSettingsDto;
}

export interface ProjectFeedSettingsDto extends BaseDto {
    projectFeedSettingsId: string;
    projectId: string;
    projectFeedAccessType: ProjectFeedAccessType;
    projectPostInitializeAccessType: ProjectPostInitializeAccessType;
    projectPostCommentPolicyType: ProjectPostCommentPolicyType;
    infoRichTextId: string;
    infoWebsiteUrl: string;
    accessKey: string;
    info: RichTextDto;
}

export interface ProjectPostCommentDto extends BaseDto {
    projectPostCommentId: string;
    projectPostId: string;
    accountId: string;
    commentBodyRichTextId: string;
    account: PlainAccountProfileDto;
    commentBody: RichTextDto;
    quote: ProjectPostCommentDto;
}

export interface ProjectPostDto extends BaseDto {
    projectPostId: string;
    projectId: string;
    accountId: string;
    feedAccessKey: string;
    account: PlainAccountProfileDto;
    postBody: RichTextDto;
    files: AccessibleMediaDto[];
    commentCount: number;
}

export interface ProjectTeamDto extends BaseDto {
    projectTeamId: string;
    projectId: string;
    teamId: string;
    team: TeamDto;
}

export interface PublicProjectDto extends BaseDto {
    projectId: string;
    workspaceId: string;
    workspaceUsername: string;
    title: string;
    archived: boolean;
    info?: RichTextDto | null;
    accountProjectPermissionFlags: AccountProjectPermissionFlags;
}

export interface ReminderDto extends BaseDto {
    reminderId: string;
    ownerId: string;
    relatedObjectId: string;
    type: ReminderType;
    repeatType: RepeatType;
    repeatStart: Date;
    repeatEnd: Date;
}

export interface ReminderJobDto extends BaseDto {
    reminderJobId: string;
    reminderId: string;
    date: Date;
    reminderJobStatus: ReminderJobStatus;
    reminder: ReminderDto;
}

export interface RichTextDto {
    richTextId: string;
    relatedObjectId: string;
    value: string;
    type: RichTextType;
    sourceStack: RichTextSourceStack;
}

export interface RobotDto extends BaseDto {
    robotId: string;
    workspaceId: string;
    robotName: string;
    robotType: RobotType;
}

export interface RobotSecretDto extends RobotDto {
    tokenClearText: string;
}

export interface ChecklistDto extends BaseDto {
    checklistId: string;
    taskId: string;
    ownerId: string;
    title: string;
    checklistItems: ChecklistItemDto[];
}

export interface ChecklistItemDto extends BaseDto {
    checklistItemId: string;
    checklistId: string;
    label: string;
    isChecked: boolean;
}

export interface CommentDto extends BaseDto {
    commentId: string;
    taskId: string;
    ownerId: string;
    richTextId: string;
    owner: PlainAccountProfileDto;
    richText: RichTextDto;
    quote: CommentDto;
}

export interface DetailedTaskSubscriptionDto extends BaseDto {
    taskSubscriptionId: string;
    taskId: string;
    accountId: string;
    accountDto: AccountDto;
}

export interface RelatedTaskDto extends BaseDto {
    taskId: string;
    topicId: string;
    workspaceId: string;
    teamId: string;
    ownerId: string;
    workflowStatusId: string;
    assignedTo: string;
    assignedDate: Date;
    dueDate: Date;
    hasPreciseAssignedDate: boolean;
    hasPreciseDueDate: boolean;
    teamTagNo: number;
    topicTagNo: number;
    title: string;
    projectId?: string | null;
    milestoneId?: string | null;
    topic?: TopicDto | null;
    owner?: PlainAccountProfileDto | null;
    assignedToAccount?: PlainAccountProfileDto | null;
    workspace?: WorkspaceDto | null;
    team?: TeamDto | null;
    workflowStatus: TeamWorkflowStatusDto;
}

export interface TaskAnalyticNumbersDto {
    missedDeadlineCount: number;
    deadlineComingUpCount: number;
    totalOpenTaskCount: number;
    totalClosedTaskCount: number;
    totalTaskCount: number;
    statusCounts: { [P in TeamWorkflowStateGroup]?: number };
}

export interface TaskAndTaskBoardRelationDto extends BaseDto {
    alreadyAddedBoards: TaskBoardEntryDetailedDto[];
    recentBoards: PageDto<TaskBoardDto>;
}

export interface TaskBoardDetailedDto extends TaskBoardDto {
    taskListEntries: TaskBoardEntryDto[];
}

export interface TaskBoardDto extends BaseDto {
    taskBoardId: string;
    workspaceId: string;
    teamId: string;
    ownerId: string;
    title: string;
    dueDate: Date;
    state: TaskBoardStateType;
}

export interface TaskBoardEntryDetailedDto extends TaskBoardEntryDto {
    taskBoard: TaskBoardDto;
}

export interface TaskBoardEntryDto extends BaseDto {
    taskBoardEntryId: string;
    taskBoardId: string;
    taskId: string;
    order: number;
    task: TaskDto;
}

export interface TaskDto extends BaseDto {
    taskId: string;
    topicId: string;
    workspaceId: string;
    teamId: string;
    ownerId: string;
    workflowStatusId: string;
    assignedTo: string;
    assignedDate: Date;
    dueDate: Date;
    hasPreciseAssignedDate: boolean;
    hasPreciseDueDate: boolean;
    teamTagNo: number;
    topicTagNo: number;
    title: string;
    projectId?: string | null;
    milestoneId?: string | null;
    description?: RichTextDto | null;
    topic?: TopicDto | null;
    owner?: PlainAccountProfileDto | null;
    assignedToAccount?: PlainAccountProfileDto | null;
    workspace?: WorkspaceDto | null;
    team?: TeamDto | null;
    workflowStatus: TeamWorkflowStatusDto;
    relations?: TaskRelationDto[] | null;
    relatedIn?: TaskRelationDto[] | null;
    taskReminders?: TaskReminderDto[] | null;
    checklists?: ChecklistDto[] | null;
    project?: ProjectDto | null;
    milestone?: MilestoneDto | null;
}

export interface TaskFeedItemDto extends BaseDto {
    taskFeedItemId: string;
    taskId: string;
    feedId: string;
    feedItemId: string;
    feed: FeedDto;
}

export interface TaskFeedItemListDto {
    feedContentItemList?: FeedContentItemDto[] | null;
    totalItemCount?: number | null;
}

export interface TaskMediaDto extends BaseDto {
    task: TaskDto;
    media: MediaDto;
}

export interface TaskRelationDto {
    taskRelationId: string;
    taskId?: string | null;
    relatedTaskId?: string | null;
    relationType: TaskRelationType;
    task?: RelatedTaskDto | null;
    relatedTask?: RelatedTaskDto | null;
}

export interface TaskReminderDto extends BaseDto {
    taskReminderId: string;
    taskId: string;
    reminderId: string;
    taskReminderType: TaskReminderType;
    reminder: ReminderDto;
}

export interface TaskSearchResultDto extends BaseDto {
    taskId: string;
    topicId: string;
    workspaceId: string;
    teamId: string;
    ownerId: string;
    workflowStatusId: string;
    assignedTo: string;
    assignedDate: Date;
    dueDate: Date;
    hasPreciseAssignedDate: boolean;
    hasPreciseDueDate: boolean;
    teamTagNo: number;
    topicTagNo: number;
    title: string;
    teamTag: string;
    workflowStateName: string;
    workflowStateGroup: TeamWorkflowStateGroup;
}

export interface TaskSearchResultDtoBuilder {
}

export interface TaskSubscriptionDto extends BaseDto {
    taskSubscriptionId: string;
    taskId: string;
    accountId: string;
    plainAccountProfileDto: PlainAccountProfileDto;
}

export interface TaskSubscriptionWithCommunicationPreferencesDto {
    accountId: string;
    email: string;
    localeType: LocaleType;
    timeZone: string;
    hasEmailPermission: boolean;
    hasPushNotificationPermission: boolean;
}

export interface UpdateTaskWorkflowDto {
    remindersPassiveId: string;
    taskDto: TaskDto;
}

export interface TeamDto extends BaseDto {
    teamId: string;
    workspaceId: string;
    workspaceUsername: string;
    name: string;
    username: string;
    tag: string;
    visibility: TeamVisibilityType;
    joinMethod: TeamJoinMethodType;
    taskVisibility: TeamTaskVisibilityType;
    teamState: TeamStateType;
    workflowStatuses: TeamWorkflowStatusDto[];
}

export interface TeamMemberDto extends BaseDto {
    teamMemberId: string;
    accountId: string;
    workspaceId: string;
    teamId: string;
    role: TeamMemberRoleType;
    team: TeamDto;
    account: AccountDto;
}

export interface GroupedTeamWorkflowStatusListDto {
    groupedTeamWorkflowStatuses: { [P in TeamWorkflowStateGroup]?: TeamWorkflowStatusDto[] };
}

export interface TeamWorkflowStatusDto {
    teamWorkflowStatusId: string;
    teamId: string;
    workspaceId: string;
    workflowStateGroup: TeamWorkflowStateGroup;
    name: string;
    order: number;
    editable: boolean;
    removable: boolean;
}

export interface TokenDto extends BaseDto {
    tokenId: string;
    relatedObject: string;
    tokenType: TokenType;
    uniqueToken: string;
    commonToken: string;
    additionalData: string;
    expiresAt: number;
}

export interface TopicDto extends BaseDto {
    topicId: string;
    workspaceId: string;
    teamId: string;
    ownerId: string;
    color: string;
    name: string;
    tag: string;
    visibility: TopicVisibility;
}

export interface UsernameDto {
    username: string;
    relatedObjectId: string;
    relatedObjectType: UsernameRelatedObjectType;
}

export interface AccountsWorkspacePerspectiveDto extends WorkspaceDto {
    role: WorkspaceAccountRoleType;
}

export interface DetailedWorkspaceMemberDto extends WorkspaceMemberDto {
    workspace: WorkspaceDto;
}

export interface WorkspaceActivityDto extends BaseDto {
    workspaceActivityId: string;
    workspaceId: string;
    teamId: string;
    taskId: string;
    type: WorkspaceActivityType;
    performedBy: string;
    relatedObjectId: string;
    groupId?: string | null;
    groupTitle?: string | null;
    groupLink?: string | null;
    oldState?: string | null;
    newState?: string | null;
    performedByAccount: PlainAccountProfileDto;
    workspaceDto?: WorkspaceDto | null;
    teamDto?: TeamDto | null;
    relatedAccount?: PlainAccountProfileDto | null;
    oldDescription?: RichTextDto | null;
    newDescription?: RichTextDto | null;
    oldWorkflowStatusDto?: TeamWorkflowStatusDto | null;
    newWorkflowStatusDto?: TeamWorkflowStatusDto | null;
    oldTopicDto?: TopicDto | null;
    newTopicDto?: TopicDto | null;
    oldAssignedToAccount?: PlainAccountProfileDto | null;
    newAssignedToAccount?: PlainAccountProfileDto | null;
    oldTaskRelationDto?: TaskRelationDto | null;
    newTaskRelationDto?: TaskRelationDto | null;
    relatedChecklist?: ChecklistDto | null;
    relatedChecklistItem?: ChecklistItemDto | null;
    relatedTask?: TaskDto | null;
    taskBoard?: TaskBoardDto | null;
    relatedTaskMedia?: MediaDto | null;
    oldProject?: ProjectDto | null;
    newProject?: ProjectDto | null;
    oldMilestoneDto?: MilestoneDto | null;
    newMilestoneDto?: MilestoneDto | null;
}

export interface WorkspaceDisplayPreferenceDto {
    accountId: string;
    preferredWorkspaceId?: string | null;
    preferredTeamId?: string | null;
    workspace?: WorkspaceDto | null;
    team?: TeamDto | null;
    workspaceRole?: WorkspaceAccountRoleType | null;
    teamRole?: TeamMemberRoleType | null;
}

export interface WorkspaceDto extends BaseDto {
    workspaceId: string;
    title: string;
    description: string;
    tier: WorkspaceTier;
    username: string;
    settings: WorkspaceSettingDto;
    profilePicture: AccessibleMediaDto;
}

export interface WorkspaceInvitationDto extends BaseDto {
    workspaceInvitationId: string;
    workspaceId: string;
    invitedBy: string;
    accountId: string;
    email: string;
    status: WorkspaceInvitationStatusType;
}

export interface WorkspaceInvitationInfoDto extends BaseDto {
    accountDto: PlainAccountProfileDto;
    workspaceDto: WorkspaceDto;
    invitationDto: WorkspaceInvitationDto;
}

export interface WorkspaceMemberDto extends BaseDto {
    workspaceMemberId: string;
    workspaceId: string;
    accountId: string;
    role: WorkspaceAccountRoleType;
    account: AccountDto;
}

export interface WorkspaceSettingDto extends BaseDto {
    visibility: WorkspaceVisibilityType;
    contentVisibility: WorkspaceContentVisibilityType;
    joinType: WorkspaceJoinType;
}

export interface BaseRequest {
    locale?: LocaleType | null;
    conversationId?: string | null;
}

export interface CompleteResetPasswordRequest extends BaseRequest {
    locale: LocaleType;
    uniqueToken: string;
}

export interface ConfirmEmailRequest extends BaseRequest {
    uniqueToken: string;
}

export interface InitializeResetPasswordRequest extends BaseRequest {
    locale: LocaleType;
    email: string;
}

export interface ResendConfirmEmailRequest extends BaseRequest {
    locale: LocaleType;
    token: string;
}

export interface SetCommunicationPermissionsRequest extends BaseRequest {
    email?: boolean | null;
    pushNotification?: boolean | null;
}

export interface UpdatePasswordRequest extends BaseRequest {
    currentPassword: string;
    newPassword: string;
}

export interface RegisterViaMailRequest extends BaseRequest {
    locale: LocaleType;
    email: string;
    password: string;
}

export interface AuthCompleteRequest extends BaseRequest {
    email: string;
    provider: ProviderType;
    csrf: string;
    code: string;
    timeZone: string;
}

export interface AuthInitializeRequest extends BaseRequest {
    email: string;
}

export interface LoginWithPasswordRequest extends BaseRequest {
    email: string;
    password: string;
    timeZone?: string | null;
}

export interface CalendarEventDateUpdateRequest {
    calendarId: string;
    calendarSourceId: string;
    calendarEventId: string;
    assignedDate: Date;
    dueDate: Date;
    hasPreciseAssignedDate?: boolean | null;
    hasPreciseDueDate?: boolean | null;
}

export interface CalendarEventFilterRequest extends BaseRequest {
    workspaceId: string;
    teamIdList?: string[] | null;
    excludingTeamIdList?: string[] | null;
    calendarIdList?: string[] | null;
    timespanStart: Date;
    timespanEnd: Date;
}

export interface CalendarEventInitializeRequest {
    calendarId: string;
    calendarSourceId: string;
    summary?: string | null;
    description?: string | null;
    location?: string | null;
    assignedDate: Date;
    dueDate: Date;
    hasPreciseAssignedDate?: boolean | null;
    hasPreciseDueDate?: boolean | null;
}

export interface CalendarEventMoveRequest {
    calendarId: string;
    calendarSourceId: string;
    targetCalendarSourceId: string;
    eventId: string;
}

export interface CalendarEventTitleDescriptionUpdateRequest {
    calendarId: string;
    calendarSourceId: string;
    calendarEventId: string;
    title?: string | null;
    description?: string | null;
}

export interface InitializeChannelRequest extends BaseRequest {
    workspaceId: string;
    title: string;
    participationType: ChannelParticipationType;
    channelVisibilityType: ChannelVisibilityType;
    initialSettings?: InitialChannelSettingDto[] | null;
}

export interface InitializeChannelSettingsRequest extends BaseRequest {
    channelId: string;
    settingsType: ChannelSettingType;
    settingsValue: string;
}

export interface UpdateChannelRequest extends BaseRequest {
    newTitle?: string | null;
    channelVisibilityType?: ChannelVisibilityType | null;
    participationType?: ChannelParticipationType | null;
}

export interface InitializeConversationRequest extends BaseRequest {
    workspaceId: string;
    initialMessageBody: string;
    participantAccountIds: string[];
}

export interface SendMessageRequest extends BaseRequest {
    body: string;
    data?: { [index: string]: string } | null;
}

export interface InitializeThreadRequest extends BaseRequest {
    channelId: string;
    initialMessageBody: string;
}

export interface RobotsInitializeThreadRequest extends BaseRequest {
    initialMessageBody: string;
}

export interface NotificationTargetInitializeRequest extends BaseRequest {
    externalTargetId: string;
    targetType?: NotificationTargetType | null;
    providerType: NotificationProviderType;
}

export interface InitializeMilestoneRequest extends BaseRequest {
    projectId: string;
    title: string;
    description?: string | null;
    targetDate?: Date | null;
}

export interface MilestoneUpdateRequest extends BaseRequest {
    milestoneId: string;
    title?: string | null;
    description?: string | null;
    targetDate?: Date | null;
    order?: number | null;
    milestoneState?: MilestoneStateType | null;
}

export interface ProjectDatesUpdateRequest extends BaseRequest {
    startDate?: Date | null;
    updateStartDate?: boolean | null;
    targetDate?: Date | null;
    updateTargetDate?: boolean | null;
}

export interface ProjectDescriptionUpdateRequest extends BaseRequest {
    description?: string | null;
}

export interface ProjectFeedSettingsOperationRequest extends BaseRequest {
    projectId: string;
    projectTitle?: string | null;
    projectFeedAccessType?: ProjectFeedAccessType | null;
    projectPostInitializeAccessType?: ProjectPostInitializeAccessType | null;
    projectPostCommentPolicyType?: ProjectPostCommentPolicyType | null;
    info?: string | null;
    infoWebsiteUrl?: string | null;
}

export interface ProjectInitializeRequest extends BaseRequest {
    workspaceId: string;
    title: string;
    description?: string | null;
    projectState?: ProjectStateType | null;
    projectPriority?: ProjectPriorityType | null;
    leadWorkspaceMemberId?: string | null;
    startDate?: Date | null;
    targetDate?: Date | null;
    teamIds: string[];
    milestones?: MilestoneInitializeDto[] | null;
}

export interface ProjectPostAddCommentRequest extends BaseRequest {
    projectId: string;
    postId: string;
    body: string;
    quoteId?: string | null;
}

export interface ProjectPostInitializeRequest extends BaseRequest {
    projectId: string;
    body: string;
    files?: MultipartFile[] | null;
}

export interface ProjectPriorityUpdateRequest extends BaseRequest {
    projectPriority: ProjectPriorityType;
}

export interface ProjectStateUpdateRequest extends BaseRequest {
    projectState: ProjectStateType;
}

export interface ProjectTeamOperationRequest extends BaseRequest {
    projectId: string;
    teamIds: string[];
}

export interface ProjectTitleUpdateRequest extends BaseRequest {
    title: string;
}

export interface ProjectUpdateArchivedRequest extends BaseRequest {
    archived: boolean;
}

export interface ProjectUpdateLeadRequest extends BaseRequest {
    workspaceMemberId?: string | null;
}

export interface TaskReminderInitializeRequest extends BaseRequest {
    taskId: string;
    beforeAssignedDate?: boolean | null;
    beforeDueDate?: boolean | null;
    specificRemindDate?: Date | null;
    specificRemindRepeatStart?: Date | null;
    specificRemindRepeatEnd?: Date | null;
    specificRemindDateRepeatType?: RepeatType | null;
}

export interface RobotInitializeRequest extends BaseRequest {
    name: string;
    workspaceId: string;
    robotType?: RobotType | null;
}

export interface ChecklistItemLabelRequest extends BaseRequest {
    checklistId: string;
    label: string;
}

export interface InitializeChecklistRequest extends BaseRequest {
    taskId: string;
    title: string;
    initialItemLabel?: string | null;
}

export interface InitializeTaskCommentRequest extends BaseRequest {
    taskId: string;
    quoteCommentId?: string | null;
    comment: string;
}

export interface TaskAssigneeUpdateRequest extends BaseRequest {
    assigneeId?: string | null;
}

export interface TaskBoardEntryInitializeRequest extends BaseRequest {
    taskBoardId: string;
    taskId: string;
}

export interface TaskBoardInitializeRequest extends BaseRequest {
    workspaceId: string;
    teamId: string;
    title: string;
    dueDate?: Date | null;
}

export interface TaskBoardUpdateDueDateRequest extends TaskBoardUpdateRequest {
    dueDate?: Date | null;
}

export interface TaskBoardUpdateRequest extends BaseRequest {
    taskBoardId: string;
}

export interface TaskBoardUpdateStateRequest extends TaskBoardUpdateRequest {
    state: TaskBoardStateType;
}

export interface TaskBoardUpdateTitleRequest extends TaskBoardUpdateRequest {
    title: string;
}

export interface TaskDateUpdateRequest extends BaseRequest {
    assignedDate?: Date | null;
    dueDate?: Date | null;
    hasPreciseAssignedDate?: boolean | null;
    hasPreciseDueDate?: boolean | null;
}

export interface TaskFilterRequest extends BaseRequest {
    page?: number | null;
    size?: number | null;
    workspaceId: string;
    teamIdList?: string[] | null;
    excludingTeamIdList?: string[] | null;
    topicIds?: string[] | null;
    ownerIds?: string[] | null;
    assigneeIds?: string[] | null;
    workflowStatusIdList?: string[] | null;
    workflowStateGroups?: TeamWorkflowStateGroup[] | null;
    timespanStart?: Date | null;
    timespanEnd?: Date | null;
    sort?: FilterSort | null;
    externalCalendarList?: TaskExternalCalendarFilterDto[] | null;
    projectIds?: string[] | null;
    milestoneIds?: string[] | null;
}

export interface TaskInitializeRequest extends BaseRequest {
    workspaceId: string;
    teamId: string;
    topicId?: string | null;
    assignedTo?: string | null;
    assignedDate?: Date | null;
    dueDate?: Date | null;
    hasPreciseAssignedDate?: boolean | null;
    hasPreciseDueDate?: boolean | null;
    title: string;
    description?: string | null;
    subTaskOf?: string | null;
    boardId?: string | null;
    feedId?: string | null;
    feedItemId?: string | null;
    projectId?: string | null;
    milestoneId?: string | null;
}

export interface TaskProjectAndMilestoneUpdateRequest {
    projectId?: string | null;
    milestoneId?: string | null;
}

export interface TaskRelationInitializeRequest extends BaseRequest {
    taskId: string;
    relatedTaskId: string;
    relation: TaskRelationType;
}

export interface TaskRetrieveAllRequest extends BaseRequest {
    workspaceId: string;
    teamId: string;
    page: number;
}

export interface TaskSearchRequest extends BaseRequest {
    workspaceId: string;
    teamIdList?: string[] | null;
    query: string;
}

export interface TaskUpdateDescriptionRequest extends BaseRequest {
    description?: string | null;
}

export interface TaskUpdateRequest extends BaseRequest {
    taskId: string;
    topicId: string;
    assignedTo?: string | null;
    assignedDate?: Date | null;
    dueDate?: Date | null;
    title: string;
    description?: string | null;
}

export interface TaskUpdateTitleRequest extends BaseRequest {
    title: string;
}

export interface UpdateChecklistTitleRequest extends BaseRequest {
    title: string;
}

export interface AddTeamMemberRequest extends BaseRequest {
    accountId: string;
    teamId: string;
    role: TeamMemberRoleType;
}

export interface InitializeTeamWorkflowStatusRequest extends BaseRequest {
    workflowStateGroup: TeamWorkflowStateGroup;
    name: string;
}

export interface TeamInitializeRequest extends BaseRequest {
    workspaceId: string;
    name: string;
    username: string;
    tag: string;
    visibility: TeamVisibilityType;
    joinMethod: TeamJoinMethodType;
    taskVisibility: TeamTaskVisibilityType;
}

export interface TeamWorkflowStatusNameChangeRequest extends BaseRequest {
    name: string;
}

export interface RetrieveTopicListRequest extends BaseRequest {
    topicIds: string[];
}

export interface TopicInitializeRequest extends BaseRequest {
    workspaceId: string;
    teamId: string;
    name: string;
    tag: string;
    color: string;
}

export interface TopicUpdateRequest extends BaseRequest {
    topicId: string;
    color: string;
    name: string;
    tag: string;
}

export interface WorkspaceActivityFilterRequest extends BaseRequest {
    page?: number | null;
    size?: number | null;
    workspaceId: string;
    teamIdList?: string[] | null;
    taskIds?: string[] | null;
}

export interface WorkspaceInitializeRequest extends BaseRequest {
    title: string;
    description: string;
    handle: string;
    visibility: WorkspaceVisibilityType;
    joinType: WorkspaceJoinType;
}

export interface WorkspaceMemberInvitationRespondRequest extends BaseRequest {
    token: string;
    accepted: boolean;
}

export interface WorkspaceMemberInviteRequest extends BaseRequest {
    email: string;
    workspaceId: string;
    initialTeamId: string;
    forRole: WorkspaceAccountRoleType;
}

export interface WorkspaceTitleUpdateRequest extends BaseRequest {
    title: string;
}

export interface BaseResponse {
    responseStatusType: ResponseStatusType;
    errorCode: string;
    errorMessage: string;
    errorGroup: string;
    consumerErrorMessage: string;
    responseLocale: string;
    systemTime: number;
    conversationId: string;
    additionalInfo: { [index: string]: string };
}

export interface AccountCommunicationPermissionsResponse extends BaseResponse {
    data: AccountCommunicationPermissionDto;
}

export interface AccountDeletionEligibilityResponse extends BaseResponse {
    data: AccountDeleteEligibilityDto;
}

export interface AccountRetrieveResponse extends BaseResponse {
    data: AccountDto;
    sessionId: string;
}

export interface AuthInitializeResponse extends BaseResponse {
    email: string;
    csrf: string;
    preferredLocaleId: number;
    code: string;
}

export interface AuthRedirectInfoResponse extends BaseResponse {
    redirectUrl: string;
}

export interface AuthResponse extends BaseResponse {
    token: string;
}

export interface CalendarEventListingResponse extends BaseResponse {
    data: CalendarEventDto[];
}

export interface CalendarMemberListingResponse extends BaseResponse {
    data: CalendarMemberDto[];
}

export interface CalendarMemberPaginatedResponse extends BaseResponse {
    data: PageDto<CalendarMemberDto>;
}

export interface CalendarShareableKeyResponse extends BaseResponse {
    data: CalendarShareKeyDto;
}

export interface FeedMemberListingResponse extends BaseResponse {
    data: FeedMemberDto[];
}

export interface FeedMemberPaginatedResponse extends BaseResponse {
    data: PageDto<FeedMemberDto>;
}

export interface ChannelListingResponse extends BaseResponse {
    data: PlainChannelDto[];
}

export interface ChannelMemberListingResponse extends BaseResponse {
    data: ChannelMemberDto[];
}

export interface ChannelMembershipInfoListingResponse extends BaseResponse {
    data: ChannelMembershipInfoDto[];
}

export interface ConversationInitializeResponse extends BaseResponse {
    data: string;
}

export interface ConversationParticipantListingResponse extends BaseResponse {
    data: ConversationParticipantDto[];
}

export interface MessageListingPaginatedResponse extends BaseResponse {
    data: PageDto<MessageDto>;
}

export interface MessageResponse extends BaseResponse {
    data: MessageDto;
}

export interface MessagingTokenResponse extends BaseResponse {
    data: MessagingTokenDto;
}

export interface ThreadListingResponse extends BaseResponse {
    data: PageDto<ThreadDto>;
}

export interface ThreadResponse extends BaseResponse {
    data: ThreadDto;
}

export interface NotificationEventListingResponse extends BaseResponse {
    data: PageDto<NotificationEventDto>;
}

export interface RetrieveUnreadNotificationEventCountResponse extends BaseResponse {
    unreadNotificationCount: number;
}

export interface RetrieveSubscriptionInfoResponse extends BaseResponse {
    data: SubscriptionInfoDto;
}

export interface AccountProjectPermissionFlagsResponse extends BaseResponse {
    data: AccountProjectPermissionFlags;
}

export interface ProjectFeedPaginatedResponse extends BaseResponse {
    data: PageDto<ProjectPostDto>;
}

export interface ProjectFeedPostResponse extends BaseResponse {
    data: ProjectPostDto;
}

export interface ProjectListingPaginatedResponse extends BaseResponse {
    data: PageDto<ProjectDto>;
}

export interface ProjectPostPaginatedCommentResponse extends BaseResponse {
    data: PageDto<ProjectPostCommentDto>;
}

export interface ProjectRetrieveResponse extends BaseResponse {
    data: ProjectDto;
}

export interface PublicProjectRetrieveResponse extends BaseResponse {
    data: PublicProjectDto;
}

export interface ReminderJobResponse extends BaseResponse {
    data: ReminderJobDto;
}

export interface ReminderResponse extends BaseResponse {
    data: ReminderDto[];
}

export interface RobotSecretResponse extends BaseResponse {
    data: RobotSecretDto;
}

export interface PaginatedTaskCommentResponse extends BaseResponse {
    data: PageDto<CommentDto>;
}

export interface RetrieveChecklistResponse extends BaseResponse {
    data: ChecklistDto;
}

export interface TaskActivityRetrieveResponse extends BaseResponse {
    data: WorkspaceActivityDto[];
}

export interface TaskAndTaskBoardRelationResponse extends BaseResponse {
    data: TaskAndTaskBoardRelationDto;
}

export interface TaskBoardEntryPaginatedResponse extends BaseResponse {
    data: PageDto<TaskBoardEntryDto>;
}

export interface TaskBoardListingPaginatedResponse extends BaseResponse {
    data: PageDto<TaskBoardDto>;
}

export interface TaskBoardResponse extends BaseResponse {
    data: TaskBoardDto;
}

export interface TaskBoardRetrieveResponse extends BaseResponse {
    data: TaskBoardDto;
}

export interface TaskFeedItemResponse extends BaseResponse {
    data: TaskFeedItemListDto;
}

export interface TaskListingListedResponse extends BaseResponse {
    data: TaskDto[];
}

export interface TaskListingPaginatedResponse extends BaseResponse {
    data: PageDto<TaskDto>;
}

export interface TaskMediaResponse extends BaseResponse {
    data: MediaDto[];
}

export interface TaskNumbersResponse {
    data: TaskAnalyticNumbersDto;
}

export interface TaskPaginatedMediaResponse extends BaseResponse {
    data: PageDto<TaskMediaDto>;
}

export interface TaskResponse extends BaseResponse {
    data: TaskDto;
}

export interface TaskSearchResponse extends BaseResponse {
    data: PageDto<TaskDto>;
}

export interface TaskSubscribersListingResponse extends BaseRequest {
    data: TaskSubscriptionDto[];
}

export interface TaskSubscriptionResponse extends BaseResponse {
    data: TaskSubscriptionDto;
}

export interface FeedContentItemResponse extends BaseResponse {
    data: FeedContentItemDto;
}

export interface FeedContentResponse extends BaseResponse {
    data: FeedContentDto;
}

export interface TeamListingResponse extends BaseResponse {
    data: TeamDto[];
}

export interface TeamMemberListingResponse extends BaseResponse {
    data: PageDto<TeamMemberDto>;
}

export interface TeamMembershipsResponse extends BaseResponse {
    data: TeamMemberDto[];
}

export interface TeamResponse extends BaseResponse {
    data: TeamDto;
}

export interface TeamWorkflowStatusListingResponse extends BaseResponse {
    data: GroupedTeamWorkflowStatusListDto;
}

export interface TopicListingResponse extends BaseResponse {
    data: PageDto<TopicDto>;
}

export interface TopicResponse extends BaseResponse {
    data: TopicDto;
}

export interface TopicSearchResponse extends BaseResponse {
    data: TopicDto[];
}

export interface WorkspaceActivityListResponse extends BaseResponse {
    data: PageDto<WorkspaceActivityDto>;
}

export interface WorkspaceActivityResponse extends BaseResponse {
    data: WorkspaceActivityDto;
}

export interface WorkspaceBaseResponse extends BaseResponse {
    data: WorkspaceDto;
}

export interface WorkspaceDisplayPreferenceResponse extends BaseResponse {
    data: WorkspaceDisplayPreferenceDto;
}

export interface WorkspaceInvitationInfoResponse extends BaseResponse {
    data: WorkspaceInvitationInfoDto;
}

export interface WorkspaceInvitationListingResponse extends BaseResponse {
    data: PageDto<WorkspaceInvitationDto>;
}

export interface WorkspaceMemberListingBaseResponse extends BaseResponse {
    data: PageDto<WorkspaceMemberDto>;
}

export interface GoogleCalendarEventInfo {
    kind: string;
    etag: string;
    id: string;
    status: string;
    htmlLink: string;
    created: string;
    updated: string;
    summary: string;
    description: string;
    location: string;
    colorId: string;
    creator: GoogleCalendarEventAttendee;
    organizer: GoogleCalendarEventAttendee;
    start: GoogleCalendarEventDate;
    end: GoogleCalendarEventDate;
    endTimeUnspecified: boolean;
    recurrence: string[];
    recurringEventId: string;
    originalStartTime: GoogleCalendarEventDate;
    transparency: string;
    visibility: string;
    sequence: number;
    attendees: GoogleCalendarEventAttendee[];
    attendeesOmitted: boolean;
    extendedProperties: GoogleCalendarExtendedProperties;
    hangoutLink: string;
    conferenceData: GoogleCalendarConferenceData;
    gadget: GoogleCalendarGadget;
    anyoneCanAddSelf: boolean;
    guestsCanInviteOthers: boolean;
    guestsCanModify: boolean;
    guestsCanSeeOtherGuests: boolean;
    privateCopy: boolean;
    locked: boolean;
    reminders: GoogleCalendarReminder;
    source: GoogleCalendarSource;
    workingLocationProperties: GoogleCalendarWorkingLocationProperties;
    outOfOfficeProperties: GoogleCalendarOutOfOfficeProperties;
    focusTimeProperties: GoogleCalendarFocusTimeProperties;
    attachments: GoogleCalendarAttachment[];
    eventType: string;
    icalUID: string;
}

export interface PassthroughDetailDto {
    passthroughType: PassthroughType;
    detailValue: string;
}

export interface MultipartFile extends InputStreamSource {
    contentType: string;
    name: string;
    bytes: any;
    empty: boolean;
    resource: Resource;
    size: number;
    originalFilename: string;
}

export interface GoogleCalendarEventAttendee {
    id: string;
    email: string;
    displayName: string;
    organizer: string;
    self: string;
    resource: string;
    optional: string;
    responseStatus: string;
    comment: string;
    additionalGuests: number;
}

export interface GoogleCalendarEventDate {
    date: string;
    dateTime: string;
    timeZone: string;
}

export interface GoogleCalendarExtendedProperties {
    private: { [index: string]: string };
    shared: { [index: string]: string };
}

export interface GoogleCalendarConferenceData {
    createRequest: GoogleCalendarConferenceDataRequest;
    entryPoints: GoogleCalendarEntryPoint[];
    conferenceSolution: GoogleCalendarConferenceSolution;
    conferenceId: string;
    signature: string;
    notes: string;
}

export interface GoogleCalendarGadget {
    type: string;
    title: string;
    link: string;
    iconLink: string;
    width: number;
    height: number;
    display: string;
    preferences: { [index: string]: string };
}

export interface GoogleCalendarReminder {
    useDefault: boolean;
    overrides: GoogleCalendarReminderOverride[];
}

export interface GoogleCalendarSource {
    url: string;
    title: string;
}

export interface GoogleCalendarWorkingLocationProperties {
    type: string;
    homeOffice: any;
    customLocation: GoogleCalendarCustomLocation;
    officeLocation: GoogleCalendarOfficeLocation;
}

export interface GoogleCalendarOutOfOfficeProperties {
    autoDeclineMode: string;
    declineMessage: string;
}

export interface GoogleCalendarFocusTimeProperties extends GoogleCalendarOutOfOfficeProperties {
    chatStatus: string;
}

export interface GoogleCalendarAttachment {
    fileUrl: string;
    title: string;
    mimeType: string;
    iconLink: string;
    fileId: string;
}

export interface Resource extends InputStreamSource {
    open: boolean;
    file: any;
    readable: boolean;
    url: URL;
    description: string;
    uri: URI;
    filename: string;
}

export interface InputStreamSource {
    inputStream: any;
}

export interface GoogleCalendarConferenceDataRequest {
    requestId: string;
    conferenceSolutionKey: GoogleCalendarConferenceSolutionKey;
    status: GoogleCalendarConferenceDataStatus;
}

export interface GoogleCalendarEntryPoint {
    entryPointType: string;
    uri: string;
    label: string;
    pin: string;
    accessCode: string;
    meetingCode: string;
    passcode: string;
    password: string;
}

export interface GoogleCalendarConferenceSolution {
    key: GoogleCalendarConferenceSolutionKey;
    name: string;
    iconUri: string;
}

export interface GoogleCalendarReminderOverride {
    method: string;
    minutes: number;
}

export interface GoogleCalendarCustomLocation {
    label: string;
}

export interface GoogleCalendarOfficeLocation {
    buildingId: string;
    floorId: string;
    floorSectionId: string;
    deskId: string;
    label: string;
}

export interface URL extends Serializable {
}

export interface URI extends Comparable<URI>, Serializable {
}

export interface GoogleCalendarConferenceSolutionKey {
    type: string;
}

export interface GoogleCalendarConferenceDataStatus {
    statusCode: string;
}

export interface Serializable {
}

export interface Comparable<T> {
}

export type DayType = "MONDAY" | "TUESDAY" | "WEDNESDAY" | "THURSDAY" | "FRIDAY" | "SATURDAY" | "SUNDAY";

export type FilterSort = "IDATE_DESC" | "IDATE_ASC" | "ASSIGNED_DATE_DESC" | "ASSIGNED_DATE_ASC";

export type ResponseStatusType = "SUCCESS" | "FAILURE";

export type PermissionType = "ACCOUNT_ROLE_EDIT" | "PROCESS_REMINDER_JOB" | "EXPIRE_TEMP_PUBLIC_MEDIA" | "ROBOT_MESSAGE_INIT";

export type RoleType = "ADMIN" | "SERVICE" | "USER" | "ROBOT";

export type ProviderType = "OAUTH_MAIL" | "OTP_MAIL" | "PASSWORD_MAIL";

export type CalendarEventSourceType = "TASK" | "GOOGLE_CALENDAR";

export type GoogleScopeType = "OPEN_ID" | "USERINFO_PROFILE" | "USERINFO_EMAIL" | "CALENDAR" | "CALENDAR_EVENTS" | "CALENDAR_SETTINGS_READONLY";

export type UserConsentPurposeType = "LOGIN" | "ATTACH_MAIL" | "ATTACH_CALENDAR";

export type IntegrationProvider = "GOOGLE";

export type IntegrationScopeType = "LOGIN" | "EMAIL" | "CALENDAR";

export type LocaleStringType = "LOGIN_SMS_TEXT" | "LOGIN_MAIL_TITLE" | "LOGIN_MAIL_TEXT" | "MAIL_CONFIRMATION_TITLE" | "MAIL_CONFIRMATION_TEXT" | "MAIL_CONFIRMATION_CTA_LABEL" | "PASSWORD_RESET_TITLE" | "PASSWORD_RESET_TEXT" | "PASSWORD_RESET_CTA_LABEL" | "NEW_PASSWORD_TITLE" | "NEW_PASSWORD_TEXT" | "TEAM_WORKFLOW_STATUS_BACKLOG" | "TEAM_WORKFLOW_STATUS_NOT_STARTED" | "TEAM_WORKFLOW_STATUS_STARTED" | "TEAM_WORKFLOW_STATUS_COMPLETED" | "TEAM_WORKFLOW_STATUS_CANCELLED" | "TASK_REMINDER_TITLE" | "TASK_REMINDER_TEXT" | "TASK_REMINDER_TYPE_ASSIGNED_DATE" | "TASK_REMINDER_TYPE_DUE_DATE" | "TASK_REMINDER_TYPE_SPECIFIC_DATE" | "TASK_REMINDER_GO_TO_TASK" | "WORKSPACE_INVITATION_TITLE_MAIL" | "WORKSPACE_INVITATION_TITLE_BODY" | "WORKSPACE_INVITATION_TEXT" | "WORKSPACE_INVITATION_CTA_LABEL" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_INITIALIZED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CLOSED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_EDIT_TASK_TITLE" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_EDIT_TASK_DESC" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_UPDATE_TOPIC" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_UPDATE_WORKFLOW_STATUS" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_ASSIGNEE" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_ASSIGNED_DATE" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_DUE_DATE" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_INITIALIZED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_REMOVED" | "WORKSPACE_ACTIVITY_NOTIFICATION_SUBTEXT_VISIT_TASK_PAGE_TO_GET_MORE_DETAIL" | "WORKSPACE_ACTIVITY_NOTIFICATION_CTA_LABEL" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_GENERIC_ACTIVITY" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_INITIALIZED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_REMOVED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_TITLE_CHANGED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_CHECKED_STATUS_CHANGED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_LABEL_CHANGED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_REMOVED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_INITIALIZED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_NEW_COMMENT" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_ATTACHMENT_ADDED" | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_ATTACHMENT_DELETED" | "ACCOUNT_DELETION_MAIL_TITLE" | "ACCOUNT_DELETION_MAIL_TEXT" | "ACCOUNT_DELETION_MAIL_SUBTEXT" | "ACCOUNT_DELETION_MAIL_CTA_LABEL";

export type LocaleType = "TR" | "EN";

export type LockSourceType = "BALANCE" | "TOPIC_TASK_INIT" | "TEAM_TASK_INIT" | "TEAM_WORKFLOW_STATUS" | "ACCOUNT_PASSWORD_RESET" | "TASK_BOARD_EDIT" | "REMINDER_JOB_PROCESS" | "CONVERSATION_INIT" | "CONVERSATION" | "PROJECT_MILESTONE";

export type FileType = "PROFILE_PIC" | "TASK_FILE" | "PROJECT_POST_FILE";

export type MediaFileProviderType = "GCLOUD";

export type MediaOwnerType = "USER" | "WORKSPACE" | "TASK" | "PROJECT_POST";

export type MediaVisibilityType = "PUBLIC" | "PRIVATE" | "TEMP_PUBLIC";

export type ChannelMemberRoleType = "OWNER" | "ADMIN" | "MEMBER" | "ROBOT";

export type ChannelParticipationType = "EVERYONE" | "ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY" | "READ_ONLY";

export type ChannelSettingType = "SYNC_MEMBERS_WITH_TEAM";

export type ChannelVisibilityType = "EVERYONE" | "MEMBERS_ONLY" | "PUBLIC_WITH_GUESTS";

export type MessageReactionType = "UNICODE_EMOJI";

export type MessageType = "USER_MESSAGE" | "CONVERSATION_INIT";

export type ThreadType = "CLASSIC" | "CHANNEL_INITIAL" | "INITIALIZED_BY_ROBOT";

export type NotificationEventState = "INITIALIZED" | "SENT";

export type NotificationProviderType = "ONE_SIGNAL" | "FIREBASE" | "EXPO";

export type NotificationTargetType = "WEB" | "WEBVIEW";

export type NotificationType = "TASK_REMINDER" | "WORKSPACE_ACTIVITY" | "TASK_INITIALIZED" | "TASK_CLOSED" | "EDIT_TASK_TITLE" | "EDIT_TASK_DESC" | "TASK_UPDATE_TOPIC" | "TASK_UPDATE_WORKFLOW_STATUS" | "TASK_CHANGE_ASSIGNEE" | "TASK_CHANGE_ASSIGNED_DATE" | "TASK_CHANGE_DUE_DATE" | "RELATION_INITIALIZED" | "RELATION_REMOVED" | "CHECKLIST_INITIALIZED" | "CHECKLIST_REMOVED" | "CHECKLIST_TITLE_CHANGED" | "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED" | "CHECKLIST_ITEM_LABEL_CHANGED" | "CHECKLIST_ITEM_REMOVED" | "CHECKLIST_ITEM_INITIALIZED" | "TASK_NEW_COMMENT" | "TASK_ATTACHMENT_ADDED" | "TASK_ATTACHMENT_DELETED" | "MESSAGING_NEW_MESSAGE_THREAD" | "MESSAGING_NEW_MESSAGE_CONVERSATION";

export type PassiveReason = "SYSTEM" | "USER_ACTION" | "FREEZE_ACCOUNT" | "DELETE_ACCOUNT" | "BANNED_ACCOUNT" | "SUSPENDED_ACCOUNT" | "REQUEST_RESPONSE" | "SMS_LOGIN_TOKEN_USED" | "PHONE_CHANGED" | "EMAIL_LOGIN_TOKEN_EXPIRED" | "EMAIL_LOGIN_TOKEN_USED" | "EMAIL_ATTACH_TOKEN_USED" | "REMOVE_FEATURE" | "REPORT_RESOLVE_GUILTY" | "REPORT_RESOLVE_NOT_GUILTY" | "TICKET_RESOLVE" | "WAIT_LIST_PASSCODE_USED" | "PROFILE_PIC_UPDATE" | "UNFOLLOW" | "PAYMENT_ISSUE";

export type MilestoneStateType = "IN_PROGRESS" | "COMPLETED";

export type ProjectFeedAccessType = "PRIVATE" | "PUBLIC" | "GUESTS_ONLY";

export type ProjectPostCommentPolicyType = "WORKSPACE_ADMINS" | "PROJECT_LEAD" | "PROJECT_TEAM_ADMINS" | "PROJECT_TEAM_MEMBERS" | "WORKSPACE_MEMBERS" | "ANY_LOGGED_IN_USER";

export type ProjectPostInitializeAccessType = "WORKSPACE_ADMINS" | "PROJECT_LEAD" | "PROJECT_TEAM_ADMINS" | "PROJECT_TEAM_MEMBERS" | "WORKSPACE_MEMBERS";

export type ProjectPriorityType = "NONE" | "URGENT" | "HIGH" | "MEDIUM" | "LOW";

export type ProjectStateType = "BACKLOG" | "PLANNED" | "IN_PROGRESS" | "COMPLETED" | "CANCELLED";

export type ReminderJobStatus = "PENDING" | "COMPLETED" | "CANCELLED" | "FAILED";

export type ReminderType = "TASK";

export type RepeatType = "NONE" | "HOURLY" | "DAILY" | "WEEKLY" | "BIWEEKLY" | "MONTHLY" | "EVERY_3_MONTHS" | "EVERY_6_MONTHS" | "YEARLY";

export type RichTextSourceStack = "WYSIWYG" | "RC";

export type RichTextType = "TASK_DETAIL" | "TASK_COMMENT" | "MESSAGE" | "PROJECT" | "PROJECT_MILESTONE_DESCRIPTION" | "PROJECT_FEED_INFO" | "PROJECT_FEED_POST" | "PROJECT_FEED_POST_COMMENT";

export type RobotType = "MESSAGE";

export type TaskBoardStateType = "OPEN" | "CLOSED";

export type TaskRelationType = "BLOCKS" | "IS_BLOCKED_BY" | "SUBTASK";

export type TaskReminderType = "ASSIGNED_DATE" | "DUE_DATE" | "SPECIFIC_DATE";

export type TaskSource = "GOOGLE_CALENDAR";

export type TaskState = "TO_DO" | "IN_PROGRESS" | "IN_TEST" | "WONT_DO" | "DONE";

export type TeamJoinMethodType = "SYNC_MEMBERS_WITH_WORKSPACE" | "ON_DEMAND" | "FROM_TEAM_ADMIN";

export type TeamMemberRoleType = "ADMIN" | "MEMBER" | "GUEST";

export type TeamStateType = "ARCHIVED" | "ACTIVE";

export type TeamTaskVisibilityType = "VISIBLE_TO_ALL_TEAM_MEMBERS" | "OWNER_ASSIGNEE_AND_ADMINS";

export type TeamVisibilityType = "VISIBLE" | "HIDDEN";

export type TeamWorkflowStateGroup = "BACKLOG" | "NOT_STARTED" | "STARTED" | "COMPLETED" | "CANCELLED";

export type TokenType = "SMS_LOGIN" | "EMAIL_LOGIN" | "WEB_USERNAME_LOGIN" | "BOOKING_EMAIL_VALIDATION" | "CONTINUE_AS_LOGIN_TOKEN" | "CONFIRM_EMAIL" | "RESET_PASSWORD" | "WORKSPACE_INVITATION" | "ACCOUNT_DELETION";

export type TopicVisibility = "SHARED" | "PRIVATE";

export type UsernameRelatedObjectType = "ACCOUNT" | "WORKSPACE";

export type WorkspaceAccountRoleType = "OWNER" | "ADMIN" | "MEMBER" | "GUEST";

export type WorkspaceActivityType = "MEMBER_JOIN" | "MEMBER_LEFT" | "MEMBER_REMOVED" | "MEMBER_REQUESTED_ACCESS" | "TASK_INITIALIZED" | "TASK_CLOSED" | "EDIT_TASK_TITLE" | "EDIT_TASK_DESC" | "TASK_UPDATE_TOPIC" | "TASK_UPDATE_WORKFLOW_STATUS" | "TASK_CHANGE_ASSIGNEE" | "TASK_CHANGE_ASSIGNED_DATE" | "TASK_CHANGE_DUE_DATE" | "TASK_NEW_COMMENT" | "RELATION_INITIALIZED" | "RELATION_REMOVED" | "CHECKLIST_INITIALIZED" | "CHECKLIST_REMOVED" | "CHECKLIST_TITLE_CHANGED" | "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED" | "CHECKLIST_ITEM_LABEL_CHANGED" | "CHECKLIST_ITEM_REMOVED" | "CHECKLIST_ITEM_INITIALIZED" | "ATTACHMENT_ADDED" | "ATTACHMENT_DELETED" | "TASK_PROJECT_ASSIGNMENT_UPDATE" | "TASK_MILESTONE_ASSIGNMENT_UPDATE" | "TASK_BOARD_ENTRY_INIT" | "TASK_BOARD_ENTRY_REMOVED" | "TASK_BOARD_ENTRY_ORDER_CHANGE";

export type WorkspaceContentVisibilityType = "VISIBLE" | "HIDDEN";

export type WorkspaceInvitationStatusType = "WAITING_FOR_ANSWER" | "ACCEPTED" | "DECLINED" | "TIMED_OUT";

export type WorkspaceJoinType = "NEVER" | "PUBLIC" | "WITH_REQUEST" | "WITH_PASSWORD";

export type WorkspaceTier = "BASIC" | "PRO";

export type WorkspaceVisibilityType = "VISIBLE" | "HIDDEN_LISTED" | "HIDDEN_UNLISTED";

export type SubscriptionStatus = "ACTIVE" | "TRIALING" | "PAST_DUE" | "PAUSED" | "DELETED";

export type PassthroughType = "ACCOUNT_ID" | "WORKSPACE_ID" | "EMAIL" | "EMAIL_CONFIRMED";
