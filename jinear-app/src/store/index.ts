import {combineReducers, configureStore, type ConfigureStoreOptions} from '@reduxjs/toolkit'
import {setupListeners} from '@reduxjs/toolkit/query'
import {persistReducer, persistStore,} from 'redux-persist'
import localforage from 'localforage'

import {api} from './api/api.ts'
import account, {logout} from "@/slice/accountSlice";
import displayPreference, {resetDisplayPreferences} from "@/slice/displayPreferenceSlice";
import firebase, {resetFirebaseSlice} from "@/slice/firebaseSlice";
import modal, {resetModals} from "@/slice/modalSlice";
import noteDrafts, {resetNoteDrafts} from "@/slice/noteDraftsSlice";
import sseSlice, {resetSseSlice} from "@/slice/sseSlice";
import taskAdditionalData, {resetTaskAdditionalData} from "@/slice/taskAdditionalDataSlice";
import {type TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";
import {makeStoreAccessibleFromWindow} from "@/util/webviewUtils.ts";
import {rtkQueryErrorLogger} from "@/api/errorMiddleware.ts";
import {deleteAllLocalDocs} from "@/components/tiptap/crdt/deleteAllLocalDocs.ts";
import Logger from "@/util/logger";

const logger = Logger("Store");

const createNoopStorage = () => ({
    getItem: () => Promise.resolve(null),
    setItem: (_key: string, value: unknown) => Promise.resolve(value),
    removeItem: () => Promise.resolve(),
})

// Kept module-level so clearLocalforageStorage empties the instance persistence actually
// writes to — localforage.clear() would hit the global default instance instead.
let localforageStore: LocalForage | null = null

const createLocalforageStorage = () => {
    const store = localforage.createInstance({name: 'jinear-app', storeName: 'redux-persist'})
    localforageStore = store
    // Old storage backend; clear it so the stale multi-MB payload doesn't linger.
    window.localStorage.removeItem('persist:jinear-app')
    return store
}

export const clearLocalforageStorage = () =>
    (localforageStore?.clear() ?? Promise.resolve())
        .then(() => {
            logger.log({message: "Database is now empty."});
        })
        .catch((err: unknown) => {
            logger.error({message: "Persist clear failed", err});
        });

export const resetLocalStorage = () => {
    try {
        if (typeof window === "object") {
            window.localStorage.clear();
        }
    } catch (error) {
        logger.error({message: "Error on logout clear. local storage", error});
    }
};

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
    noteDrafts,
})

const persistConfig = {
    key: 'jinear-app',
    storage,
    whitelist: ['account', 'displayPreference', 'taskAdditionalData', 'noteDrafts', api.reducerPath],
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
    dispatch(resetNoteDrafts());
    dispatch(resetFirebaseSlice());
    dispatch(resetSseSlice());
    dispatch(api.util.resetApiState());
};

/**
 * The one local-cleanup path for leaving an account — explicit logout and account deletion alike.
 * Every entry point must call this, otherwise the previous user's notes stay readable on disk.
 *
 * Order is deliberate: doc keys are read before the reset wipes them, and the reset itself runs
 * before the delete so PendingDraftSubmitter can't resurrect a doc database mid-wipe (it recreates
 * one via readLocalDocState for any draft still in `pending`).
 */
export const performLogoutCleanup = async (dispatch: typeof store.dispatch) => {
    const {pending, docKeyAliases} = (store.getState() as RootState).noteDrafts;
    const knownDocKeys = [
        ...Object.keys(pending),
        ...Object.keys(docKeyAliases),
        ...Object.values(docKeyAliases)
    ];

    resetAllStates(dispatch);
    resetLocalStorage();
    await clearLocalforageStorage();
    await deleteAllLocalDocs(knownDocKeys);
};
