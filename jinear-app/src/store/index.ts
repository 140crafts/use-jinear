import {combineReducers, configureStore, type ConfigureStoreOptions} from '@reduxjs/toolkit'
import {setupListeners} from '@reduxjs/toolkit/query'
import {persistReducer, persistStore,} from 'redux-persist'
import createWebStorageImport from 'redux-persist/lib/storage/createWebStorage'

import {api} from './api/api.ts'
import account, {logout} from "@/slice/accountSlice";
import displayPreference, {resetDisplayPreferences} from "@/slice/displayPreferenceSlice";
import firebase, {resetFirebaseSlice} from "@/slice/firebaseSlice";
import modal, {resetModals} from "@/slice/modalSlice";
import sseSlice, {resetSseSlice} from "@/slice/sseSlice";
import taskAdditionalData, {resetTaskAdditionalData} from "@/slice/taskAdditionalDataSlice";
import {type TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";
import {makeStoreAccessibleFromWindow} from "@/util/webviewUtils.ts";
import {rtkQueryErrorLogger} from "@/api/errorMiddleware.ts";

const createWebStorage =
    (createWebStorageImport as unknown as { default?: typeof createWebStorageImport })
        .default ?? createWebStorageImport

const createNoopStorage = () => ({
    getItem: () => Promise.resolve(null),
    setItem: (_key: string, value: unknown) => Promise.resolve(value),
    removeItem: () => Promise.resolve(),
})

const storage =
    globalThis.window === undefined
        ? createNoopStorage()
        : createWebStorage('local')

const rootReducer = combineReducers({
    account,
    modal,
    displayPreference,
    taskAdditionalData,
    firebase,
    sseSlice,
    [api.reducerPath]: api.reducer,
})

const persistConfig = {
    key: 'jinear-app',
    storage,
    whitelist: ['auth', api.reducerPath],
}

const persistedReducer = persistReducer(persistConfig, rootReducer)

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
export const persistor = persistStore(store)
setupListeners(store.dispatch)

export type RootState = ReturnType<typeof rootReducer>
export type AppDispatch = typeof store.dispatch
export const useAppDispatch: () => AppDispatch = useDispatch;
export const useTypedSelector: TypedUseSelectorHook<RootState> = useSelector;

makeStoreAccessibleFromWindow(store);

export const resetAllStates = (dispatch: typeof store.dispatch) => {
    dispatch(logout());
    dispatch(resetModals());
    dispatch(resetDisplayPreferences());
    dispatch(resetTaskAdditionalData());
    dispatch(resetFirebaseSlice());
    dispatch(resetSseSlice());
    dispatch(api.util.resetApiState());
};
