"use client";
import { configureStore, ConfigureStoreOptions } from "@reduxjs/toolkit";
import { TypedUseSelectorHook, useDispatch, useSelector } from "react-redux";

import account, { logout } from "@/slice/accountSlice";
import displayPreference, { resetDisplayPreferences } from "@/slice/displayPreferenceSlice";
import firebase, { resetFirebaseSlice } from "@/slice/firebaseSlice";
import modal, { resetModals } from "@/slice/modalSlice";
import sseSlice, { resetSseSlice } from "@/slice/sseSlice";
import taskAdditionalData, { resetTaskAdditionalData } from "@/slice/taskAdditionalDataSlice";
import { makeStoreAccessibleFromWindow } from "@/utils/webviewUtils";
import { combineReducers } from "redux";
import { api } from "./api/api";
import { rtkQueryErrorLogger } from "./api/errorMiddleware";
import messagingSlice, { resetMessagingData } from "@/slice/messagingSlice";

const rootReducer = combineReducers({
  [api.reducerPath]: api.reducer,
  account,
  modal,
  displayPreference,
  taskAdditionalData,
  firebase,
  sseSlice,
  messagingSlice
});

export const createStore = (options?: ConfigureStoreOptions["preloadedState"] | undefined) =>
  configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: false
      })
        .concat(api.middleware)
        .concat(rtkQueryErrorLogger),
    ...options
  });

export const store = createStore();

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch: () => AppDispatch = useDispatch;
export type RootState = ReturnType<typeof rootReducer>;
export const useTypedSelector: TypedUseSelectorHook<RootState> = useSelector;

makeStoreAccessibleFromWindow(store);

export const resetAllStates = (dispatch: typeof store.dispatch) => {
  dispatch(logout());
  dispatch(resetModals());
  dispatch(resetDisplayPreferences());
  dispatch(resetTaskAdditionalData());
  dispatch(resetFirebaseSlice());
  dispatch(resetSseSlice());
  dispatch(resetMessagingData());
  dispatch(api.util.resetApiState());
};