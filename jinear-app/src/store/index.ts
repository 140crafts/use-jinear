import {combineReducers, configureStore, type ConfigureStoreOptions} from '@reduxjs/toolkit'
import {setupListeners} from '@reduxjs/toolkit/query'
import {persistReducer, persistStore,} from 'redux-persist'
import localforage from 'localforage'

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
import Logger from "@/util/logger";

const logger = Logger("Store");

const createNoopStorage = () => ({
    getItem: () => Promise.resolve(null),
    setItem: (_key: string, value: unknown) => Promise.resolve(value),
    removeItem: () => Promise.resolve(),
})

const createLocalforageStorage = () => {
    const store = localforage.createInstance({name: 'jinear-app', storeName: 'redux-persist'})
    // Old storage backend; clear it so the stale multi-MB payload doesn't linger.
    window.localStorage.removeItem('persist:jinear-app')
    return store
}

export const clearLocalforageStorage = () => {
    localforage.clear().then(function () {
        logger.log({message: "Database is now empty."});
    }).catch(function (err) {
        logger.error({message: "Persist clear failed", err});
    });
}

const storage =
    globalThis.window === undefined
        ? createNoopStorage()
        : createLocalforageStorage()

const rootReducer = combineReducers({
    [api.reducerPath]: api.reducer,
    account,
    modal,
    displayPreference,
    taskAdditionalData,
    firebase,
    sseSlice,
})

const persistConfig = {
    key: 'jinear-app',
    storage,
    whitelist: ['account', 'displayPreference', 'taskAdditionalData', api.reducerPath],
    throttle: 2000,
    serialize: false,
    deserialize: false,
    writeFailHandler: (error: Error) => logger.error({message: "Persist write failed", error})
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

// Throttled writes can lag behind by up to 2s; drain them when the tab is
// backgrounded or closed (pagehide also covers mobile/PWA bfcache cases).
if (globalThis.window !== undefined) {
    window.addEventListener('pagehide', () => {
        persistor.flush();
    });
}

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
