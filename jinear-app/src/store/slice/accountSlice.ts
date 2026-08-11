import type {AccountDto, AccountsWorkspacePerspectiveDto, WorkspaceDto} from "@/model/be/jinear-core";
import {accountApi} from "@/api/accountApi";
import Logger from "@/util/logger";
import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import type {RootState} from "@/store";

export type NavigationStatus = "NOT_DECIDED" | "LOGGED_IN" | "NOT_LOGGED_IN";

const initialState = {
    current: null,
    authState: "NOT_DECIDED",
    sessionId: undefined
} as {
    current: null | AccountDto;
    authState: NavigationStatus;
    sessionId?: string;
};

const logger = Logger("accountSlice");

const slice = createSlice({
    name: "account",
    initialState,
    reducers: {
        setAuthStateAsNotDecided: (state, action: PayloadAction<void>) => {
            state.authState = "NOT_DECIDED";
        },
        logout: () => initialState
    },
    extraReducers: (builder) => {
        builder
            .addMatcher(accountApi.endpoints.me.matchFulfilled, (state, action) => {
                state.current = action.payload.data;
                state.authState = "LOGGED_IN";
                state.sessionId = action.payload.sessionId;
            })
            .addMatcher(accountApi.endpoints.me.matchRejected, (state, action) => {
                logger.log({ rejected: action });
                // Skipped due to `condition`: not a real attempt, ignore entirely.
                if (action.meta.condition) {
                    return;
                }
                const status = action.payload?.status;
                const isAuthError = typeof status === "number" && (status === 401 || status === 403);
                if (isAuthError) {
                    // Genuine server-side rejection: user is not authenticated.
                    state.current = null;
                    state.sessionId = undefined;
                    state.authState = "NOT_LOGGED_IN";
                }
                // else: network error, timeout, parse error, 5xx: keep current auth + stale data.
            });
    }
});

export const {logout, setAuthStateAsNotDecided} = slice.actions;
export default slice.reducer;

export const selectAuthState = (state: RootState) => state.account.authState;
export const selectIsLoggedIn = (state: RootState) => state.account.authState == "LOGGED_IN";

export const selectCurrentSessionId = (state: RootState) => state.account.sessionId;
export const selectCurrentAccount = (state: RootState) => state.account.current;
export const selectCurrentAccountId = (state: RootState) => state.account.current?.accountId;

export const selectIsAccountIdIsCurrentAccountId = (accountId: string) => (state: RootState) =>
    accountId == state.account.current?.accountId;

export const selectCurrentAccountsWorkspaces = (state: RootState) => state.account.current?.workspaces;

export const selectCurrentAccountHasAnyWorkspace = (state: RootState) =>
    state.account.current?.workspaces == null || state.account.current?.workspaces.length == 0;

export const selectCurrentAccountsWorkspace = (workspaceId?: string) => (state: RootState) => {
    return state.account.current?.workspaces.find((workspace: WorkspaceDto) => workspace.workspaceId == workspaceId);
};

export const selectCurrentAccountsWorkspaceRoleIsAdminOrOwner = (
    accountsWorkspacePerspectiveDto: AccountsWorkspacePerspectiveDto
) => {
    const role = accountsWorkspacePerspectiveDto?.role;
    return role == "ADMIN" || role == "OWNER";
};

export const selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto =
    (workspace: WorkspaceDto) => (state: RootState) => {
        const accountsWorkspacePerspectiveDto = state.account.current?.workspaces.find((w: WorkspaceDto) => w.workspaceId == workspace.workspaceId);
        const role = accountsWorkspacePerspectiveDto?.role;
        return role == "ADMIN" || role == "OWNER";
    };

export const selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithWorkspaceUsername = (username?: string) => (state: RootState) => {
    const accountsWorkspacePerspectiveDto = state.account.current?.workspaces.find((w: WorkspaceDto) => w.username == username);
    const role = accountsWorkspacePerspectiveDto?.role;
    return role == "ADMIN" || role == "OWNER";
};

export const selectWorkspaceFromWorkspaceUsername = (workspaceUsername?: string) => (state: RootState) =>
    state.account.current?.workspaces.find((workspace: WorkspaceDto) => workspace.username == workspaceUsername);
