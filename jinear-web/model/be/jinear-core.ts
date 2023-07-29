/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.0.1157 on 2023-07-29 11:28:31.

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
  first: boolean;
  last: boolean;
}

export interface AccountCommunicationPermissionDto extends BaseDto {
  accountId: string;
  email: boolean;
  pushNotification: boolean;
}

export interface AccountDto extends BaseDto {
  accountId: string;
  email: string;
  emailConfirmed: boolean;
  localeType: LocaleType;
  timeZone: string;
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
}

export interface NotificationTargetDto extends BaseDto {
  externalTargetId: string;
  accountId: string;
  sessionInfoId: string;
  targetType: NotificationTargetType;
  providerType: NotificationProviderType;
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
  topic?: TopicDto | null;
  owner?: PlainAccountProfileDto | null;
  assignedToAccount?: PlainAccountProfileDto | null;
  workspace?: WorkspaceDto | null;
  team?: TeamDto | null;
  workflowStatus: TeamWorkflowStatusDto;
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
}

export interface TaskMediaDto extends BaseDto {
  task: TaskDto;
  media: MediaDto;
}

export interface TaskRelationDto {
  taskRelationId: string;
  taskId: string;
  relatedTaskId: string;
  relationType: TaskRelationType;
  task: RelatedTaskDto;
  relatedTask: RelatedTaskDto;
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

export interface TaskSearchResultDtoBuilder {}

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
  isPersonal: boolean;
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

export interface NotificationTargetInitializeRequest extends BaseRequest {
  externalTargetId: string;
  targetType?: NotificationTargetType | null;
  providerType: NotificationProviderType;
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

export interface ChecklistItemLabelRequest extends BaseRequest {
  checklistId: string;
  label: string;
}

export interface InitializeChecklistRequest extends BaseRequest {
  taskId: string;
  title: string;
  initialItemLabel?: string | null;
}

export interface RetrieveIntersectingTasksFromTeamRequest extends BaseRequest {
  workspaceId: string;
  teamId: string;
  timespanStart: Date;
  timespanEnd: Date;
}

export interface RetrieveIntersectingTasksFromWorkspaceRequest extends BaseRequest {
  workspaceId: string;
  timespanStart: Date;
  timespanEnd: Date;
}

export interface TaskAssigneeUpdateRequest {
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

export interface TaskDateUpdateRequest {
  assignedDate?: Date | null;
  dueDate?: Date | null;
  hasPreciseAssignedDate?: boolean | null;
  hasPreciseDueDate?: boolean | null;
}

export interface TaskFilterRequest {
  page?: number | null;
  workspaceId: string;
  teamIdList: string[];
  topicIds?: string[] | null;
  ownerIds?: string[] | null;
  assigneeIds?: string[] | null;
  workflowStatusIdList?: string[] | null;
  timespanStart?: Date | null;
  timespanEnd?: Date | null;
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

export interface WorkspaceInitializeRequest extends BaseRequest {
  title: string;
  description: string;
  handle: string;
  visibility: WorkspaceVisibilityType;
  joinType: WorkspaceJoinType;
  isPersonal: boolean;
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

export interface BaseResponse {
  responseStatusType: ResponseStatusType;
  errorCode: string;
  errorMessage: string;
  errorGroup: string;
  consumerErrorMessage: string;
  responseLocale: string;
  systemTime: number;
  conversationId: string;
}

export interface AccountCommunicationPermissionsResponse extends BaseResponse {
  data: AccountCommunicationPermissionDto;
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

export interface AuthResponse extends BaseResponse {
  token: string;
}

export interface NotificationEventListingResponse extends BaseResponse {
  data: PageDto<NotificationEventDto>;
}

export interface RetrieveUnreadNotificationEventCountResponse extends BaseResponse {
  unreadNotificationCount: number;
}

export interface ReminderJobResponse extends BaseResponse {
  data: ReminderJobDto;
}

export interface ReminderResponse extends BaseResponse {
  data: ReminderDto[];
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

export interface TaskListingPaginatedResponse extends BaseResponse {
  data: PageDto<TaskDto>;
}

export interface TaskListingResponse extends BaseResponse {
  data: TaskDto[];
}

export interface TaskMediaResponse extends BaseResponse {
  data: MediaDto[];
}

export interface TaskPaginatedMediaResponse extends BaseResponse {
  data: PageDto<TaskMediaDto>;
}

export interface TaskResponse extends BaseResponse {
  data: TaskDto;
}

export interface TaskSearchResponse extends BaseResponse {
  data: PageDto<TaskSearchResultDto>;
}

export interface TaskSubscribersListingResponse extends BaseRequest {
  data: TaskSubscriptionDto[];
}

export interface TaskSubscriptionResponse extends BaseResponse {
  data: TaskSubscriptionDto;
}

export interface TeamListingResponse extends BaseResponse {
  data: TeamDto[];
}

export interface TeamMemberListingResponse extends BaseResponse {
  data: PageDto<TeamMemberDto>;
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

export type DayType = "MONDAY" | "TUESDAY" | "WEDNESDAY" | "THURSDAY" | "FRIDAY" | "SATURDAY" | "SUNDAY";

export type ResponseStatusType = "SUCCESS" | "FAILURE";

export type PermissionType = "ACCOUNT_ROLE_EDIT" | "PROCESS_REMINDER_JOB" | "EXPIRE_TEMP_PUBLIC_MEDIA";

export type RoleType = "ADMIN" | "SERVICE" | "USER";

export type ProviderType = "OTP_MAIL" | "PASSWORD_MAIL";

export type LocaleStringType =
  | "LOGIN_SMS_TEXT"
  | "LOGIN_MAIL_TITLE"
  | "LOGIN_MAIL_TEXT"
  | "MAIL_CONFIRMATION_TITLE"
  | "MAIL_CONFIRMATION_TEXT"
  | "MAIL_CONFIRMATION_CTA_LABEL"
  | "PASSWORD_RESET_TITLE"
  | "PASSWORD_RESET_TEXT"
  | "PASSWORD_RESET_CTA_LABEL"
  | "NEW_PASSWORD_TITLE"
  | "NEW_PASSWORD_TEXT"
  | "TEAM_WORKFLOW_STATUS_BACKLOG"
  | "TEAM_WORKFLOW_STATUS_NOT_STARTED"
  | "TEAM_WORKFLOW_STATUS_STARTED"
  | "TEAM_WORKFLOW_STATUS_COMPLETED"
  | "TEAM_WORKFLOW_STATUS_CANCELLED"
  | "TASK_REMINDER_TITLE"
  | "TASK_REMINDER_TEXT"
  | "TASK_REMINDER_TYPE_ASSIGNED_DATE"
  | "TASK_REMINDER_TYPE_DUE_DATE"
  | "TASK_REMINDER_TYPE_SPECIFIC_DATE"
  | "TASK_REMINDER_GO_TO_TASK"
  | "WORKSPACE_INVITATION_TITLE_MAIL"
  | "WORKSPACE_INVITATION_TITLE_BODY"
  | "WORKSPACE_INVITATION_TEXT"
  | "WORKSPACE_INVITATION_CTA_LABEL"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_INITIALIZED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CLOSED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_EDIT_TASK_TITLE"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_EDIT_TASK_DESC"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_UPDATE_TOPIC"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_UPDATE_WORKFLOW_STATUS"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_ASSIGNEE"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_ASSIGNED_DATE"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_DUE_DATE"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_INITIALIZED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_REMOVED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_SUBTEXT_VISIT_TASK_PAGE_TO_GET_MORE_DETAIL"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_CTA_LABEL"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_GENERIC_ACTIVITY"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_INITIALIZED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_REMOVED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_TITLE_CHANGED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_CHECKED_STATUS_CHANGED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_LABEL_CHANGED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_REMOVED"
  | "WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_INITIALIZED";

export type LocaleType = "TR" | "EN";

export type LockSourceType =
  | "BALANCE"
  | "TOPIC_TASK_INIT"
  | "TEAM_TASK_INIT"
  | "TEAM_WORKFLOW_STATUS"
  | "ACCOUNT_PASSWORD_RESET"
  | "TASK_BOARD_EDIT"
  | "REMINDER_JOB_PROCESS";

export type FileType = "PROFILE_PIC" | "TASK_FILE";

export type MediaOwnerType = "USER" | "WORKSPACE" | "TASK";

export type MediaVisibilityType = "PUBLIC" | "PRIVATE" | "TEMP_PUBLIC";

export type NotificationEventState = "INITIALIZED" | "SENT";

export type NotificationProviderType = "ONE_SIGNAL" | "FIREBASE";

export type NotificationTargetType = "WEB";

export type NotificationType =
  | "TASK_REMINDER"
  | "WORKSPACE_ACTIVITY"
  | "TASK_INITIALIZED"
  | "TASK_CLOSED"
  | "EDIT_TASK_TITLE"
  | "EDIT_TASK_DESC"
  | "TASK_UPDATE_TOPIC"
  | "TASK_UPDATE_WORKFLOW_STATUS"
  | "TASK_CHANGE_ASSIGNEE"
  | "TASK_CHANGE_ASSIGNED_DATE"
  | "TASK_CHANGE_DUE_DATE"
  | "RELATION_INITIALIZED"
  | "RELATION_REMOVED"
  | "CHECKLIST_INITIALIZED"
  | "CHECKLIST_REMOVED"
  | "CHECKLIST_TITLE_CHANGED"
  | "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED"
  | "CHECKLIST_ITEM_LABEL_CHANGED"
  | "CHECKLIST_ITEM_REMOVED"
  | "CHECKLIST_ITEM_INITIALIZED";

export type PassiveReason =
  | "SYSTEM"
  | "USER_ACTION"
  | "FREEZE_ACCOUNT"
  | "DELETE_ACCOUNT"
  | "BANNED_ACCOUNT"
  | "SUSPENDED_ACCOUNT"
  | "REQUEST_RESPONSE"
  | "SMS_LOGIN_TOKEN_USED"
  | "PHONE_CHANGED"
  | "EMAIL_LOGIN_TOKEN_EXPIRED"
  | "EMAIL_LOGIN_TOKEN_USED"
  | "EMAIL_ATTACH_TOKEN_USED"
  | "REMOVE_FEATURE"
  | "REPORT_RESOLVE_GUILTY"
  | "REPORT_RESOLVE_NOT_GUILTY"
  | "TICKET_RESOLVE"
  | "WAIT_LIST_PASSCODE_USED"
  | "PROFILE_PIC_UPDATE"
  | "UNFOLLOW"
  | "PAYMENT_ISSUE";

export type ReminderJobStatus = "PENDING" | "COMPLETED" | "CANCELLED" | "FAILED";

export type ReminderType = "TASK";

export type RepeatType =
  | "NONE"
  | "HOURLY"
  | "DAILY"
  | "WEEKLY"
  | "BIWEEKLY"
  | "MONTHLY"
  | "EVERY_3_MONTHS"
  | "EVERY_6_MONTHS"
  | "YEARLY";

export type RichTextSourceStack = "WYSIWYG" | "RC";

export type RichTextType = "TASK_DETAIL";

export type TaskBoardStateType = "OPEN" | "CLOSED";

export type TaskFilterSort = "IDATE_DESC" | "IDATE_ASC" | "ASSIGNED_DATE_DESC" | "ASSIGNED_DATE_ASC";

export type TaskRelationType = "BLOCKS" | "IS_BLOCKED_BY" | "SUBTASK";

export type TaskReminderType = "ASSIGNED_DATE" | "DUE_DATE" | "SPECIFIC_DATE";

export type TaskState = "TO_DO" | "IN_PROGRESS" | "IN_TEST" | "WONT_DO" | "DONE";

export type TeamJoinMethodType = "SYNC_MEMBERS_WITH_WORKSPACE" | "ON_DEMAND" | "FROM_TEAM_ADMIN";

export type TeamMemberRoleType = "ADMIN" | "MEMBER" | "GUEST";

export type TeamVisibilityType = "VISIBLE" | "HIDDEN";

export type TeamWorkflowStateGroup = "BACKLOG" | "NOT_STARTED" | "STARTED" | "COMPLETED" | "CANCELLED";

export type TokenType =
  | "SMS_LOGIN"
  | "EMAIL_LOGIN"
  | "WEB_USERNAME_LOGIN"
  | "BOOKING_EMAIL_VALIDATION"
  | "CONTINUE_AS_LOGIN_TOKEN"
  | "CONFIRM_EMAIL"
  | "RESET_PASSWORD"
  | "WORKSPACE_INVITATION";

export type TopicVisibility = "SHARED" | "PRIVATE";

export type UsernameRelatedObjectType = "ACCOUNT" | "WORKSPACE";

export type WorkspaceAccountRoleType = "OWNER" | "ADMIN" | "MEMBER" | "GUEST";

export type WorkspaceActivityType =
  | "MEMBER_JOIN"
  | "MEMBER_LEFT"
  | "MEMBER_REMOVED"
  | "MEMBER_REQUESTED_ACCESS"
  | "TASK_INITIALIZED"
  | "TASK_CLOSED"
  | "EDIT_TASK_TITLE"
  | "EDIT_TASK_DESC"
  | "TASK_UPDATE_TOPIC"
  | "TASK_UPDATE_WORKFLOW_STATUS"
  | "TASK_CHANGE_ASSIGNEE"
  | "TASK_CHANGE_ASSIGNED_DATE"
  | "TASK_CHANGE_DUE_DATE"
  | "RELATION_INITIALIZED"
  | "RELATION_REMOVED"
  | "CHECKLIST_INITIALIZED"
  | "CHECKLIST_REMOVED"
  | "CHECKLIST_TITLE_CHANGED"
  | "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED"
  | "CHECKLIST_ITEM_LABEL_CHANGED"
  | "CHECKLIST_ITEM_REMOVED"
  | "CHECKLIST_ITEM_INITIALIZED"
  | "ATTACHMENT_ADDED"
  | "ATTACHMENT_DELETED"
  | "TASK_BOARD_ENTRY_INIT"
  | "TASK_BOARD_ENTRY_REMOVED"
  | "TASK_BOARD_ENTRY_ORDER_CHANGE";

export type WorkspaceContentVisibilityType = "VISIBLE" | "HIDDEN";

export type WorkspaceInvitationStatusType = "WAITING_FOR_ANSWER" | "ACCEPTED" | "DECLINED" | "TIMED_OUT";

export type WorkspaceJoinType = "NEVER" | "PUBLIC" | "WITH_REQUEST" | "WITH_PASSWORD";

export type WorkspaceTier = "BASIC" | "PLUS";

export type WorkspaceVisibilityType = "VISIBLE" | "HIDDEN_LISTED" | "HIDDEN_UNLISTED";
