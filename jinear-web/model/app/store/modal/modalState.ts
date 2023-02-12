import { TaskDto } from "@/model/be/jinear-core";

export default interface ModalState {
  visible: boolean;
}

export interface LoginWith2FaMailModalState extends ModalState {
  rerouteDisabled?: boolean;
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
  onSelect?: (task: TaskDto) => void;
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
