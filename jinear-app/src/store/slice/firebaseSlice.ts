import Logger from "@/util/logger";
import {type PayloadAction, createSlice} from "@reduxjs/toolkit";
import type {FirebaseApp} from "firebase/app";
import type {Messaging} from "firebase/messaging";
import type {RootState} from "@/store";

const initialState = {
    firebase: undefined,
    messaging: undefined,
} as {
    firebase?: FirebaseApp;
    messaging?: Messaging;
};

const logger = Logger("FirebaseSlice");

const slice = createSlice({
    name: "firebaseSlice",
    initialState,
    reducers: {
        setFirebase: (state, action: PayloadAction<FirebaseApp>) => {
            state.firebase = action.payload;
        },
        setMessaging: (state, action: PayloadAction<Messaging>) => {
            state.messaging = action.payload;
        },
        resetFirebaseSlice: () => initialState,
    },
});

export const {setFirebase, setMessaging, resetFirebaseSlice} = slice.actions;
export default slice.reducer;

export const selectFirebase = (state: RootState) => state.firebase.firebase;
export const selectMessaging = (state: RootState) => state.firebase.messaging;
