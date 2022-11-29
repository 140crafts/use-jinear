import { accountApi } from "@/api/accountApi";
import { AccountDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "../store";

export type NavigationStatus = "NOT_DECIDED" | "LOGGED_IN" | "NOT_LOGGED_IN";

const initialState = {
  current: null,
  authState: "NOT_DECIDED",
} as {
  current: null | AccountDto;
  authState: NavigationStatus;
};

const logger = Logger("accountSlice");

const slice = createSlice({
  name: "account",
  initialState,
  reducers: {
    logout: () => initialState,
  },
  extraReducers: (builder) => {
    builder
      .addMatcher(accountApi.endpoints.me.matchFulfilled, (state, action) => {
        state.current = action.payload.data;
        state.authState = "LOGGED_IN";
      })
      .addMatcher(accountApi.endpoints.me.matchRejected, (state, action) => {
        logger.log({ rejected: action });
        state.current = null;
        if (action.error?.name != "ConditionError") {
          state.authState = "NOT_LOGGED_IN";
        }
      });
  },
});

export const { logout } = slice.actions;
export default slice.reducer;

export const selectAuthState = (state: RootState) => state.account.authState;
export const selectIsLoggedIn = (state: RootState) =>
  state.account.authState == "LOGGED_IN";

export const selectCurrentAccount = (state: RootState) => state.account.current;
export const selectCurrentAccountId = (state: RootState) =>
  state.account.current?.accountId;

export const selectIsAccountIdIsCurrentAccountId =
  (accountId: string) => (state: RootState) =>
    accountId == state.account.current?.accountId;

export const selectCurrentAccountsPersonalWorkspace = (state: RootState) =>
  state.account.current?.workspaces?.find((w) => w.personal);

export const selectCurrentAccountsCollectiveWorkspaces = (state: RootState) =>
  state.account.current?.workspaces?.filter((w) => !w.personal);

export const selectCurrentAccountsPreferredWorkspace = (state: RootState) => {
  const preferred = state.account.current?.workspaces?.find(
    (w) => w.workspaceId == state.account.current?.preferredWorkspaceId
  );
  return preferred ? preferred : selectCurrentAccountsPersonalWorkspace(state);
};
