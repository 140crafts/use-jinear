import { AppMenu } from "@/model/app/store/displayPreference/appMenu";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store";

const initialState = {
  appMenu: {
    mobileVisible: false,
  },
} as {
  appMenu: AppMenu;
};

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
    toggleMenu: (state, action: PayloadAction<void>) => {
      state.appMenu.mobileVisible = !state.appMenu.mobileVisible;
    },
    resetDisplayPreferences: () => initialState,
  },
});

export const { popMenu, closeMenu, toggleMenu } = slice.actions;
export default slice.reducer;

export const selectAppMenuVisible = (state: RootState) => state.displayPreference.appMenu.mobileVisible;
