/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.0.1157 on 2022-11-29 22:33:46.

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

export interface AccountDto extends BaseDto {
  accountId: string;
  email: string;
  emailConfirmed: boolean;
  username?: string | null;
  roles: AccountRoleDto[];
  profilePicture?: MediaDto | null;
  workspaces: WorkspaceDto[];
  preferredWorkspaceId: string;
}

export interface AccountRoleDto {
  role: RoleType;
}

export interface PlainAccountDto extends BaseDto {
  accountId: string;
  username: string;
}

export interface MediaDto extends BaseDto {
  mediaId: string;
  ownerId: string;
  relatedObjectId: string;
  mediaOwnerType: MediaOwnerType;
  fileType: FileType;
  bucketName: string;
  storagePath: string;
  originalName: string;
}

export interface TaskDto extends BaseDto {
  taskId: string;
  topicId: string;
  workspaceId: string;
  teamId: string;
  ownerId: string;
  assignedDate: Date;
  dueDate: Date;
  teamTagNo: number;
  topicTagNo: number;
  title: string;
  description: string;
  topic: Topic;
  account: PlainAccountDto;
  workspace: WorkspaceDto;
  team: TeamDto;
}

export interface TeamDto extends BaseDto {
  teamId: string;
  workspaceId: string;
  name: string;
  tag: string;
  visibility: TeamVisibilityType;
  joinMethod: TeamJoinMethodType;
}

export interface TeamMemberDto extends BaseDto {
  teamMemberId: string;
  accountId: string;
  workspaceId: string;
  teamId: string;
  team: TeamDto;
  account: AccountDto;
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

export interface WorkspaceDisplayPreferenceDto {
  account_id: string;
  preferredWorkspaceId: string;
}

export interface WorkspaceDto extends BaseDto {
  workspaceId: string;
  title: string;
  description: string;
  username: string;
  settings: WorkspaceSettingDto;
  profilePicture: MediaDto;
  personal: boolean;
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
  email: string;
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
}

export interface AuthInitializeRequest extends BaseRequest {
  email: string;
}

export interface LoginWithPasswordRequest extends BaseRequest {
  email: string;
  password: string;
}

export interface TaskInitializeRequest extends BaseRequest {
  workspaceId: string;
  teamId: string;
  topicId: string;
  assignedDate: Date;
  dueDate: Date;
  title: string;
  description: string;
}

export interface TaskRetrieveAllRequest extends BaseRequest {
  workspaceId: string;
  teamId: string;
  page: number;
}

export interface TaskUpdateRequest {
  taskId: string;
  topicId: string;
  assignedDate: Date;
  dueDate: Date;
  title: string;
  description: string;
}

export interface TeamInitializeRequest extends BaseRequest {
  workspaceId: string;
  name: string;
  tag: string;
  visibility: TeamVisibilityType;
  joinMethod: TeamJoinMethodType;
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

export interface BaseResponseBuilder {}

export interface AccountRetrieveResponse extends BaseResponse {
  data: AccountDto;
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

export interface TaskListingResponse extends BaseResponse {
  data: PageDto<TaskDto>;
}

export interface TaskResponse extends BaseResponse {
  data: TaskDto;
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

export interface TopicListingResponse extends BaseResponse {
  data: PageDto<TopicDto>;
}

export interface TopicResponse extends BaseResponse {
  data: TopicDto;
}

export interface WorkspaceBaseResponse extends BaseResponse {
  data: WorkspaceDto;
}

export interface WorkspaceMemberListingBaseResponse extends BaseResponse {
  data: PageDto<WorkspaceMemberDto>;
}

export interface Topic extends BaseEntity {
  topicId: string;
  workspaceId: string;
  teamId: string;
  ownerId: string;
  color: string;
  name: string;
  tag: string;
}

export interface BaseEntity {
  createdDate: Date;
  lastUpdatedDate: Date;
  passiveId: string;
}

export type DayType =
  | "MONDAY"
  | "TUESDAY"
  | "WEDNESDAY"
  | "THURSDAY"
  | "FRIDAY"
  | "SATURDAY"
  | "SUNDAY";

export type ResponseStatusType = "SUCCESS" | "FAILURE";

export type TaskState =
  | "TO_DO"
  | "IN_PROGRESS"
  | "IN_TEST"
  | "WONT_DO"
  | "DONE";

export type PermissionType = "ACCOUNT_ROLE_EDIT";

export type RoleType = "ADMIN" | "USER";

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
  | "NEW_PASSWORD_TEXT";

export type LocaleType = "TR" | "EN";

export type LockSourceType = "BALANCE" | "TOPIC_TASK_INIT" | "TEAM_TASK_INIT";

export type FileType = "PROFILE_PIC";

export type MediaOwnerType = "USER" | "COMMUNITY" | "WORKSPACE";

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

export type TeamJoinMethodType =
  | "SYNC_MEMBERS_WITH_WORKSPACE"
  | "ON_DEMAND"
  | "FROM_TEAM_ADMIN";

export type TeamVisibilityType = "VISIBLE" | "HIDDEN";

export type TokenType =
  | "SMS_LOGIN"
  | "EMAIL_LOGIN"
  | "WEB_USERNAME_LOGIN"
  | "BOOKING_EMAIL_VALIDATION"
  | "CONTINUE_AS_LOGIN_TOKEN"
  | "CONFIRM_EMAIL"
  | "RESET_PASSWORD";

export type TopicVisibility = "SHARED" | "PRIVATE";

export type UsernameRelatedObjectType = "ACCOUNT" | "COMMUNITY" | "WORKSPACE";

export type WorkspaceAccountRoleType = "OWNER" | "ADMIN" | "MEMBER";

export type WorkspaceActivityType =
  | "JOIN"
  | "LEAVE"
  | "KICKED_OUT"
  | "REQUESTED_ACCESS"
  | "PLACED_BET";

export type WorkspaceContentVisibilityType = "VISIBLE" | "HIDDEN";

export type WorkspaceJoinType =
  | "NEVER"
  | "PUBLIC"
  | "WITH_REQUEST"
  | "WITH_PASSWORD";

export type WorkspaceVisibilityType =
  | "VISIBLE"
  | "HIDDEN_LISTED"
  | "HIDDEN_UNLISTED";
