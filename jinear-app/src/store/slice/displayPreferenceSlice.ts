import type {AppMenu} from "@/model/app/store/displayPreference/appMenu";
import Logger from "@/util/logger";
import {createSlice, type  PayloadAction} from "@reduxjs/toolkit";
import type {RootState} from "@/store";

export type NoteTextWidth = "page" | "full";

const initialState = {
    appMenu: {
        secondLevelMenuVisible: false
    },
    noteTextWidth: "page"
} as {
    appMenu: AppMenu;
    noteTextWidth?: NoteTextWidth;
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
        toggleNoteTextWidth: (state, action: PayloadAction<void>) => {
            state.noteTextWidth = state.noteTextWidth === "full" ? "page" : "full";
        },
        resetDisplayPreferences: () => initialState
    }
});

export const {
    popSecondLevelMenu,
    closeSecondLevelMenu,
    closeAllMenus,
    toggleNoteTextWidth,
    resetDisplayPreferences
} = slice.actions;
export default slice.reducer;

export const selectAnyMenuVisible = (state: RootState) =>
    state.displayPreference.appMenu.secondLevelMenuVisible;

export const selectSecondLevelMenuVisible = (state: RootState) =>
    state.displayPreference.appMenu.secondLevelMenuVisible;

export const selectNoteTextWidth = (state: RootState): NoteTextWidth =>
    state.displayPreference.noteTextWidth ?? "page";
