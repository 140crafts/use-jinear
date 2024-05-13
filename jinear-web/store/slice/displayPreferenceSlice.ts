import { AppMenu } from "@/model/app/store/displayPreference/appMenu";
import Logger from "@/utils/logger";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store";

const initialState = {
  appMenu: {
    mobileVisible: false,
    tasksMenuVisible: true,
    calendarsMenuVisible: true,
    conversationsMenuVisible: true,
  },
} as {
  appMenu: AppMenu;
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
    toggleCalendarsMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.calendarsMenuVisible = !state.appMenu.calendarsMenuVisible;
    },
    closeCalendarsMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.calendarsMenuVisible = false;
    },
    toggleConversationsMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.conversationsMenuVisible = !state.appMenu.conversationsMenuVisible;
    },
    closeConversationsMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.conversationsMenuVisible = false;
    },
    popAllMenus: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = true;
      state.appMenu.tasksMenuVisible = true;
      state.appMenu.calendarsMenuVisible = true;
    },
    closeAllMenus: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = false;
      state.appMenu.tasksMenuVisible = false;
      state.appMenu.calendarsMenuVisible = false;
    },
    toggleMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = !state.appMenu.mobileVisible;
    },
    resetDisplayPreferences: () => initialState,
  },
});

export const {
  popMenu,
  closeMenu,
  popTasksMenu,
  closeTasksMenu,
  toggleTasksMenu,
  toggleCalendarsMenu,
  closeCalendarsMenu,
  toggleConversationsMenu,
  closeConversationsMenu,
  popAllMenus,
  closeAllMenus,
  toggleMenu,
  resetDisplayPreferences,
} = slice.actions;
export default slice.reducer;

export const selectAppMenuVisible = (state: RootState) => state.displayPreference.appMenu.mobileVisible;
export const selectTasksMenuVisible = (state: RootState) => state.displayPreference.appMenu.tasksMenuVisible;
export const selectCalendarsMenuVisible = (state: RootState) => state.displayPreference.appMenu.calendarsMenuVisible;
export const selectConversationsMenuVisible = (state: RootState) => state.displayPreference.appMenu.conversationsMenuVisible;
export const selectAnyMenuVisible = (state: RootState) =>
  state.displayPreference.appMenu.mobileVisible ||
  state.displayPreference.appMenu.tasksMenuVisible ||
  state.displayPreference.appMenu.calendarsMenuVisible ||
  state.displayPreference.appMenu.conversationsMenuVisible;
