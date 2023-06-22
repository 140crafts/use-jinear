import { TaskSearchResultDto } from "@/model/be/jinear-core";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import ModalState, {
  AddMemberToTeamModalState,
  BasicTextInputModalState,
  ChangeTaskAssigneeModalState,
  ChangeTaskDateModalState,
  ChangeTaskTopicModalState,
  ChangeTaskWorkflowStatusModalState,
  DatePickerModalState,
  DialogModalState,
  LoginWith2FaMailModalState,
  NewReminderModalState,
  NewTaskBoardModalState,
  NewTaskModalState,
  NewTopicModalState,
  NotFoundModalState,
  ReminderListModalState,
  SearchTaskModalState,
  TaskOverviewModalState,
  TaskTaskBoardAssignModalState,
  TeamMemberPickerModalState,
  TeamPickerModalState,
  TeamWorkflowStatusPickerModalState,
  TopicPickerModalState,
  WorkspaceMemberInviteModalState,
  WorkspacePickerModalState,
} from "model/app/store/modal/modalState";
import { accountApi } from "../api/accountApi";
import { RootState } from "../store";

const initialState = {
  loginWith2FaMailModal: {
    visible: false,
    rerouteDisabled: false,
  },
  loadingModal: {
    visible: false,
  },
  notFoundModal: {
    visible: false,
    imgSrc: "/images/gif/sad-doge.gif",
    imgAlt: "Illustration for not found modal. Sad doge gif.",
    title: undefined,
    label: undefined,
  },
  newTaskModal: {
    visible: false,
  },
  teamOptionsModal: {
    visible: false,
  },
  newTopicModal: {
    visible: false,
  },
  changeTaskWorkflowStatusModal: {
    visible: false,
  },
  changeTaskTopicModal: {
    visible: false,
  },
  changeTaskDateModal: {
    visible: false,
    dateType: "assigned",
  },
  changeTaskAssigneeModal: {
    visible: false,
  },
  newWorkspaceModal: {
    visible: false,
  },
  newTeamModal: {
    visible: false,
  },
  searchTaskModal: {
    visible: false,
  },
  dialogModal: {
    visible: false,
  },
  reminderListModal: {
    visible: false,
  },
  newReminderModal: {
    visible: false,
  },
  datePickerModal: {
    visible: false,
  },
  workspaceMemberInviteModal: {
    visible: false,
  },
  addMemberToTeamModal: {
    visible: false,
  },
  notificationPermissionModal: {
    visible: false,
  },
  taskOverviewModal: {
    visible: false,
  },
  teamPickerModal: {
    visible: false,
  },
  workspacePickerModal: {
    visible: false,
  },
  newTaskBoardModal: {
    visible: false,
  },
  basicTextInputModal: {
    visible: false,
  },
  taskTaskBoardAssignModal: {
    visible: false,
  },
  topicPickerModal: {
    visible: false,
  },
  teamMemberPickerModal: {
    visible: false,
  },
  teamWorkflowStatusPickerModal: {
    visible: false,
  },
} as {
  loginWith2FaMailModal: null | LoginWith2FaMailModalState;
  loadingModal: null | ModalState;
  notFoundModal: null | NotFoundModalState;
  newTaskModal: null | NewTaskModalState;
  teamOptionsModal: null | ModalState;
  newTopicModal: null | NewTopicModalState;
  changeTaskWorkflowStatusModal: null | ChangeTaskWorkflowStatusModalState;
  changeTaskTopicModal: null | ChangeTaskTopicModalState;
  changeTaskDateModal: null | ChangeTaskDateModalState;
  changeTaskAssigneeModal: null | ChangeTaskAssigneeModalState;
  newWorkspaceModal: null | ModalState;
  newTeamModal: null | ModalState;
  searchTaskModal: null | SearchTaskModalState;
  dialogModal: null | DialogModalState;
  reminderListModal: null | ReminderListModalState;
  newReminderModal: null | NewReminderModalState;
  datePickerModal: null | DatePickerModalState;
  workspaceMemberInviteModal: null | WorkspaceMemberInviteModalState;
  addMemberToTeamModal: null | AddMemberToTeamModalState;
  notificationPermissionModal: null | ModalState;
  taskOverviewModal: null | TaskOverviewModalState;
  teamPickerModal: null | TeamPickerModalState;
  workspacePickerModal: null | WorkspacePickerModalState;
  newTaskBoardModal: null | NewTaskBoardModalState;
  basicTextInputModal: null | BasicTextInputModalState;
  taskTaskBoardAssignModal: null | TaskTaskBoardAssignModalState;
  topicPickerModal: null | TopicPickerModalState;
  teamMemberPickerModal: null | TeamMemberPickerModalState;
  teamWorkflowStatusPickerModal: null | TeamWorkflowStatusPickerModalState;
};

const slice = createSlice({
  name: "modal",
  initialState,
  reducers: {
    changeLoginWith2FaMailModalVisibility: (state, action: PayloadAction<LoginWith2FaMailModalState>) => {
      state.loginWith2FaMailModal = action.payload;
    },
    changeLoadingModalVisibility: (state, action: PayloadAction<ModalState>) => {
      state.loadingModal = action.payload;
    },
    popNotFoundModal: (state, action: PayloadAction<NotFoundModalState>) => {
      state.notFoundModal = {
        ...state.notFoundModal,
        ...action.payload,
        visible: true,
      };
    },
    closeNotFoundModal: (state, action: PayloadAction<void>) => {
      state.notFoundModal = initialState.notFoundModal;
    },
    popNewTaskModal: (state, action: PayloadAction<NewTaskModalState>) => {
      state.newTaskModal = { ...action.payload, visible: true };
    },
    popNewTaskWithSubtaskRelationModal: (state, action: PayloadAction<NewTaskModalState>) => {
      state.newTaskModal = { ...action.payload, visible: true };
    },
    closeNewTaskModal: (state, action: PayloadAction<void>) => {
      state.newTaskModal = initialState.newTaskModal;
    },
    popTeamOptionsModal: (state, action: PayloadAction<void>) => {
      state.teamOptionsModal = { visible: true };
    },
    closeTeamOptionsModal: (state, action: PayloadAction<void>) => {
      state.teamOptionsModal = { visible: false };
    },
    popNewTopicModal: (state, action: PayloadAction<NewTopicModalState>) => {
      state.newTopicModal = { ...action.payload, visible: true };
    },
    closeNewTopicModal: (state, action: PayloadAction<void>) => {
      state.newTopicModal = { visible: false };
    },
    popChangeTaskWorkflowStatusModal: (state, action: PayloadAction<ChangeTaskWorkflowStatusModalState>) => {
      state.changeTaskWorkflowStatusModal = {
        visible: true,
        task: action.payload.task,
      };
    },
    closeChangeTaskWorkflowStatusModal: (state, action: PayloadAction<void>) => {
      state.changeTaskWorkflowStatusModal = { visible: false };
    },

    popChangeTaskTopicModal: (state, action: PayloadAction<ChangeTaskTopicModalState>) => {
      state.changeTaskTopicModal = { visible: true, task: action.payload.task };
    },
    closeChangeTaskTopicModal: (state, action: PayloadAction<void>) => {
      state.changeTaskTopicModal = { visible: false };
    },

    popChangeTaskDateModal: (state, action: PayloadAction<ChangeTaskDateModalState>) => {
      state.changeTaskDateModal = { ...action.payload, visible: true };
    },
    closeChangeTaskDateModal: (state, action: PayloadAction<void>) => {
      state.changeTaskDateModal = initialState.changeTaskDateModal;
    },

    popChangeTaskAssigneeModal: (state, action: PayloadAction<ChangeTaskAssigneeModalState>) => {
      state.changeTaskAssigneeModal = { ...action.payload, visible: true };
    },
    closeChangeTaskAssigneeModal: (state, action: PayloadAction<void>) => {
      state.changeTaskAssigneeModal = initialState.changeTaskAssigneeModal;
    },

    popNewWorkspaceModal: (state, action: PayloadAction<void>) => {
      state.newWorkspaceModal = { visible: true };
    },
    closeNewWorkspaceModal: (state, action: PayloadAction<void>) => {
      state.newWorkspaceModal = initialState.newWorkspaceModal;
    },

    popNewTeamModal: (state, action: PayloadAction<void>) => {
      state.newTeamModal = { visible: true };
    },
    closeNewTeamModal: (state, action: PayloadAction<void>) => {
      state.newTeamModal = initialState.newTeamModal;
    },

    popDialogModal: (state, action: PayloadAction<DialogModalState>) => {
      state.dialogModal = { ...action.payload, visible: true };
    },
    closeDialogModal: (state, action: PayloadAction<void>) => {
      state.dialogModal = initialState.dialogModal;
    },

    popSearchTaskModal: (
      state,
      action: PayloadAction<{
        workspaceId: string;
        teamId: string;
        onSelect: (task: TaskSearchResultDto) => void;
      }>
    ) => {
      state.searchTaskModal = { visible: true, ...action.payload };
    },
    closeSearchTaskModal: (state, action: PayloadAction<void>) => {
      state.searchTaskModal = initialState.searchTaskModal;
    },

    popReminderListModal: (state, action: PayloadAction<ReminderListModalState>) => {
      state.reminderListModal = { ...action.payload, visible: true };
    },
    closeReminderListModal: (state, action: PayloadAction<void>) => {
      state.reminderListModal = initialState.reminderListModal;
    },

    popNewReminderModal: (state, action: PayloadAction<NewReminderModalState>) => {
      state.newReminderModal = { ...action.payload, visible: true };
    },
    closeNewReminderModal: (state, action: PayloadAction<void>) => {
      state.newReminderModal = initialState.newReminderModal;
    },

    popDatePickerModal: (state, action: PayloadAction<DatePickerModalState>) => {
      state.datePickerModal = { ...action.payload, visible: true };
    },
    closeDatePickerModal: (state, action: PayloadAction<void>) => {
      state.datePickerModal = initialState.datePickerModal;
    },

    popWorkspaceMemberInviteModal: (state, action: PayloadAction<WorkspaceMemberInviteModalState>) => {
      state.workspaceMemberInviteModal = { ...action.payload, visible: true };
    },
    closeWorkspaceMemberInviteModal: (state, action: PayloadAction<void>) => {
      state.workspaceMemberInviteModal = initialState.workspaceMemberInviteModal;
    },

    popAddMemberToTeamModal: (state, action: PayloadAction<AddMemberToTeamModalState>) => {
      state.addMemberToTeamModal = { ...action.payload, visible: true };
    },
    closeAddMemberToTeamModal: (state, action: PayloadAction<void>) => {
      state.addMemberToTeamModal = initialState.addMemberToTeamModal;
    },

    popNotificationPermissionModal: (state, action: PayloadAction<void>) => {
      state.notificationPermissionModal = { visible: true };
    },
    closeNotificationPermissionModal: (state, action: PayloadAction<void>) => {
      state.notificationPermissionModal = initialState.notificationPermissionModal;
    },

    popTaskOverviewModal: (state, action: PayloadAction<TaskOverviewModalState>) => {
      state.taskOverviewModal = { ...action.payload, visible: true };
    },
    closeTaskOverviewModal: (state, action: PayloadAction<void>) => {
      state.taskOverviewModal = initialState.taskOverviewModal;
    },

    popTeamPickerModal: (state, action: PayloadAction<TeamPickerModalState>) => {
      state.teamPickerModal = { ...action.payload, visible: true };
    },
    closeTeamPickerModal: (state, action: PayloadAction<void>) => {
      state.teamPickerModal = initialState.teamPickerModal;
    },

    popWorkspacePickerModal: (state, action: PayloadAction<WorkspacePickerModalState>) => {
      state.workspacePickerModal = { ...action.payload, visible: true };
    },
    closeWorkspacePickerModal: (state, action: PayloadAction<void>) => {
      state.workspacePickerModal = initialState.workspacePickerModal;
    },

    popNewTaskBoardModal: (state, action: PayloadAction<NewTaskBoardModalState>) => {
      state.newTaskBoardModal = { ...action.payload, visible: true };
    },
    closeNewTaskBoardModal: (state, action: PayloadAction<void>) => {
      state.newTaskBoardModal = initialState.newTaskBoardModal;
    },

    popBasicTextInputModal: (state, action: PayloadAction<BasicTextInputModalState>) => {
      state.basicTextInputModal = { ...action.payload, visible: true };
    },
    closeBasicTextInputModal: (state, action: PayloadAction<void>) => {
      state.basicTextInputModal = initialState.basicTextInputModal;
    },

    popTaskTaskBoardAssignModal: (state, action: PayloadAction<TaskTaskBoardAssignModalState>) => {
      state.taskTaskBoardAssignModal = { ...action.payload, visible: true };
    },
    closeTaskTaskBoardAssignModal: (state, action: PayloadAction<void>) => {
      state.taskTaskBoardAssignModal = initialState.taskTaskBoardAssignModal;
    },

    popTopicPickerModal: (state, action: PayloadAction<TopicPickerModalState>) => {
      state.topicPickerModal = { ...action.payload, visible: true };
    },
    closeTopicPickerModal: (state, action: PayloadAction<void>) => {
      state.topicPickerModal = initialState.topicPickerModal;
    },

    popTeamMemberPickerModal: (state, action: PayloadAction<TeamMemberPickerModalState>) => {
      state.teamMemberPickerModal = { ...action.payload, visible: true };
    },
    closeTeamMemberPickerModal: (state, action: PayloadAction<void>) => {
      state.teamMemberPickerModal = initialState.teamMemberPickerModal;
    },

    popTeamWorkflowStatusPickerModal: (state, action: PayloadAction<TeamWorkflowStatusPickerModalState>) => {
      state.teamWorkflowStatusPickerModal = { ...action.payload, visible: true };
    },
    closeTeamWorkflowStatusPickerModal: (state, action: PayloadAction<void>) => {
      state.teamWorkflowStatusPickerModal = initialState.teamWorkflowStatusPickerModal;
    },

    resetModals: () => initialState,
  },
  extraReducers: (builder) => {
    builder
      .addMatcher(accountApi.endpoints.me.matchPending, (state, action) => {
        state.loadingModal = { visible: true };
      })
      .addMatcher(accountApi.endpoints.me.matchFulfilled, (state, action) => {
        state.loadingModal = { visible: false };
      })
      .addMatcher(accountApi.endpoints.me.matchRejected, (state, action) => {
        state.loadingModal = { visible: false };
      });
  },
});

export const {
  changeLoginWith2FaMailModalVisibility,
  changeLoadingModalVisibility,
  popNotFoundModal,
  closeNotFoundModal,
  popNewTaskModal,
  popNewTaskWithSubtaskRelationModal,
  closeNewTaskModal,
  popTeamOptionsModal,
  closeTeamOptionsModal,
  popNewTopicModal,
  closeNewTopicModal,
  popChangeTaskWorkflowStatusModal,
  closeChangeTaskWorkflowStatusModal,
  popChangeTaskTopicModal,
  closeChangeTaskTopicModal,
  popChangeTaskDateModal,
  closeChangeTaskDateModal,
  popChangeTaskAssigneeModal,
  closeChangeTaskAssigneeModal,
  popNewWorkspaceModal,
  closeNewWorkspaceModal,
  popNewTeamModal,
  closeNewTeamModal,
  popSearchTaskModal,
  closeSearchTaskModal,
  popDialogModal,
  closeDialogModal,
  popReminderListModal,
  closeReminderListModal,
  popNewReminderModal,
  closeNewReminderModal,
  popDatePickerModal,
  closeDatePickerModal,
  popWorkspaceMemberInviteModal,
  closeWorkspaceMemberInviteModal,
  popAddMemberToTeamModal,
  closeAddMemberToTeamModal,
  popNotificationPermissionModal,
  closeNotificationPermissionModal,
  popTaskOverviewModal,
  closeTaskOverviewModal,
  popTeamPickerModal,
  closeTeamPickerModal,
  popWorkspacePickerModal,
  closeWorkspacePickerModal,
  popNewTaskBoardModal,
  closeNewTaskBoardModal,
  popBasicTextInputModal,
  closeBasicTextInputModal,
  popTaskTaskBoardAssignModal,
  closeTaskTaskBoardAssignModal,
  popTopicPickerModal,
  closeTopicPickerModal,
  popTeamMemberPickerModal,
  closeTeamMemberPickerModal,
  popTeamWorkflowStatusPickerModal,
  closeTeamWorkflowStatusPickerModal,
  resetModals,
} = slice.actions;
export default slice.reducer;

export const selectAnyModalVisible = (state: RootState) => {
  const modalState = state.modal || {};
  return Object.values(modalState)
    ?.map((modalState) => modalState?.visible || false)
    ?.reduce((prev, curr) => prev || curr);
};

export const selectLoginWith2FaMailModalVisible = (state: RootState) => state.modal.loginWith2FaMailModal?.visible;
export const selectLoginWith2FaMailModalAutoSubmitEmail = (state: RootState) =>
  state.modal.loginWith2FaMailModal?.autoSubmitEmail;

export const selectLoadingModalVisible = (state: RootState) => state.modal.loadingModal?.visible;

export const selectNotFoundModalVisible = (state: RootState) => state.modal.notFoundModal?.visible;
export const selectNotFoundModalTitle = (state: RootState) => state.modal.notFoundModal?.title;
export const selectNotFoundModalLabel = (state: RootState) => state.modal.notFoundModal?.label;
export const selectNotFoundModalImgSrc = (state: RootState) => state.modal.notFoundModal?.imgSrc;
export const selectNotFoundModalImgAlt = (state: RootState) => state.modal.notFoundModal?.imgAlt;

export const selectNewTaskModalVisible = (state: RootState) => state.modal.newTaskModal?.visible;
export const selectNewTaskModalSubTaskOf = (state: RootState) => state.modal.newTaskModal?.subTaskOf;
export const selectNewTaskModalSubTaskOfLabel = (state: RootState) => state.modal.newTaskModal?.subTaskOfLabel;
export const selectNewTaskModalWorkspace = (state: RootState) => state.modal.newTaskModal?.workspace;
export const selectNewTaskModalTeam = (state: RootState) => state.modal.newTaskModal?.team;

export const selectTeamOptionsModalVisible = (state: RootState) => state.modal.teamOptionsModal?.visible;

export const selectNewTopicModalVisible = (state: RootState) => state.modal.newTopicModal?.visible;
export const selectNewTopicModalWorkspace = (state: RootState) => state.modal.newTopicModal?.workspace;
export const selectNewTopicModalTeam = (state: RootState) => state.modal.newTopicModal?.team;

export const selectChangeTaskWorkflowStatusModalVisible = (state: RootState) =>
  state.modal.changeTaskWorkflowStatusModal?.visible;
export const selectChangeTaskWorkflowStatusModalTask = (state: RootState) => state.modal.changeTaskWorkflowStatusModal?.task;

export const selectChangeTaskTopicModalVisible = (state: RootState) => state.modal.changeTaskTopicModal?.visible;
export const selectChangeTaskTopicModalTaskId = (state: RootState) => state.modal.changeTaskTopicModal?.task?.taskId;
export const selectChangeTaskTopicModalTaskCurrentTopicId = (state: RootState) => state.modal.changeTaskTopicModal?.task?.topicId;

export const selectChangeTaskDateModalVisible = (state: RootState) => state.modal.changeTaskDateModal?.visible;
export const selectChangeTaskDateModalTaskId = (state: RootState) => state.modal.changeTaskDateModal?.task?.taskId;
export const selectChangeTaskDateModalTaskCurrentAssignedDate = (state: RootState) =>
  state.modal.changeTaskDateModal?.task?.assignedDate;
export const selectChangeTaskDateModalTaskCurrentDueDate = (state: RootState) => state.modal.changeTaskDateModal?.task?.dueDate;
export const selectChangeTaskDateModalHasPreciseAssignedDate = (state: RootState) =>
  state.modal.changeTaskDateModal?.task?.hasPreciseAssignedDate;
export const selectChangeTaskDateModalHasPreciseDueDate = (state: RootState) =>
  state.modal.changeTaskDateModal?.task?.hasPreciseDueDate;

export const selectChangeTaskAssigneeModalVisible = (state: RootState) => state.modal.changeTaskAssigneeModal?.visible;
export const selectChangeTaskAssigneeModalTaskId = (state: RootState) => state.modal.changeTaskAssigneeModal?.task?.taskId;
export const selectChangeTaskAssigneeModalTaskCurrentAssigneeId = (state: RootState) =>
  state.modal.changeTaskAssigneeModal?.task?.assignedTo;
export const selectChangeTaskAssigneeModalTaskCurrentTeamId = (state: RootState) =>
  state.modal.changeTaskAssigneeModal?.task?.teamId;

export const selectNewWorkspaceModalVisible = (state: RootState) => state.modal.newWorkspaceModal?.visible;

export const selectNewTeamModalVisible = (state: RootState) => state.modal.newTeamModal?.visible;

export const selectSearchTaskModalVisible = (state: RootState) => state.modal.searchTaskModal?.visible;
export const selectSearchTaskModalWorkspaceId = (state: RootState) => state.modal.searchTaskModal?.workspaceId;
export const selectSearchTaskModalTeamId = (state: RootState) => state.modal.searchTaskModal?.teamId;
export const selectSearchTaskModalOnSelect = (state: RootState) => state.modal.searchTaskModal?.onSelect;

export const selectDialogModalVisible = (state: RootState) => state.modal.dialogModal?.visible;
export const selectDialogModalTitle = (state: RootState) => state.modal.dialogModal?.title;
export const selectDialogModalContent = (state: RootState) => state.modal.dialogModal?.content;
export const selectDialogModalHtmlContent = (state: RootState) => state.modal.dialogModal?.htmlContent;
export const selectDialogModalCloseButtonLabel = (state: RootState) => state.modal.dialogModal?.closeButtonLabel;
export const selectDialogModalConfirmButtonLabel = (state: RootState) => state.modal.dialogModal?.confirmButtonLabel;
export const selectDialogModalOnConfirm = (state: RootState) => state.modal.dialogModal?.onConfirm;
export const selectDialogModalOnClose = (state: RootState) => state.modal.dialogModal?.onClose;

export const selectReminderListModalVisible = (state: RootState) => state.modal.reminderListModal?.visible;
export const selectReminderListModalTask = (state: RootState) => state.modal.reminderListModal?.task;

export const selectNewReminderModalVisible = (state: RootState) => state.modal.newReminderModal?.visible;
export const selectNewReminderModalTask = (state: RootState) => state.modal.newReminderModal?.task;

export const selectDatePickerModalVisible = (state: RootState) => state.modal.datePickerModal?.visible;
export const selectDatePickerModalInitialDate = (state: RootState) => state.modal.datePickerModal?.initialDate;
export const selectDatePickerModalOnDateChange = (state: RootState) => state.modal.datePickerModal?.onDateChange;

export const selectWorkspaceMemberInviteModalVisible = (state: RootState) => state.modal.workspaceMemberInviteModal?.visible;
export const selectWorkspaceMemberInviteModalWorkspaceId = (state: RootState) =>
  state.modal.workspaceMemberInviteModal?.workspaceId;

export const selectAddMemberToTeamModalVisible = (state: RootState) => state.modal.addMemberToTeamModal?.visible;
export const selectAddMemberToTeamModalWorkspace = (state: RootState) => state.modal.addMemberToTeamModal?.workspace;
export const selectAddMemberToTeamModalTeam = (state: RootState) => state.modal.addMemberToTeamModal?.team;

export const selectNotificationPermissionModalVisible = (state: RootState) => state.modal.notificationPermissionModal?.visible;

export const selectTaskOverviewModalVisible = (state: RootState) => state.modal.taskOverviewModal?.visible;
export const selectTaskOverviewModalTaskTag = (state: RootState) => state.modal.taskOverviewModal?.taskTag;
export const selectTaskOverviewModalWorkspaceName = (state: RootState) => state.modal.taskOverviewModal?.workspaceName;

export const selectTeamPickerModalVisible = (state: RootState) => state.modal.teamPickerModal?.visible;
export const selectTeamPickerModalWorkspaceId = (state: RootState) => state.modal.teamPickerModal?.workspaceId;
export const selectTeamPickerModalOnPick = (state: RootState) => state.modal.teamPickerModal?.onPick;

export const selectWorkspacePickerModalVisible = (state: RootState) => state.modal.workspacePickerModal?.visible;
export const selectWorkspacePickerModalCurrentWorkspaceId = (state: RootState) =>
  state.modal.workspacePickerModal?.currentWorkspaceId;
export const selectWorkspacePickerModalOnPick = (state: RootState) => state.modal.workspacePickerModal?.onPick;

export const selectNewTaskBoardModalVisible = (state: RootState) => state.modal.newTaskBoardModal?.visible;
export const selectNewTaskBoardModalWorkspace = (state: RootState) => state.modal.newTaskBoardModal?.workspace;
export const selectNewTaskBoardModalTeam = (state: RootState) => state.modal.newTaskBoardModal?.team;

export const selectBasicTextInputModalVisible = (state: RootState) => state.modal.basicTextInputModal?.visible;
export const selectBasicTextInputModalTitle = (state: RootState) => state.modal.basicTextInputModal?.title;
export const selectBasicTextInputModalInfoText = (state: RootState) => state.modal.basicTextInputModal?.infoText;
export const selectBasicTextInputModalInfoInitialText = (state: RootState) => state.modal.basicTextInputModal?.initialText;
export const selectBasicTextInputModalInfoOnSubmit = (state: RootState) => state.modal.basicTextInputModal?.onSubmit;

export const selectTaskTaskBoardAssignModalVisible = (state: RootState) => state.modal.taskTaskBoardAssignModal?.visible;
export const selectTaskTaskBoardAssignModalTaskId = (state: RootState) => state.modal.taskTaskBoardAssignModal?.taskId;

export const selectTopicPickerModalVisible = (state: RootState) => state.modal.topicPickerModal?.visible;
export const selectTopicPickerModalWorkspace = (state: RootState) => state.modal.topicPickerModal?.workspace;
export const selectTopicPickerModalTeam = (state: RootState) => state.modal.topicPickerModal?.team;
export const selectTopicPickerModalMultiple = (state: RootState) => state.modal.topicPickerModal?.multiple;
export const selectTopicPickerModalInitialSelectionOnMultiple = (state: RootState) =>
  state.modal.topicPickerModal?.initialSelectionOnMultiple;
export const selectTopicPickerModalOnPick = (state: RootState) => state.modal.topicPickerModal?.onPick;

export const selectTeamMemberPickerModalVisible = (state: RootState) => state.modal.teamMemberPickerModal?.visible;
export const selectTeamMemberPickerModalTeamId = (state: RootState) => state.modal.teamMemberPickerModal?.teamId;
export const selectTeamMemberPickerModalWorkspaceId = (state: RootState) => state.modal.teamMemberPickerModal?.workspaceId;
export const selectTeamMemberPickerModalMultiple = (state: RootState) => state.modal.teamMemberPickerModal?.multiple;
export const selectTeamMemberPickerModalInitialSelectionOnMultiple = (state: RootState) =>
  state.modal.teamMemberPickerModal?.initialSelectionOnMultiple;
export const selectTeamMemberPickerModalOnPick = (state: RootState) => state.modal.teamMemberPickerModal?.onPick;

export const selectTeamWorkflowStatusPickerModalVisible = (state: RootState) =>
  state.modal.teamWorkflowStatusPickerModal?.visible;
export const selectTeamWorkflowStatusPickerModalTeamId = (state: RootState) => state.modal.teamWorkflowStatusPickerModal?.teamId;
export const selectTeamWorkflowStatusPickerModalWorkspaceId = (state: RootState) =>
  state.modal.teamWorkflowStatusPickerModal?.workspaceId;
export const selectTeamWorkflowStatusPickerModalMultiple = (state: RootState) =>
  state.modal.teamWorkflowStatusPickerModal?.multiple;
export const selectTeamWorkflowStatusPickerModalInitialSelectionOnMultiple = (state: RootState) =>
  state.modal.teamWorkflowStatusPickerModal?.initialSelectionOnMultiple;
export const selectTeamWorkflowStatusPickerModalOnPick = (state: RootState) => state.modal.teamWorkflowStatusPickerModal?.onPick;
