import { combineReducers, configureStore } from '@reduxjs/toolkit'
import { setupListeners } from '@reduxjs/toolkit/query'
import {
  persistReducer,
  persistStore,
  FLUSH,
  REHYDRATE,
  PAUSE,
  PERSIST,
  PURGE,
  REGISTER,
} from 'redux-persist'
import createWebStorageImport from 'redux-persist/lib/storage/createWebStorage'

import { api } from './api/api.ts'
import accountSlice from '@/store/slice/accountSlice.ts'

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
  account: accountSlice,
  [api.reducerPath]: api.reducer,
})

const persistConfig = {
  key: 'jinear-app',
  storage,
  whitelist: ['auth', api.reducerPath],
}

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }).concat(api.middleware),
})

export const persistor = persistStore(store)

setupListeners(store.dispatch)

export type RootState = ReturnType<typeof rootReducer>
export type AppDispatch = typeof store.dispatch
