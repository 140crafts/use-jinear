import {
  CalendarEventDto,
  IntegrationProvider,
  TaskBoardDto,
  TaskDto,
  TaskSearchResultDto,
  TeamDto,
  TeamMemberDto,
  TeamWorkflowStatusDto,
  TopicDto,
  WorkspaceDto,
  WorkspaceMemberDto
} from "@/model/be/jinear-core";

export interface IRelatedFeedItemData {
  feedId: string;
  feedItemId: string;
  itemTitle?: string;
  integrationProvider?: IntegrationProvider;
}

export default interface ModalState {
  visible: boolean;
}

export interface LoginWith2FaMailModalState extends ModalState {
  rerouteDisabled?: boolean;
  autoSubmitEmail?: string;
}

export interface NotFoundModalState extends ModalState {
  imgSrc?: string;
  imgAlt?: string;
  title?: string;
  label?: string;
}

export interface NewTopicModalState extends ModalState {
  workspace?: WorkspaceDto;
  team?: TeamDto;
}

export interface ChangeTaskWorkflowStatusModalState extends ModalState {
  task?: TaskDto;
}

export interface ChangeTaskTopicModalState extends ModalState {
  task?: TaskDto;
}

export interface ChangeTaskDateModalState extends ModalState {
  task?: TaskDto;
}

export interface ChangeTaskAssigneeModalState extends ModalState {
  task?: TaskDto;
}

export interface NewTeamModalState extends ModalState {
  workspace?: WorkspaceDto;
}

export interface SearchTaskModalState extends ModalState {
  workspaceId?: string;
  teamId?: string;
  onSelect?: (task: TaskSearchResultDto) => void;
}

export interface DialogModalState extends ModalState {
  title?: string;
  content?: string;
  htmlContent?: string;
  closeButtonLabel?: string;
  confirmButtonLabel?: string;
  onConfirm?: () => void;
  onClose?: () => void;
}

export interface ReminderListModalState extends ModalState {
  task?: TaskDto;
}

export interface NewReminderModalState extends ModalState {
  task?: TaskDto;
}

export interface DatePickerModalState extends ModalState {
  title?: string;
  initialDate?: Date;
  dateSpanStart?: Date;
  dateSpanEnd?: Date;
  disabledBefore?: Date;
  disabledAfter?: Date;
  onDateChange?: (date: Date) => void;
}

export interface WorkspaceMemberInviteModalState extends ModalState {
  workspaceId?: string;
}

export interface AddMemberToTeamModalState extends ModalState {
  workspace?: WorkspaceDto;
  team?: TeamDto;
}

export interface NotificationPermissionModalState extends ModalState {
  platform: "web" | "expo-webview";
}

export interface TaskOverviewModalState extends ModalState {
  workspaceName?: string;
  taskTag?: string;
}

export interface NewTaskModalState extends ModalState {
  workspace?: WorkspaceDto;
  team?: TeamDto;
  subTaskOf?: string;
  subTaskOfLabel?: string;
  initialAssignedDate?: Date;
  initialAssignedDateIsPrecise?: boolean;
  initialDueDate?: Date;
  initialDueDateIsPrecise?: boolean;
  initialRelatedFeedItemData?: IRelatedFeedItemData;
}

export interface TeamPickerModalState extends ModalState {
  workspaceId?: string;
  filterActiveTeams?: boolean;
  onPick?: (team: TeamDto) => void;
}

export interface WorkspacePickerModalState extends ModalState {
  currentWorkspaceId?: string;
  onPick?: (workspace: WorkspaceDto) => void;
}

export interface NewTaskBoardModalState extends ModalState {
  workspace?: WorkspaceDto;
  team?: TeamDto;
}

export interface BasicTextInputModalState extends ModalState {
  title?: string;
  infoText?: string;
  initialText?: string;
  onSubmit?: (text: string) => void;
}

export interface TaskTaskBoardAssignModalState extends ModalState {
  taskId?: string;
}

export interface TopicPickerModalState extends ModalState {
  workspace?: WorkspaceDto;
  team?: TeamDto;
  multiple?: boolean;
  initialSelectionOnMultiple?: TopicDto[];
  onPick?: (pickedList: TopicDto[]) => void;
}

export interface TeamMemberPickerModalState extends ModalState {
  workspaceId?: string;
  teamId?: string;
  multiple?: boolean;
  initialSelectionOnMultiple?: TeamMemberDto[];
  onPick?: (pickedList: TeamMemberDto[]) => void;
}

export interface TeamWorkflowStatusPickerModalState extends ModalState {
  workspaceId?: string;
  teamId?: string;
  multiple?: boolean;
  initialSelectionOnMultiple?: TeamWorkflowStatusDto[];
  onPick?: (pickedList: TeamWorkflowStatusDto[]) => void;
}

export interface TaskBoardPickerModalState extends ModalState {
  workspaceId?: string;
  teamId?: string;
  multiple?: boolean;
  initialSelectionOnMultiple?: TaskBoardDto[];
  onPick?: (pickedList: TaskBoardDto[]) => void;
}

export interface UpgradeWorkspacePlanModalState extends ModalState {
  workspaceId?: string;
}

export interface NewMailIntegrationModalState extends ModalState {
  workspaceId?: string;
}

export interface IntegrationFeedItemDetailModalState extends ModalState {
  workspace?: WorkspaceDto;
  feedId?: string;
  itemId?: string;
  title?: string | null;
}

export interface WorkspaceMemberPickerModalState extends ModalState {
  workspaceId?: string;
  multiple?: boolean;
  initialSelectionOnMultiple?: WorkspaceMemberDto[];
  onPick?: (pickedList: WorkspaceMemberDto[]) => void;
}

export interface NewCalendarIntegrationModalState extends ModalState {
  workspaceId?: string;
}

export interface CalendarExternalEventViewModalState extends ModalState {
  workspaceId?: string;
  calendarEventDto?: CalendarEventDto;
}

export interface CalendarShareEventsModalState extends ModalState {
  workspaceId?: string;
}

export interface ChannelSettingsModalState extends ModalState {
  workspaceName?: string;
  channelId?: string;
  workspaceId?: string;
}

export interface NewChannelModalState extends ModalState {
  workspaceId?: string;
}

export interface ConversationSettingsModalState extends ModalState {
  workspaceId?: string;
  conversationId?: string;
}