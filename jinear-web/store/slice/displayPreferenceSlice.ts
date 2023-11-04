import { AppMenu } from "@/model/app/store/displayPreference/appMenu";
import Logger from "@/utils/logger";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { workspaceDisplayPreferenceApi } from "../api/workspaceDisplayPreferenceApi";
import { RootState } from "../store";

const initialState = {
  appMenu: {
    mobileVisible: false,
    tasksMenuVisible: true,
  },
  reroute: undefined,
} as {
  appMenu: AppMenu;
  reroute?: string;
};

const logger = Logger("displayPreferenceSlice");

const slice = createSlice({
  name: "displayPreference",
  initialState,
  reducers: {
    popMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = true;
    },
    closeMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = false;
    },
    popTasksMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.tasksMenuVisible = true;
    },
    toggleTasksMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.tasksMenuVisible = !state.appMenu.tasksMenuVisible;
    },
    closeTasksMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.tasksMenuVisible = false;
    },
    popAllMenus: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = true;
      state.appMenu.tasksMenuVisible = true;
    },
    closeAllMenus: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = false;
      state.appMenu.tasksMenuVisible = false;
    },
    toggleMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = !state.appMenu.mobileVisible;
    },

    clearReroute: (state, action: PayloadAction<void>) => {
      state.reroute = undefined;
    },

    resetDisplayPreferences: () => initialState,
  },
  extraReducers: (builder) => {
    builder.addMatcher(
      workspaceDisplayPreferenceApi.endpoints.updatePreferredWorkspace.matchFulfilled,
      setRerouteOnPreferenceChangeSuccess
    );
    builder.addMatcher(
      workspaceDisplayPreferenceApi.endpoints.updatePreferredWorkspaceWithUsername.matchFulfilled,
      setRerouteOnPreferenceChangeSuccess
    );
    builder.addMatcher(
      workspaceDisplayPreferenceApi.endpoints.updatePreferredTeam.matchFulfilled,
      setRerouteOnPreferenceChangeSuccess
    );
    builder.addMatcher(
      workspaceDisplayPreferenceApi.endpoints.updatePreferredTeamWithUsername.matchFulfilled,
      setRerouteOnPreferenceChangeSuccess
    );
  },
});

const setRerouteOnPreferenceChangeSuccess = (state: any, action: any) => {
  if (action?.meta?.arg?.originalArgs?.dontReroute) {
    return;
  }
  const workspaceUsername = action.payload.data.workspace?.username;
  const reroute = `/${workspaceUsername}`;
  logger.log({ reroute, originalArgs: action?.meta?.arg?.originalArgs });
  state.reroute = reroute;
};

export const {
  popMenu,
  closeMenu,
  popTasksMenu,
  closeTasksMenu,
  toggleTasksMenu,
  popAllMenus,
  closeAllMenus,
  toggleMenu,
  clearReroute,
} = slice.actions;
export default slice.reducer;

export const selectAppMenuVisible = (state: RootState) => state.displayPreference.appMenu.mobileVisible;
export const selectTasksMenuVisible = (state: RootState) => state.displayPreference.appMenu.tasksMenuVisible;
export const selectAnyMenuVisible = (state: RootState) =>
  state.displayPreference.appMenu.mobileVisible || state.displayPreference.appMenu.tasksMenuVisible;

export const selectReroute = (state: RootState) => state.displayPreference.reroute;
