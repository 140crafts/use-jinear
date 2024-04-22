"use client";
import { configureStore, ConfigureStoreOptions } from "@reduxjs/toolkit";
import { TypedUseSelectorHook, useDispatch, useSelector } from "react-redux";

import account from "@/slice/accountSlice";
import displayPreference from "@/slice/displayPreferenceSlice";
import firebase from "@/slice/firebaseSlice";
import modal from "@/slice/modalSlice";
import sseSlice from "@/slice/sseSlice";
import taskAdditionalData from "@/slice/taskAdditionalDataSlice";
import { makeStoreAccessibleFromWindow } from "@/utils/webviewUtils";
import { combineReducers } from "redux";
import { api } from "./api/api";
import { rtkQueryErrorLogger } from "./api/errorMiddleware";
import messagingSlice from "@/slice/messagingSlice";

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
        serializableCheck: false,
      })
        .concat(api.middleware)
        .concat(rtkQueryErrorLogger),
    ...options,
  });

export const store = createStore();

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch: () => AppDispatch = useDispatch;
export type RootState = ReturnType<typeof rootReducer>;
export const useTypedSelector: TypedUseSelectorHook<RootState> = useSelector;

makeStoreAccessibleFromWindow(store);
