import type {FetchBaseQueryError} from "@reduxjs/toolkit/query";
import {API_ROOT} from "@/util/constants.ts";

export const LOGOUT_ENDPOINT_NAME = "logout";
export const LOGOUT_PATH = "v1/auth/logout";

const PENDING_LOGOUT_KEY = "PENDING_LOGOUT";
const UNAUTHORIZED_STATUS = 401;
const PENDING_LOGOUT_TIMEOUT_MILLIS = 8000;

let pendingLogoutRequest: Promise<FetchBaseQueryError | undefined> | undefined;

export const isLogoutPending = () => localStorage.getItem(PENDING_LOGOUT_KEY) != null;

export const markLogoutPending = () => localStorage.setItem(PENDING_LOGOUT_KEY, "true");

const clearPendingLogout = () => localStorage.removeItem(PENDING_LOGOUT_KEY);

export const recordLogoutResult = (error?: FetchBaseQueryError) => {
    const sessionEnded = !error || error.status === UNAUTHORIZED_STATUS;
    if (sessionEnded) {
        clearPendingLogout();
        return;
    }
    markLogoutPending();
};

const sendPendingLogout = async (): Promise<FetchBaseQueryError | undefined> => {
    try {
        const response = await fetch(`${API_ROOT}${LOGOUT_PATH}`, {
            method: "POST",
            credentials: "include",
            signal: AbortSignal.timeout(PENDING_LOGOUT_TIMEOUT_MILLIS)
        });
        if (response.ok || response.status === UNAUTHORIZED_STATUS) {
            clearPendingLogout();
            return undefined;
        }
        return {status: response.status, data: undefined};
    } catch (error) {
        return {status: "FETCH_ERROR", error: String(error)};
    }
};

export const completePendingLogout = () => {
    pendingLogoutRequest ??= sendPendingLogout().finally(() => {
        pendingLogoutRequest = undefined;
    });
    return pendingLogoutRequest;
};
