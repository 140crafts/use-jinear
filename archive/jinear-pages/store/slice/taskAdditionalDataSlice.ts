import { TaskAdditionalDataMap } from "@/model/app/store/task/TaskAdditionalDataMap";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store";

const initialState = {
  taskDataMap: {},
} as {
  taskDataMap: TaskAdditionalDataMap;
};

const slice = createSlice({
  name: "taskAdditionalData",
  initialState,
  reducers: {
    markHasUnreadNotification: (state, action: PayloadAction<{ taskId: string }>) => {
      const taskId = action.payload.taskId;
      const taskData = state.taskDataMap[taskId] || {};
      taskData.hasUpdates = true;
      state.taskDataMap[taskId] = taskData;
    },
    clearHasUnreadNotification: (state, action: PayloadAction<{ taskId: string }>) => {
      const taskId = action.payload.taskId;
      const taskData = state.taskDataMap[taskId] || {};
      taskData.hasUpdates = false;
      state.taskDataMap[taskId] = taskData;
    },
    clearHasUnreadNotificationOnAllTasks: (state, action: PayloadAction<void>) => {
      state.taskDataMap = initialState.taskDataMap;
    },
    resetTaskAdditionalData: () => initialState,
  },
});

export const {
  markHasUnreadNotification,
  clearHasUnreadNotification,
  clearHasUnreadNotificationOnAllTasks,
  resetTaskAdditionalData,
} = slice.actions;
export default slice.reducer;

export const selectTaskAdditionalData = (taskId: string) => (state: RootState) => state.taskAdditionalData.taskDataMap[taskId];
