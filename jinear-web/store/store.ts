"use client";
import { configureStore, ConfigureStoreOptions } from "@reduxjs/toolkit";
import { TypedUseSelectorHook, useDispatch, useSelector } from "react-redux";
import { setupListeners } from "@reduxjs/toolkit/query";

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

import { persistReducer, persistStore } from "redux-persist";
import createWebStorageImport from "redux-persist/lib/storage/createWebStorage";

const createWebStorage =
  (createWebStorageImport as unknown as { default?: typeof createWebStorageImport })
    .default ?? createWebStorageImport;

const createNoopStorage = () => ({
  getItem: () => Promise.resolve(null),
  setItem: (_key: string, value: unknown) => Promise.resolve(value),
  removeItem: () => Promise.resolve()
});

const storage =
  globalThis.window === undefined
    ? createNoopStorage()
    : createWebStorage("local");

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

const persistConfig = {
  key: "jinear-app",
  storage,
  // Persist auth state plus the RTK Query cache (api.reducerPath) so previously
  // fetched data is available immediately on reload and while offline.
  // `modal` and the other UI/transient slices are intentionally NOT persisted
  // (modal stores callback functions in state, which are non-serializable).
  whitelist: ["account", api.reducerPath]
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const createStore = (options?: ConfigureStoreOptions["preloadedState"] | undefined) =>
  configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: false
      })
        .concat(api.middleware)
        .concat(rtkQueryErrorLogger),
    ...options
  });

export const store = createStore();

export const persistor = persistStore(store);

setupListeners(store.dispatch);

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