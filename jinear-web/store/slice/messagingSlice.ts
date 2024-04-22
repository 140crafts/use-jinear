import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store";
import { ThreadLastMessageMap } from "@/model/app/store/messaging/ThreadLastMessageMap";
import { MessageDto } from "@/be/jinear-core";
import { isAfter } from "date-fns";
import { messageOperationApi } from "@/api/messageOperationApi";

const initialState = {
  threadLastMessageMap: {}
} as {
  threadLastMessageMap: ThreadLastMessageMap;
};

const slice = createSlice({
  name: "messaging",
  initialState,
  reducers: {
    checkAndUpdateThreadLastMessage: (state, action: PayloadAction<{ message: MessageDto }>) => {
      const message = action.payload.message;
      const threadId = message.threadId;
      if (threadId) {
        const existingLastMessage = state.threadLastMessageMap[threadId];
        if (existingLastMessage != null) {
          state.threadLastMessageMap[threadId] = isAfter(new Date(existingLastMessage.createdDate), new Date(message.createdDate)) ? existingLastMessage : message;
        } else {
          state.threadLastMessageMap[threadId] = message;
        }
      }
    },
    resetMessagingData: () => initialState
  },
  extraReducers: (builder) => {
    builder
      .addMatcher(messageOperationApi.endpoints.sendToThread.matchFulfilled, (state, action) => {
        const sentMessage = action.payload.data;
        slice.caseReducers.checkAndUpdateThreadLastMessage(state, { payload: { message: sentMessage }, type: "" });
      });
  }
});

export const {
  checkAndUpdateThreadLastMessage,
  resetMessagingData
} = slice.actions;
export default slice.reducer;

export const selectThreadLastMessage = (threadId: string) => (state: RootState) => state.messagingSlice.threadLastMessageMap[threadId];
