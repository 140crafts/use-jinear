import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import ModalState, {
  ChangeTaskAssigneeModalState,
  ChangeTaskDateModalState,
  ChangeTaskTopicModalState,
  ChangeTaskWorkflowStatusModalState,
  LoginWith2FaMailModalState,
  NotFoundModalState,
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
} as {
  loginWith2FaMailModal: null | LoginWith2FaMailModalState;
  loadingModal: null | ModalState;
  notFoundModal: null | NotFoundModalState;
  newTaskModal: null | ModalState;
  teamOptionsModal: null | ModalState;
  newTopicModal: null | ModalState;
  changeTaskWorkflowStatusModal: null | ChangeTaskWorkflowStatusModalState;
  changeTaskTopicModal: null | ChangeTaskTopicModalState;
  changeTaskDateModal: null | ChangeTaskDateModalState;
  changeTaskAssigneeModal: null | ChangeTaskAssigneeModalState;
  newWorkspaceModal: null | ModalState;
};

const slice = createSlice({
  name: "modal",
  initialState,
  reducers: {
    changeLoginWith2FaMailModalVisibility: (
      state,
      action: PayloadAction<LoginWith2FaMailModalState>
    ) => {
      state.loginWith2FaMailModal = action.payload;
    },
    changeLoadingModalVisibility: (
      state,
      action: PayloadAction<ModalState>
    ) => {
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
    popNewTaskModal: (state, action: PayloadAction<void>) => {
      state.newTaskModal = { visible: true };
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
    popChangeTaskWorkflowStatusModal: (
      state,
      action: PayloadAction<ChangeTaskWorkflowStatusModalState>
    ) => {
      state.changeTaskWorkflowStatusModal = {
        visible: true,
        task: action.payload.task,
      };
    },
    closeChangeTaskWorkflowStatusModal: (
      state,
      action: PayloadAction<void>
    ) => {
      state.changeTaskWorkflowStatusModal = { visible: false };
    },

    popChangeTaskTopicModal: (
      state,
      action: PayloadAction<ChangeTaskTopicModalState>
    ) => {
      state.changeTaskTopicModal = { visible: true, task: action.payload.task };
    },
    closeChangeTaskTopicModal: (state, action: PayloadAction<void>) => {
      state.changeTaskTopicModal = { visible: false };
    },

    popChangeTaskDateModal: (
      state,
      action: PayloadAction<ChangeTaskDateModalState>
    ) => {
      state.changeTaskDateModal = { ...action.payload, visible: true };
    },
    closeChangeTaskDateModal: (state, action: PayloadAction<void>) => {
      state.changeTaskDateModal = initialState.changeTaskDateModal;
    },

    popChangeTaskAssigneeModal: (
      state,
      action: PayloadAction<ChangeTaskAssigneeModalState>
    ) => {
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
  closeNewTaskModal,
  popTeamOptionsModal,
  closeTeamOptionsModal,
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
  resetModals,
} = slice.actions;
export default slice.reducer;

export const selectAnyModalVisible = (state: RootState) => {
  const modalState = state.modal || {};
  return Object.values(modalState)
    ?.map((modalState) => modalState?.visible || false)
    ?.reduce((prev, curr) => prev || curr);
};

export const selectLoginWith2FaMailModalVisible = (state: RootState) =>
  state.modal.loginWith2FaMailModal?.visible;
export const selectLoadingModalVisible = (state: RootState) =>
  state.modal.loadingModal?.visible;

export const selectNotFoundModalVisible = (state: RootState) =>
  state.modal.notFoundModal?.visible;
export const selectNotFoundModalTitle = (state: RootState) =>
  state.modal.notFoundModal?.title;
export const selectNotFoundModalLabel = (state: RootState) =>
  state.modal.notFoundModal?.label;
export const selectNotFoundModalImgSrc = (state: RootState) =>
  state.modal.notFoundModal?.imgSrc;
export const selectNotFoundModalImgAlt = (state: RootState) =>
  state.modal.notFoundModal?.imgAlt;

export const selectNewTaskModalVisible = (state: RootState) =>
  state.modal.newTaskModal?.visible;

export const selectTeamOptionsModalVisible = (state: RootState) =>
  state.modal.teamOptionsModal?.visible;

export const selectChangeTaskWorkflowStatusModalVisible = (state: RootState) =>
  state.modal.changeTaskWorkflowStatusModal?.visible;
export const selectChangeTaskWorkflowStatusModalTask = (state: RootState) =>
  state.modal.changeTaskWorkflowStatusModal?.task;

export const selectChangeTaskTopicModalVisible = (state: RootState) =>
  state.modal.changeTaskTopicModal?.visible;
export const selectChangeTaskTopicModalTaskId = (state: RootState) =>
  state.modal.changeTaskTopicModal?.task?.taskId;
export const selectChangeTaskTopicModalTaskCurrentTopicId = (
  state: RootState
) => state.modal.changeTaskTopicModal?.task?.topicId;

export const selectChangeTaskDateModalVisible = (state: RootState) =>
  state.modal.changeTaskDateModal?.visible;
export const selectChangeTaskDateModalTaskId = (state: RootState) =>
  state.modal.changeTaskDateModal?.task?.taskId;
export const selectChangeTaskDateModalDateType = (state: RootState) =>
  state.modal.changeTaskDateModal?.dateType ||
  initialState.changeTaskDateModal?.dateType ||
  "assigned";
export const selectChangeTaskDateModalTaskCurrentAssignedDate = (
  state: RootState
) => state.modal.changeTaskDateModal?.task?.assignedDate;
export const selectChangeTaskDateModalTaskCurrentDueDate = (state: RootState) =>
  state.modal.changeTaskDateModal?.task?.dueDate;

export const selectChangeTaskAssigneeModalVisible = (state: RootState) =>
  state.modal.changeTaskAssigneeModal?.visible;
export const selectChangeTaskAssigneeModalTaskId = (state: RootState) =>
  state.modal.changeTaskAssigneeModal?.task?.taskId;
export const selectChangeTaskAssigneeModalTaskCurrentAssigneeId = (
  state: RootState
) => state.modal.changeTaskAssigneeModal?.task?.assignedTo;
export const selectChangeTaskAssigneeModalTaskCurrentTeamId = (
  state: RootState
) => state.modal.changeTaskAssigneeModal?.task?.teamId;

export const selectNewWorkspaceModalVisible = (state: RootState) =>
  state.modal.newWorkspaceModal?.visible;
