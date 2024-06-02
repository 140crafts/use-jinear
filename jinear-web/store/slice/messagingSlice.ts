import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store";
import {
  IChannelDateMap,
  IChannelThreadMap,
  IConversationLastCheckMap,
  IConversationMessageMap,
  IThreadMessageInfoMap,
  IThreadMessageMap
} from "@/model/app/store/messaging/ThreadLastMessageMap";
import { MessageDto, ThreadDto, ThreadMessageInfoDto } from "@/be/jinear-core";
import { isAfter } from "date-fns";
import { messageOperationApi } from "@/api/messageOperationApi";
import { messageListingApi } from "@/api/messageListingApi";
import { conversationApi } from "@/api/conversationApi";
import Logger from "@/utils/logger";
import { threadApi } from "@/api/threadApi";
import { channelMemberApi } from "@/api/channelMemberApi";
import {
  checkAndUpdateChannelLastActivity as checkAndUpdateChannelLastActivityRepo,
  checkAndUpdateChannelLastCheck as checkAndUpdateChannelLastCheckRepo,
  checkAndUpdateConversationLastCheck as checkAndUpdateConversationLastCheckRepo,
  insertAllMessages,
  insertAllThreads,
  insertMessage,
  insertThread
} from "../../repository/IndexedDbRepository";

const initialState = {} as {
  [workspaceId: string]: {
    channelThreadMap: IChannelThreadMap,
    threadMessageMap: IThreadMessageMap,
    threadMessageInfoMap: IThreadMessageInfoMap,
    conversationMessageMap: IConversationMessageMap;
    conversationLastCheckMap: IConversationLastCheckMap,
    channelLastCheckMap: IChannelDateMap;
    channelLastActivityMap: IChannelDateMap;
  }
};

const logger = Logger("messagingSlice");

const slice = createSlice({
  name: "messaging",
  initialState,
  reducers: {

    upsertThread: (state, action: PayloadAction<{ workspaceId: string, thread: ThreadDto }>) => {
      const { workspaceId, thread } = action.payload;
      const channelId = thread.channelId as string;
      const workspaceState = state[workspaceId] || {};
      const channelThreadMap = { ...(workspaceState.channelThreadMap || {}) };
      if (channelId) {
        const threadMap = channelThreadMap?.[channelId] || {};
        threadMap[thread.threadId] = thread;
        channelThreadMap[channelId] = threadMap;
      }
      logger.log({ upsertThread: channelThreadMap });
      workspaceState.channelThreadMap = channelThreadMap;
      state[workspaceId] = workspaceState;
    },

    upsertThreadMessage: (state, action: PayloadAction<{
      workspaceId: string,
      messageDto: MessageDto,
      channelId: string
    }>) => {
      const { messageDto, workspaceId, channelId } = action.payload;
      const threadId = messageDto.threadId as string;
      const workspaceState = state[workspaceId] || {};
      const threadMessageMap = { ...(workspaceState.threadMessageMap || {}) };
      if (threadId) {
        const messages = threadMessageMap?.[threadId] || {};
        messages[messageDto.messageId] = messageDto;
        threadMessageMap[threadId] = messages;
      }
      logger.log({ upsertThreadMessage: threadMessageMap });
      workspaceState.threadMessageMap = threadMessageMap;
      state[workspaceId] = workspaceState;

      slice.caseReducers.checkAndUpdateChannelLastActivity(state, {
        payload: {
          workspaceId,
          channelId,
          lastActivityDate: new Date()
        }, type: "messaging/checkAndUpdateChannelLastActivity"
      });

    },

    upsertAllThreadMessages: (state, action: PayloadAction<{
      workspaceId: string,
      messageDtoList: MessageDto[]
    }>) => {
      const { workspaceId, messageDtoList } = action.payload;
      const workspaceState = state[workspaceId] || {};
      const threadMessageMap = { ...(workspaceState.threadMessageMap || {}) };
      messageDtoList.forEach(messageDto => {
        const threadId = messageDto.threadId as string;
        if (threadId) {
          const messages = threadMessageMap?.[threadId] || {};
          messages[messageDto.messageId] = messageDto;
          threadMessageMap[threadId] = messages;
        }
      });
      logger.log({ upsertAllThreadMessages: threadMessageMap });

      workspaceState.threadMessageMap = threadMessageMap;
      state[workspaceId] = workspaceState;
    },

    upsertThreadMessageInfos: (state, action: PayloadAction<{
      workspaceId: string,
      threadMessageInfo: ThreadMessageInfoDto
    }>) => {
      const { workspaceId, threadMessageInfo } = action.payload;
      const workspaceState = state[workspaceId] || {};
      const threadMessageInfoMap = { ...(workspaceState.threadMessageInfoMap || {}) };
      threadMessageInfoMap[threadMessageInfo.threadId] = threadMessageInfo;
      workspaceState.threadMessageInfoMap = threadMessageInfoMap;
      state[workspaceId] = workspaceState;
    },

    checkAndUpdateChannelLastActivity: (state, action: PayloadAction<{
      workspaceId: string,
      channelId: string,
      lastActivityDate: Date
    }>) => {
      const { channelId, workspaceId, lastActivityDate } = action.payload;
      const workspaceState = state[workspaceId] || {};
      const channelLastActivityMap = { ...(workspaceState.channelLastActivityMap || {}) };
      const lastActivityDateInState = channelLastActivityMap[channelId];
      const requestedLastActivityDate = lastActivityDate;
      if (lastActivityDateInState && requestedLastActivityDate) {
        channelLastActivityMap[channelId] = isAfter(new Date(lastActivityDateInState), new Date(requestedLastActivityDate)) ? lastActivityDateInState : requestedLastActivityDate;
      } else {
        channelLastActivityMap[channelId] = requestedLastActivityDate;
      }
      workspaceState.channelLastActivityMap = channelLastActivityMap;
      state[workspaceId] = workspaceState;
    },

    checkAndUpdateChannelLastCheck: (state, action: PayloadAction<{
      workspaceId: string,
      channelId: string,
      lastCheckDate: Date
    }>) => {
      const { channelId, workspaceId, lastCheckDate } = action.payload;
      const workspaceState = state[workspaceId] || {};
      const channelLastCheckMap = { ...(workspaceState.channelLastCheckMap || {}) };
      const lastCheckInState = channelLastCheckMap[channelId];
      const requestedLastCheck = lastCheckDate;
      if (lastCheckInState && requestedLastCheck) {
        channelLastCheckMap[channelId] = isAfter(new Date(lastCheckInState), new Date(requestedLastCheck)) ? lastCheckInState : requestedLastCheck;
      } else {
        channelLastCheckMap[channelId] = requestedLastCheck;
      }
      workspaceState.channelLastCheckMap = channelLastCheckMap;
      state[workspaceId] = workspaceState;
    },

    checkAndUpdateConversationLastCheck: (state, action: PayloadAction<{
      workspaceId: string,
      conversationId: string,
      lastCheckDate: Date
    }>) => {
      const { conversationId, workspaceId, lastCheckDate } = action.payload;
      const workspaceState = state[workspaceId] || {};
      const conversationLastCheckMap = { ...(workspaceState.conversationLastCheckMap || {}) };
      const lastCheckInState = conversationLastCheckMap[conversationId];
      const requestedLastCheck = lastCheckDate;
      if (lastCheckInState && requestedLastCheck) {
        conversationLastCheckMap[conversationId] = isAfter(new Date(lastCheckInState), new Date(requestedLastCheck)) ? lastCheckInState : requestedLastCheck;
      } else {
        conversationLastCheckMap[conversationId] = requestedLastCheck;
      }
      workspaceState.conversationLastCheckMap = conversationLastCheckMap;
      state[workspaceId] = workspaceState;
    },

    upsertConversationMessage: (state, action: PayloadAction<{ workspaceId: string, messageDto: MessageDto }>) => {
      const { messageDto, workspaceId } = action.payload;
      const conversationId = messageDto.conversationId as string;
      const workspaceState = state[workspaceId] || {};
      const conversationMessageMap = { ...(workspaceState.conversationMessageMap || {}) };
      if (conversationId) {
        const messages = conversationMessageMap?.[conversationId] || {};
        messages[messageDto.messageId] = messageDto;
        conversationMessageMap[conversationId] = messages;
      }
      logger.log({ upsertConversationMessage: conversationMessageMap });
      workspaceState.conversationMessageMap = conversationMessageMap;
      state[workspaceId] = workspaceState;
    },

    upsertAllConversationMessages: (state, action: PayloadAction<{
      workspaceId: string,
      messageDtoList: MessageDto[]
    }>) => {
      const { workspaceId, messageDtoList } = action.payload;
      const workspaceState = state[workspaceId] || {};
      const conversationMessageMap = { ...(workspaceState.conversationMessageMap || {}) };
      messageDtoList.forEach(messageDto => {
        const conversationId = messageDto.conversationId as string;
        if (conversationId) {
          const messages = conversationMessageMap?.[conversationId] || {};
          messages[messageDto.messageId] = messageDto;
          conversationMessageMap[conversationId] = messages;
        }
      });
      logger.log({ upsertAllConversationMessages: conversationMessageMap });

      workspaceState.conversationMessageMap = conversationMessageMap;
      state[workspaceId] = workspaceState;
    },

    resetMessagingData: () => initialState
  },
  extraReducers: (builder) => {
    builder
      .addMatcher(messageOperationApi.endpoints.sendToThread.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const channelId = action.meta.arg.originalArgs.channelId;
        const sentMessage = action.payload.data;

        insertMessage(workspaceId, sentMessage);
        checkAndUpdateChannelLastCheckRepo({workspaceId, channelId, date: new Date() });
      })

      .addMatcher(messageOperationApi.endpoints.sendToConversation.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const sentMessage = action.payload.data;
        insertMessage(workspaceId, sentMessage);
        checkAndUpdateConversationLastCheckRepo({
          workspaceId,
          conversationId: sentMessage.conversationId || "",
          date: new Date(sentMessage.createdDate)
        });
      })

      .addMatcher(messageListingApi.endpoints.retrieveThreadMessages.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const channelId = action.meta.arg.originalArgs.channelId;
        const messagesPage = action.payload.data;

        insertAllMessages(workspaceId, messagesPage.content || []);
        checkAndUpdateChannelLastCheckRepo({workspaceId, channelId, date: new Date() });
      })

      .addMatcher(channelMemberApi.endpoints.retrieveChannelMemberships.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const channelMemberDtos = action.payload.data;

        channelMemberDtos.forEach(channelMember => {
          const channelInfo = channelMember?.channel?.channelInfo;
          if (channelInfo?.lastChannelActivity) {
            checkAndUpdateChannelLastActivityRepo({
              workspaceId,
              channelId: channelInfo.channelId,
              date: new Date(channelInfo.lastChannelActivity)
            });
          }
          checkAndUpdateChannelLastCheckRepo({
            workspaceId,
            channelId: channelInfo.channelId,
            date: new Date(channelMember.lastCheck)
          });

        });
      })

      .addMatcher(threadApi.endpoints.listThreads.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const threadsPage = action.payload.data;
        insertAllThreads(workspaceId, threadsPage?.content);
        threadsPage?.content?.forEach(thread => {
          const { initialMessage, latestMessage } = thread.threadMessageInfo;
          insertAllMessages(workspaceId, [initialMessage, latestMessage]);
        });
      })

      .addMatcher(threadApi.endpoints.retrieveThread.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const thread = action.payload.data;

        const { initialMessage, latestMessage } = thread.threadMessageInfo;
        insertAllMessages(workspaceId, [initialMessage, latestMessage]);
        insertThread(workspaceId, thread);
      })

      .addMatcher(messageListingApi.endpoints.retrieveConversationMessages.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const conversationId = action.meta.arg.originalArgs.conversationId;
        const messagesPage = action.payload.data;
        checkAndUpdateConversationLastCheckRepo({ workspaceId, conversationId, date: new Date() });
        insertAllMessages(workspaceId, messagesPage.content || []);
      })

      .addMatcher(conversationApi.endpoints.retrieveParticipatedConversations.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const participations = action.payload.data;
        participations.forEach(participation => {
          const conversationId = participation.conversationId;
          const participatedConversationMessageInfo = participation.conversation.conversationMessageInfo;
          const initialMessage = participatedConversationMessageInfo.initialMessage;
          const lastMessage = participatedConversationMessageInfo.lastMessage;

          checkAndUpdateConversationLastCheckRepo({
            workspaceId,
            conversationId: conversationId,
            date: new Date(participation.lastCheck)
          });
          // initialMessage do not insert initial message. so we can check the earliest message is initial message.
          insertMessage(workspaceId, lastMessage);
        });
      });
  }
});

export const {
  resetMessagingData
} = slice.actions;
export default slice.reducer;

export const selectConversationMessagesMap = (workspaceId: string) => (state: RootState) => state.messagingSlice[workspaceId]?.conversationMessageMap;

export const selectConversationLastCheckMap = (workspaceId: string) => (state: RootState) => state.messagingSlice[workspaceId]?.conversationLastCheckMap;
