import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import ModalState, {
  LoginWith2FaMailModalState,
  NewTaskModalState,
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
} as {
  loginWith2FaMailModal: null | LoginWith2FaMailModalState;
  loadingModal: null | ModalState;
  notFoundModal: null | NotFoundModalState;
  newTaskModal: null | NewTaskModalState;
  teamOptionsModal: null | ModalState;
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
    popNewTaskModal: (
      state,
      action: PayloadAction<{ workspaceId: string }>
    ) => {
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

    resetModals: () => initialState,
  },
  extraReducers: (builder) => {
    builder
      .addMatcher(accountApi.endpoints.me.matchPending, (state, action) => {
        console.log("matchPending1");
        state.loadingModal = { visible: true };
      })
      .addMatcher(accountApi.endpoints.me.matchFulfilled, (state, action) => {
        console.log("matchFulfilled2");
        state.loadingModal = { visible: false };
      })
      .addMatcher(accountApi.endpoints.me.matchRejected, (state, action) => {
        console.log("matchRejected3");
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
  resetModals,
} = slice.actions;
export default slice.reducer;

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
export const selectNewTaskModalWorkspaceId = (state: RootState) =>
  state.modal.newTaskModal?.workspaceId;

export const selectTeamOptionsModalVisible = (state: RootState) =>
  state.modal.teamOptionsModal?.visible;
