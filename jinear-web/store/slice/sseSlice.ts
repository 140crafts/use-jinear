import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { PayloadAction, createSlice } from "@reduxjs/toolkit";
import { RootState } from "../store";

const initialState = {
  latestWorkspaceActivity: undefined,
} as {
  latestWorkspaceActivity?: WorkspaceActivityDto;
};

const logger = Logger("sseSlice");

const slice = createSlice({
  name: "sseSlice",
  initialState,
  reducers: {
    setLatestWorkspaceActivity: (state, action: PayloadAction<WorkspaceActivityDto | undefined>) => {
      state.latestWorkspaceActivity = action.payload;
    },
    resetSseSlice: () => initialState,
  },
});

export const { setLatestWorkspaceActivity, resetSseSlice } = slice.actions;
export default slice.reducer;

export const selectLatestWorkspaceActivity = (state: RootState) => state.sseSlice.latestWorkspaceActivity;
