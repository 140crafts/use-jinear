import { TaskDto } from "@/model/be/jinear-core";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import ModalState, {
  AddMemberToTeamModalState,
  BasicTextInputModalState,
  CalendarExternalEventViewModalState,
  CalendarShareEventsModalState,
  ChangeTaskAssigneeModalState,
  ChangeTaskDateModalState,
  ChangeTaskTopicModalState,
  ChangeTaskWorkflowStatusModalState,
  ChannelListModalState,
  ChannelSettingsModalState,
  ConversationSettingsModalState,
  DatePickerModalState,
  DialogModalState,
  IntegrationFeedItemDetailModalState,
  LoginWith2FaMailModalState,
  NewCalendarIntegrationModalState,
  NewChannelModalState,
  NewConversationModalState,
  NewMailIntegrationModalState,
  NewProjectModalState,
  NewReminderModalState,
  NewTaskBoardModalState,
  NewTaskModalState,
  NewTeamModalState,
  NewTopicModalState,
  NotFoundModalState,
  NotificationPermissionModalState, ProjectPrioritySelectModalState, ProjectStateSelectModalState,
  ReminderListModalState,
  SearchTaskModalState,
  TaskBoardPickerModalState,
  TaskOverviewModalState,
  TaskTaskBoardAssignModalState,
  TeamMemberPickerModalState,
  TeamPickerModalState, TeamPickerModalV2State,
  TeamWorkflowStatusPickerModalState,
  TopicPickerModalState,
  UpgradeWorkspacePlanModalState,
  WorkspaceMemberInviteModalState,
  WorkspaceMemberPickerModalState,
  WorkspacePickerModalState
} from "model/app/store/modal/modalState";
import { accountApi } from "@/api/accountApi";
import { RootState } from "../store";

const initialState = {
  loginWith2FaMailModal: {
    visible: false,
    rerouteDisabled: false
  },
  loadingModal: {
    visible: false
  },
  notFoundModal: {
    visible: false,
    imgSrc: "/images/gif/sad-doge.gif",
    imgAlt: "Illustration for not found modal. Sad doge gif.",
    title: undefined,
    label: undefined
  },
  newTaskModal: {
    visible: false
  },
  newTopicModal: {
    visible: false
  },
  changeTaskWorkflowStatusModal: {
    visible: false
  },
  changeTaskTopicModal: {
    visible: false
  },
  changeTaskDateModal: {
    visible: false,
    dateType: "assigned"
  },
  changeTaskAssigneeModal: {
    visible: false
  },
  newWorkspaceModal: {
    visible: false
  },
  newTeamModal: {
    visible: false,
    workspace: undefined
  },
  searchTaskModal: {
    visible: false
  },
  dialogModal: {
    visible: false
  },
  reminderListModal: {
    visible: false
  },
  newReminderModal: {
    visible: false
  },
  datePickerModal: {
    visible: false
  },
  workspaceMemberInviteModal: {
    visible: false
  },
  addMemberToTeamModal: {
    visible: false
  },
  notificationPermissionModal: {
    visible: false,
    platform: "web"
  },
  taskOverviewModal: {
    visible: false
  },
  teamPickerModal: {
    visible: false
  },
  workspacePickerModal: {
    visible: false
  },
  newTaskBoardModal: {
    visible: false
  },
  basicTextInputModal: {
    visible: false
  },
  taskTaskBoardAssignModal: {
    visible: false
  },
  topicPickerModal: {
    visible: false
  },
  teamMemberPickerModal: {
    visible: false
  },
  teamWorkflowStatusPickerModal: {
    visible: false
  },
  taskBoardPickerModal: {
    visible: false
  },
  upgradeWorkspacePlanModal: {
    visible: false
  },
  accountProfileModal: {
    visible: false
  },
  workspaceSwitchModal: {
    visible: false
  },
  menuMoreActionModal: {
    visible: false
  },
  deviceOfflineModal: {
    visible: false
  },
  newMailIntegrationModal: {
    visible: false
  },
  integrationFeedItemDetailModal: {
    visible: false
  },
  workspaceMemberPickerModal: {
    visible: false
  },
  newCalendarIntegrationModal: {
    visible: false
  },
  calendarExternalEventViewModal: {
    visible: false
  },
  calendarShareEventsModal: {
    visible: false
  },
  channelSettingsModal: {
    visible: false
  },
  newChannelModal: {
    visible: false
  },
  conversationSettingsModal: {
    visible: false
  },
  channelListModal: {
    visible: false
  },
  newConversationModal: {
    visible: false
  },
  installPwaInstructionsModal: {
    visible: false
  },
  newProjectModal: {
    visible: false
  },
  teamPickerModalV2: {
    visible: false
  },
  projectPrioritySelectModal: {
    visible: false
  },
  projectStateSelectModal: {
    visible: false
  }
} as {
  loginWith2FaMailModal: null | LoginWith2FaMailModalState;
  loadingModal: null | ModalState;
  notFoundModal: null | NotFoundModalState;
  newTaskModal: null | NewTaskModalState;
  newTopicModal: null | NewTopicModalState;
  changeTaskWorkflowStatusModal: null | ChangeTaskWorkflowStatusModalState;
  changeTaskTopicModal: null | ChangeTaskTopicModalState;
  changeTaskDateModal: null | ChangeTaskDateModalState;
  changeTaskAssigneeModal: null | ChangeTaskAssigneeModalState;
  newWorkspaceModal: null | ModalState;
  newTeamModal: null | NewTeamModalState;
  searchTaskModal: null | SearchTaskModalState;
  dialogModal: null | DialogModalState;
  reminderListModal: null | ReminderListModalState;
  newReminderModal: null | NewReminderModalState;
  datePickerModal: null | DatePickerModalState;
  workspaceMemberInviteModal: null | WorkspaceMemberInviteModalState;
  addMemberToTeamModal: null | AddMemberToTeamModalState;
  notificationPermissionModal: null | NotificationPermissionModalState;
  taskOverviewModal: null | TaskOverviewModalState;
  teamPickerModal: null | TeamPickerModalState;
  workspacePickerModal: null | WorkspacePickerModalState;
  newTaskBoardModal: null | NewTaskBoardModalState;
  basicTextInputModal: null | BasicTextInputModalState;
  taskTaskBoardAssignModal: null | TaskTaskBoardAssignModalState;
  topicPickerModal: null | TopicPickerModalState;
  teamMemberPickerModal: null | TeamMemberPickerModalState;
  teamWorkflowStatusPickerModal: null | TeamWorkflowStatusPickerModalState;
  taskBoardPickerModal: null | TaskBoardPickerModalState;
  upgradeWorkspacePlanModal: null | UpgradeWorkspacePlanModalState;
  accountProfileModal: null | ModalState;
  workspaceSwitchModal: null | ModalState;
  menuMoreActionModal: null | ModalState;
  deviceOfflineModal: null | ModalState;
  newMailIntegrationModal: null | NewMailIntegrationModalState;
  integrationFeedItemDetailModal: null | IntegrationFeedItemDetailModalState;
  workspaceMemberPickerModal: null | WorkspaceMemberPickerModalState;
  newCalendarIntegrationModal: null | NewCalendarIntegrationModalState;
  calendarExternalEventViewModal: null | CalendarExternalEventViewModalState;
  calendarShareEventsModal: null | CalendarShareEventsModalState;
  channelSettingsModal: null | ChannelSettingsModalState;
  newChannelModal: null | NewChannelModalState;
  conversationSettingsModal: null | ConversationSettingsModalState;
  channelListModal: null | ChannelListModalState;
  newConversationModal: null | NewConversationModalState;
  installPwaInstructionsModal: null | ModalState;
  newProjectModal: null | NewProjectModalState;
  teamPickerModalV2: null | TeamPickerModalV2State;
  projectPrioritySelectModal: null | ProjectPrioritySelectModalState;
  projectStateSelectModal: null | ProjectStateSelectModalState;
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
        visible: true
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
    popNewTopicModal: (state, action: PayloadAction<NewTopicModalState>) => {
      state.newTopicModal = { ...action.payload, visible: true };
    },
    closeNewTopicModal: (state, action: PayloadAction<void>) => {
      state.newTopicModal = { visible: false };
    },
    popChangeTaskWorkflowStatusModal: (state, action: PayloadAction<ChangeTaskWorkflowStatusModalState>) => {
      state.changeTaskWorkflowStatusModal = {
        visible: true,
        task: action.payload.task
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

    popNewTeamModal: (state, action: PayloadAction<NewTeamModalState>) => {
      state.newTeamModal = { visible: true, workspace: action.payload.workspace };
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
        onSelect: (task: TaskDto) => void;
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
    popNotificationPermissionModal: (state, action: PayloadAction<NotificationPermissionModalState>) => {
      state.notificationPermissionModal = { ...action.payload, visible: true };
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

    popBoardPickerModal: (state, action: PayloadAction<TaskBoardPickerModalState>) => {
      state.taskBoardPickerModal = { ...action.payload, visible: true };
    },
    closeBoardPickerModal: (state, action: PayloadAction<void>) => {
      state.taskBoardPickerModal = initialState.taskBoardPickerModal;
    },

    popUpgradeWorkspacePlanModal: (state, action: PayloadAction<UpgradeWorkspacePlanModalState>) => {
      state.upgradeWorkspacePlanModal = { ...action.payload, visible: true };
    },
    closeUpgradeWorkspacePlanModal: (state, action: PayloadAction<void>) => {
      state.upgradeWorkspacePlanModal = initialState.upgradeWorkspacePlanModal;
    },

    popAccountProfileModal: (state, action: PayloadAction<void>) => {
      state.accountProfileModal = { visible: true };
    },
    closeAccountProfileModal: (state, action: PayloadAction<void>) => {
      state.accountProfileModal = initialState.accountProfileModal;
    },

    popWorkspaceSwitchModal: (state, action: PayloadAction<void>) => {
      state.workspaceSwitchModal = { visible: true };
    },
    closeWorkspaceSwitchModal: (state, action: PayloadAction<void>) => {
      state.workspaceSwitchModal = initialState.workspaceSwitchModal;
    },

    popMenuMoreActionModal: (state, action: PayloadAction<void>) => {
      state.menuMoreActionModal = { visible: true };
    },
    closeMenuMoreActionModal: (state, action: PayloadAction<void>) => {
      state.menuMoreActionModal = initialState.menuMoreActionModal;
    },

    popDeviceOfflineModal: (state, action: PayloadAction<void>) => {
      state.deviceOfflineModal = { visible: true };
    },
    closeDeviceOfflineModal: (state, action: PayloadAction<void>) => {
      state.deviceOfflineModal = initialState.deviceOfflineModal;
    },

    popNewMailIntegrationModal: (state, action: PayloadAction<NewMailIntegrationModalState>) => {
      state.newMailIntegrationModal = { visible: true, workspaceId: action.payload.workspaceId };
    },
    closeNewMailIntegrationModal: (state, action: PayloadAction<void>) => {
      state.newMailIntegrationModal = initialState.newMailIntegrationModal;
    },

    popIntegrationFeedItemDetailModal: (state, action: PayloadAction<IntegrationFeedItemDetailModalState>) => {
      state.integrationFeedItemDetailModal = { ...action.payload, visible: true };
    },
    closeIntegrationFeedItemDetailModal: (state, action: PayloadAction<void>) => {
      state.integrationFeedItemDetailModal = initialState.integrationFeedItemDetailModal;
    },

    popWorkspaceMemberPickerModal: (state, action: PayloadAction<WorkspaceMemberPickerModalState>) => {
      state.workspaceMemberPickerModal = { ...action.payload, visible: true };
    },
    closeWorkspaceMemberPickerModal: (state, action: PayloadAction<void>) => {
      state.workspaceMemberPickerModal = initialState.workspaceMemberPickerModal;
    },

    popNewCalendarIntegrationModal: (state, action: PayloadAction<NewCalendarIntegrationModalState>) => {
      state.newCalendarIntegrationModal = { visible: true, workspaceId: action.payload.workspaceId };
    },
    closeNewCalendarIntegrationModal: (state, action: PayloadAction<void>) => {
      state.newCalendarIntegrationModal = initialState.newMailIntegrationModal;
    },

    popCalendarExternalEventViewModal: (state, action: PayloadAction<CalendarExternalEventViewModalState>) => {
      state.calendarExternalEventViewModal = { ...action.payload, visible: true };
    },
    closeCalendarExternalEventViewModal: (state, action: PayloadAction<void>) => {
      state.calendarExternalEventViewModal = initialState.calendarExternalEventViewModal;
    },

    popCalendarShareEventsModal: (state, action: PayloadAction<CalendarShareEventsModalState>) => {
      state.calendarShareEventsModal = { ...action.payload, visible: true };
    },
    closeCalendarShareEventsModal: (state, action: PayloadAction<void>) => {
      state.calendarShareEventsModal = initialState.calendarShareEventsModal;
    },

    popChannelSettingsModal: (state, action: PayloadAction<ChannelSettingsModalState>) => {
      state.channelSettingsModal = { ...action.payload, visible: true };
    },
    closeChannelSettingsModal: (state, action: PayloadAction<void>) => {
      state.channelSettingsModal = initialState.channelSettingsModal;
    },

    popNewChannelModal: (state, action: PayloadAction<NewChannelModalState>) => {
      state.newChannelModal = { ...action.payload, visible: true };
    },
    closeNewChannelModal: (state, action: PayloadAction<void>) => {
      state.newChannelModal = initialState.newChannelModal;
    },

    popConversationSettingsModal: (state, action: PayloadAction<ConversationSettingsModalState>) => {
      state.conversationSettingsModal = { ...action.payload, visible: true };
    },
    closeConversationSettingsModal: (state, action: PayloadAction<void>) => {
      state.conversationSettingsModal = initialState.conversationSettingsModal;
    },

    popChannelListModal: (state, action: PayloadAction<ChannelListModalState>) => {
      state.channelListModal = { ...action.payload, visible: true };
    },
    closeChannelListModal: (state, action: PayloadAction<void>) => {
      state.channelListModal = initialState.channelListModal;
    },

    popNewConversationModal: (state, action: PayloadAction<NewConversationModalState>) => {
      state.newConversationModal = { ...action.payload, visible: true };
    },
    closeNewConversationModal: (state, action: PayloadAction<void>) => {
      state.newConversationModal = initialState.newConversationModal;
    },

    popInstallPwaInstructionsModal: (state, action: PayloadAction<void>) => {
      state.installPwaInstructionsModal = { visible: true };
    },
    closeInstallPwaInstructionsModal: (state, action: PayloadAction<void>) => {
      state.installPwaInstructionsModal = initialState.installPwaInstructionsModal;
    },

    popNewProjectModal: (state, action: PayloadAction<NewProjectModalState>) => {
      state.newProjectModal = { ...action.payload, visible: true };
    },
    closeNewProjectModal: (state, action: PayloadAction<void>) => {
      state.newProjectModal = initialState.newProjectModal;
    },

    popTeamPickerModalV2: (state, action: PayloadAction<TeamPickerModalV2State>) => {
      state.teamPickerModalV2 = { ...action.payload, visible: true };
    },
    closeTeamPickerModalV2: (state, action: PayloadAction<void>) => {
      state.teamPickerModalV2 = initialState.teamPickerModalV2;
    },

    popProjectPrioritySelectModal: (state, action: PayloadAction<ProjectPrioritySelectModalState>) => {
      state.projectPrioritySelectModal = { ...action.payload, visible: true };
    },
    closeProjectPrioritySelectModal: (state, action: PayloadAction<void>) => {
      state.projectPrioritySelectModal = initialState.projectPrioritySelectModal;
    },

    popProjectStateSelectModal: (state, action: PayloadAction<ProjectStateSelectModalState>) => {
      state.projectStateSelectModal = { ...action.payload, visible: true };
    },
    closeProjectStateSelectModal: (state, action: PayloadAction<void>) => {
      state.projectStateSelectModal = initialState.projectStateSelectModal;
    },

    resetModals: () => initialState
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
  }
});

export const {
  changeLoginWith2FaMailModalVisibility,
  changeLoadingModalVisibility,
  popNotFoundModal,
  closeNotFoundModal,
  popNewTaskModal,
  popNewTaskWithSubtaskRelationModal,
  closeNewTaskModal,
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
  popBoardPickerModal,
  closeBoardPickerModal,
  popUpgradeWorkspacePlanModal,
  closeUpgradeWorkspacePlanModal,
  popAccountProfileModal,
  closeAccountProfileModal,
  popWorkspaceSwitchModal,
  closeWorkspaceSwitchModal,
  popMenuMoreActionModal,
  closeMenuMoreActionModal,
  popDeviceOfflineModal,
  closeDeviceOfflineModal,
  popNewMailIntegrationModal,
  closeNewMailIntegrationModal,
  popIntegrationFeedItemDetailModal,
  closeIntegrationFeedItemDetailModal,
  popWorkspaceMemberPickerModal,
  closeWorkspaceMemberPickerModal,
  popNewCalendarIntegrationModal,
  closeNewCalendarIntegrationModal,
  popCalendarExternalEventViewModal,
  closeCalendarExternalEventViewModal,
  popCalendarShareEventsModal,
  closeCalendarShareEventsModal,
  popChannelSettingsModal,
  closeChannelSettingsModal,
  popNewChannelModal,
  closeNewChannelModal,
  popConversationSettingsModal,
  closeConversationSettingsModal,
  popChannelListModal,
  closeChannelListModal,
  popNewConversationModal,
  closeNewConversationModal,
  popInstallPwaInstructionsModal,
  closeInstallPwaInstructionsModal,
  popNewProjectModal,
  closeNewProjectModal,
  popTeamPickerModalV2,
  closeTeamPickerModalV2,
  popProjectPrioritySelectModal,
  closeProjectPrioritySelectModal,
  popProjectStateSelectModal,
  closeProjectStateSelectModal,
  resetModals
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
export const selectNewTaskModalInitialAssignedDate = (state: RootState) => state.modal.newTaskModal?.initialAssignedDate;
export const selectNewTaskModalInitialAssignedDateIsPrecise = (state: RootState) =>
  state.modal.newTaskModal?.initialAssignedDateIsPrecise;
export const selectNewTaskModalInitialDueDate = (state: RootState) => state.modal.newTaskModal?.initialDueDate;
export const selectNewTaskModalInitialDueDateIsPrecise = (state: RootState) => state.modal.newTaskModal?.initialDueDateIsPrecise;
export const selectNewTaskModalInitialRelatedFeedItemData = (state: RootState) =>
  state.modal.newTaskModal?.initialRelatedFeedItemData;

export const selectNewTopicModalVisible = (state: RootState) => state.modal.newTopicModal?.visible;
export const selectNewTopicModalWorkspace = (state: RootState) => state.modal.newTopicModal?.workspace;
export const selectNewTopicModalTeam = (state: RootState) => state.modal.newTopicModal?.team;

export const selectChangeTaskWorkflowStatusModalVisible = (state: RootState) =>
  state.modal.changeTaskWorkflowStatusModal?.visible;
export const selectChangeTaskWorkflowStatusModalTask = (state: RootState) => state.modal.changeTaskWorkflowStatusModal?.task;

export const selectChangeTaskTopicModalVisible = (state: RootState) => state.modal.changeTaskTopicModal?.visible;
export const selectChangeTaskTopicModalTaskId = (state: RootState) => state.modal.changeTaskTopicModal?.task?.taskId;
export const selectChangeTaskTopicModalTeamId = (state: RootState) => state.modal.changeTaskTopicModal?.task?.teamId;
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
export const selectNewTeamModalWorkspace = (state: RootState) => state.modal.newTeamModal?.workspace;

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
export const selectDatePickerModalTitle = (state: RootState) => state.modal.datePickerModal?.title;
export const selectDatePickerModalInitialDate = (state: RootState) => state.modal.datePickerModal?.initialDate;
export const selectDatePickerModalDateSpanStart = (state: RootState) => state.modal.datePickerModal?.dateSpanStart;
export const selectDatePickerModalDateSpanEnd = (state: RootState) => state.modal.datePickerModal?.dateSpanEnd;
export const selectDatePickerModalDisabledBefore = (state: RootState) => state.modal.datePickerModal?.disabledBefore;
export const selectDatePickerModalDisabledAfter = (state: RootState) => state.modal.datePickerModal?.disabledAfter;
export const selectDatePickerModalOnDateChange = (state: RootState) => state.modal.datePickerModal?.onDateChange;
export const selectDatePickerModalUnpickable = (state: RootState) => state.modal.datePickerModal?.unpickable;

export const selectWorkspaceMemberInviteModalVisible = (state: RootState) => state.modal.workspaceMemberInviteModal?.visible;
export const selectWorkspaceMemberInviteModalWorkspaceId = (state: RootState) =>
  state.modal.workspaceMemberInviteModal?.workspaceId;

export const selectAddMemberToTeamModalVisible = (state: RootState) => state.modal.addMemberToTeamModal?.visible;
export const selectAddMemberToTeamModalWorkspace = (state: RootState) => state.modal.addMemberToTeamModal?.workspace;
export const selectAddMemberToTeamModalTeam = (state: RootState) => state.modal.addMemberToTeamModal?.team;

export const selectNotificationPermissionModalVisible = (state: RootState) => state.modal.notificationPermissionModal?.visible;
export const selectNotificationPermissionModalPlatform = (state: RootState) => state.modal.notificationPermissionModal?.platform;

export const selectTaskOverviewModalVisible = (state: RootState) => state.modal.taskOverviewModal?.visible;
export const selectTaskOverviewModalTaskTag = (state: RootState) => state.modal.taskOverviewModal?.taskTag;
export const selectTaskOverviewModalWorkspaceName = (state: RootState) => state.modal.taskOverviewModal?.workspaceName;

export const selectTeamPickerModalVisible = (state: RootState) => state.modal.teamPickerModal?.visible;
export const selectTeamPickerModalWorkspaceId = (state: RootState) => state.modal.teamPickerModal?.workspaceId;
export const selectTeamPickerModalFilterActiveTeams = (state: RootState) => state.modal.teamPickerModal?.filterActiveTeams;
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
export const selectBasicTextInputModalInputType = (state: RootState) => state.modal.basicTextInputModal?.inputType;
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

export const selectTaskBoardPickerModalVisible = (state: RootState) => state.modal.taskBoardPickerModal?.visible;
export const selectTaskBoardPickerModalTeamId = (state: RootState) => state.modal.taskBoardPickerModal?.teamId;
export const selectTaskBoardPickerModalWorkspaceId = (state: RootState) => state.modal.taskBoardPickerModal?.workspaceId;
export const selectTaskBoardPickerModalMultiple = (state: RootState) => state.modal.taskBoardPickerModal?.multiple;
export const selectTaskBoardPickerModalInitialSelectionOnMultiple = (state: RootState) =>
  state.modal.taskBoardPickerModal?.initialSelectionOnMultiple;
export const selectTaskBoardPickerModalOnPick = (state: RootState) => state.modal.taskBoardPickerModal?.onPick;

export const selectUpgradeWorkspacePlanModalVisible = (state: RootState) => state.modal.upgradeWorkspacePlanModal?.visible;
export const selectUpgradeWorkspacePlanModalWorkspaceId = (state: RootState) =>
  state.modal.upgradeWorkspacePlanModal?.workspaceId;

export const selectAccountProfileModalVisible = (state: RootState) => state.modal.accountProfileModal?.visible;

export const selectWorkspaceSwitchModalVisible = (state: RootState) => state.modal.workspaceSwitchModal?.visible;

export const selectMenuMoreActionModalVisible = (state: RootState) => state.modal.menuMoreActionModal?.visible;

export const selectDeviceOfflineModal = (state: RootState) => state.modal.deviceOfflineModal?.visible;

export const selectNewMailIntegrationModalVisible = (state: RootState) => state.modal.newMailIntegrationModal?.visible;
export const selectNewMailIntegrationModalWorkspaceId = (state: RootState) => state.modal.newMailIntegrationModal?.workspaceId;

export const selectIntegrationFeedItemDetailModalVisible = (state: RootState) =>
  state.modal.integrationFeedItemDetailModal?.visible;
export const selectIntegrationFeedItemDetailModalWorkspace = (state: RootState) =>
  state.modal.integrationFeedItemDetailModal?.workspace;
export const selectIntegrationFeedItemDetailModalFeedId = (state: RootState) =>
  state.modal.integrationFeedItemDetailModal?.feedId;
export const selectIntegrationFeedItemDetailModalFeedItemId = (state: RootState) =>
  state.modal.integrationFeedItemDetailModal?.itemId;
export const selectIntegrationFeedItemDetailModalTitle = (state: RootState) => state.modal.integrationFeedItemDetailModal?.title;

export const selectWorkspaceMemberPickerModalVisible = (state: RootState) => state.modal.workspaceMemberPickerModal?.visible;
export const selectWorkspaceMemberPickerModalWorkspaceId = (state: RootState) =>
  state.modal.workspaceMemberPickerModal?.workspaceId;
export const selectWorkspaceMemberPickerModalMultiple = (state: RootState) => state.modal.workspaceMemberPickerModal?.multiple;
export const selectWorkspaceMemberPickerModalInitialSelectionOnMultiple = (state: RootState) =>
  state.modal.workspaceMemberPickerModal?.initialSelectionOnMultiple;
export const selectWorkspaceMemberPickerModalOnPick = (state: RootState) => state.modal.workspaceMemberPickerModal?.onPick;
export const selectWorkspaceMemberPickerModalDeselectable = (state: RootState) => state.modal.workspaceMemberPickerModal?.deselectable;
export const selectWorkspaceMemberPickerModalOnDeselect = (state: RootState) => state.modal.workspaceMemberPickerModal?.onDeselect;

export const selectNewCalendarIntegrationModalVisible = (state: RootState) => state.modal.newCalendarIntegrationModal?.visible;
export const selectNewCalendarIntegrationModalWorkspaceId = (state: RootState) =>
  state.modal.newCalendarIntegrationModal?.workspaceId;

export const selectCalendarExternalEventViewModalVisible = (state: RootState) =>
  state.modal.calendarExternalEventViewModal?.visible;
export const selectCalendarExternalEventViewModalCalendarEvent = (state: RootState) =>
  state.modal.calendarExternalEventViewModal?.calendarEventDto;

export const selectCalendarShareEventsModalVisible = (state: RootState) => state.modal.calendarShareEventsModal?.visible;
export const selectCalendarShareEventsModalWorkspaceId = (state: RootState) => state.modal.calendarShareEventsModal?.workspaceId;

export const selectChannelSettingsModalVisible = (state: RootState) => state.modal.channelSettingsModal?.visible;
export const selectChannelSettingsModalChannelId = (state: RootState) => state.modal.channelSettingsModal?.channelId;
export const selectChannelSettingsModalWorkspaceName = (state: RootState) => state.modal.channelSettingsModal?.workspaceName;
export const selectChannelSettingsModalWorkspaceId = (state: RootState) => state.modal.channelSettingsModal?.workspaceId;

export const selectNewChannelModalVisible = (state: RootState) => state.modal.newChannelModal?.visible;
export const selectNewChannelModalWorkspaceId = (state: RootState) => state.modal.newChannelModal?.workspaceId;

export const selectConversationSettingsModalVisible = (state: RootState) => state.modal.conversationSettingsModal?.visible;
export const selectConversationSettingsModalConversationId = (state: RootState) => state.modal.conversationSettingsModal?.conversationId;
export const selectConversationSettingsModalWorkspaceId = (state: RootState) => state.modal.conversationSettingsModal?.workspaceId;

export const selectChannelListModalVisible = (state: RootState) => state.modal.channelListModal?.visible;
export const selectChannelListModalWorkspaceId = (state: RootState) => state.modal.channelListModal?.workspaceId;

export const selectNewConversationModalVisible = (state: RootState) => state.modal.newConversationModal?.visible;
export const selectNewConversationModalWorkspaceId = (state: RootState) => state.modal.newConversationModal?.workspaceId;
export const selectNewConversationModalWorkspaceName = (state: RootState) => state.modal.newConversationModal?.workspaceName;

export const selectInstallPwaInstructionsModalVisible = (state: RootState) => state.modal.installPwaInstructionsModal?.visible;

export const selectNewProjectModalVisible = (state: RootState) => state.modal.newProjectModal?.visible;
export const selectNewProjectModalWorkspace = (state: RootState) => state.modal.newProjectModal?.workspace;

export const selectTeamPickerModalV2Visible = (state: RootState) => state.modal.teamPickerModalV2?.visible;
export const selectTeamPickerModalV2WorkspaceId = (state: RootState) => state.modal.teamPickerModalV2?.workspaceId;
export const selectTeamPickerModalV2Multiple = (state: RootState) => state.modal.teamPickerModalV2?.multiple;
export const selectTeamPickerModalV2ModalData = (state: RootState) => state.modal.teamPickerModalV2?.modalData;
export const selectTeamPickerModalV2InitialSelectionOnMultiple = (state: RootState) => state.modal.teamPickerModalV2?.initialSelectionOnMultiple;
export const selectTeamPickerModalV2OnPick = (state: RootState) => state.modal.teamPickerModalV2?.onPick;

export const selectProjectPrioritySelectModalVisible = (state: RootState) => state.modal.projectPrioritySelectModal?.visible;
export const selectProjectPrioritySelectModalOnPick = (state: RootState) => state.modal.projectPrioritySelectModal?.onPick;

export const selectProjectStateSelectModalVisible = (state: RootState) => state.modal.projectStateSelectModal?.visible;
export const selectProjectStateSelectModalOnPick = (state: RootState) => state.modal.projectStateSelectModal?.onPick;