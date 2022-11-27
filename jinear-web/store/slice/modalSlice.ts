import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import ModalState, {
  LoginWith2FaMailModalState,
  NotFoundModalState,
} from "model/app/store/modal/modalState";
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
} as {
  loginWith2FaMailModal: null | LoginWith2FaMailModalState;
  loadingModal: null | ModalState;
  notFoundModal: null | NotFoundModalState;
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
    resetModals: () => initialState,
  },
});

export const {
  changeLoginWith2FaMailModalVisibility,
  changeLoadingModalVisibility,
  popNotFoundModal,
  closeNotFoundModal,
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
