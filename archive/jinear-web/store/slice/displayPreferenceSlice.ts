import { AppMenu } from "@/model/app/store/displayPreference/appMenu";
import Logger from "@/utils/logger";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store";

const initialState = {
  appMenu: {
    secondLevelMenuVisible: false
  }
} as {
  appMenu: AppMenu;
};

const logger = Logger("displayPreferenceSlice");

const slice = createSlice({
  name: "displayPreference",
  initialState,
  reducers: {
    popSecondLevelMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.secondLevelMenuVisible = true;
    },
    closeSecondLevelMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.secondLevelMenuVisible = false;
    },
    closeAllMenus: (state, action: PayloadAction<void>) => {
      state.appMenu.secondLevelMenuVisible = false;
    },
    resetDisplayPreferences: () => initialState
  }
});

export const {
  popSecondLevelMenu,
  closeSecondLevelMenu,
  closeAllMenus,
  resetDisplayPreferences
} = slice.actions;
export default slice.reducer;

export const selectAnyMenuVisible = (state: RootState) =>
  state.displayPreference.appMenu.secondLevelMenuVisible;

export const selectSecondLevelMenuVisible = (state: RootState) =>
  state.displayPreference.appMenu.secondLevelMenuVisible;
