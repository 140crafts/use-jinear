import {
  TaskDto,
  TaskSearchResultDto,
  TeamDto,
  TeamMemberDto,
  TeamWorkflowStatusDto,
  TopicDto,
  WorkspaceDto,
} from "@/model/be/jinear-core";

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
  initialDate?: Date;
  onDateChange?: (date: Date) => void;
}

export interface TaskOverviewModalState extends ModalState {
  workspaceName?: string;
  taskTag?: string;
}

export interface NewTaskModalState extends ModalState {
  subTaskOf?: string;
  subTaskOfLabel?: string;
}

export interface TeamPickerModalState extends ModalState {
  workspaceId?: string;
  onPick?: (team: TeamDto) => void;
}

export interface WorkspacePickerModalState extends ModalState {
  currentWorkspaceId?: string;
  onPick?: (workspace: WorkspaceDto) => void;
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
  workspaceId?: string;
  teamId?: string;
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
