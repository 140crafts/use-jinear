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
import { insertAllMessages, insertMessage } from "../../repository/MessageRepository";

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
        slice.caseReducers.upsertThreadMessage(state, {
          payload: { workspaceId, messageDto: sentMessage, channelId },
          type: "messaging/upsertThreadMessage"
        });
        slice.caseReducers.checkAndUpdateChannelLastActivity(state, {
          payload: { workspaceId, channelId, lastActivityDate: new Date() },
          type: "messaging/checkAndUpdateChannelLastActivity"
        });
        insertMessage(sentMessage);
      })

      .addMatcher(messageOperationApi.endpoints.sendToConversation.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const sentMessage = action.payload.data;
        slice.caseReducers.upsertConversationMessage(state, {
          payload: { workspaceId, messageDto: sentMessage },
          type: "messaging/upsertThreadMessage"
        });
        insertMessage(sentMessage);
      })

      .addMatcher(messageListingApi.endpoints.retrieveThreadMessages.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const channelId = action.meta.arg.originalArgs.channelId;
        const messagesPage = action.payload.data;

        slice.caseReducers.upsertAllThreadMessages(state, {
          payload: {
            workspaceId,
            messageDtoList: messagesPage.content || []
          }, type: "messaging/upsertAllThreadMessages"
        });
        slice.caseReducers.checkAndUpdateChannelLastCheck(state, {
          payload: {
            workspaceId,
            channelId,
            lastCheckDate: new Date()
          }, type: "messaging/checkAndUpdateChannelLastCheck"
        });
        insertAllMessages(messagesPage.content || []);
      })

      .addMatcher(channelMemberApi.endpoints.retrieveChannelMemberships.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const channelMemberDtos = action.payload.data;

        channelMemberDtos.map(channelMember => {
          const channelInfo = channelMember?.channel?.channelInfo;
          slice.caseReducers.checkAndUpdateChannelLastCheck(state, {
            payload: {
              workspaceId,
              channelId: channelInfo.channelId,
              lastCheckDate: channelMember.lastCheck
            }, type: "messaging/checkAndUpdateChannelLastActivity"
          });

          if (channelInfo?.lastChannelActivity) {
            slice.caseReducers.checkAndUpdateChannelLastActivity(state, {
              payload: {
                workspaceId,
                channelId: channelInfo.channelId,
                lastActivityDate: new Date(channelInfo.lastChannelActivity)
              }, type: "messaging/checkAndUpdateChannelLastActivity"
            });
          }
        });
      })

      .addMatcher(threadApi.endpoints.listThreads.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const threadsPage = action.payload.data;

        threadsPage?.content?.map(thread => {
          const { initialMessage, latestMessage } = thread.threadMessageInfo;
          slice.caseReducers.upsertAllThreadMessages(state, {
            payload: {
              workspaceId,
              messageDtoList: [initialMessage, latestMessage]
            }, type: "messaging/upsertAllThreadMessages"
          });
          slice.caseReducers.upsertThreadMessageInfos(state, {
            payload: {
              workspaceId,
              threadMessageInfo: thread.threadMessageInfo
            }, type: "messaging/upsertThreadMessageInfos"
          });
          slice.caseReducers.upsertThread(state, {
            payload: {
              workspaceId,
              thread
            }, type: "messaging/upsertThread"
          });
          insertAllMessages([initialMessage, latestMessage]);
        });

      })

      .addMatcher(threadApi.endpoints.retrieveThread.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const thread = action.payload.data;

        const { initialMessage, latestMessage } = thread.threadMessageInfo;
        slice.caseReducers.upsertAllThreadMessages(state, {
          payload: {
            workspaceId,
            messageDtoList: [initialMessage, latestMessage]
          }, type: "messaging/upsertAllThreadMessages"
        });
        slice.caseReducers.upsertThreadMessageInfos(state, {
          payload: {
            workspaceId,
            threadMessageInfo: thread.threadMessageInfo
          }, type: "messaging/upsertThreadMessageInfos"
        });
        insertAllMessages([initialMessage, latestMessage]);
      })

      .addMatcher(messageListingApi.endpoints.retrieveConversationMessages.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const conversationId = action.meta.arg.originalArgs.conversationId;
        const messagesPage = action.payload.data;

        slice.caseReducers.upsertAllConversationMessages(state, {
          payload: {
            workspaceId,
            messageDtoList: messagesPage.content || []
          }, type: "messaging/checkAndUpdateConversationLastCheck"
        });
        slice.caseReducers.checkAndUpdateConversationLastCheck(state, {
          payload: {
            workspaceId,
            conversationId,
            lastCheckDate: new Date()
          }, type: "messaging/checkAndUpdateConversationLastCheck"
        });

        insertAllMessages(messagesPage.content || []);
      })

      .addMatcher(conversationApi.endpoints.retrieveParticipatedConversations.matchFulfilled, (state, action) => {
        const workspaceId = action.meta.arg.originalArgs.workspaceId;
        const participations = action.payload.data;
        participations.forEach(participation => {
          const conversationId = participation.conversationId;
          const participatedConversationMessageInfo = participation.conversation.conversationMessageInfo;
          const initialMessage = participatedConversationMessageInfo.initialMessage;
          const lastMessage = participatedConversationMessageInfo.lastMessage;

          slice.caseReducers.checkAndUpdateConversationLastCheck(state, {
            payload: {
              workspaceId,
              conversationId,
              lastCheckDate: participation.lastCheck
            }, type: "messaging/checkAndUpdateConversationLastCheck"
          });
          // initialMessage do not insert initial message. so we can check the earliest message is initial message.
          slice.caseReducers.upsertAllConversationMessages(state, {
            payload: {
              workspaceId,
              messageDtoList: [lastMessage]
            }, type: "messaging/upsertAllConversationMessages"
          });
          insertMessage(lastMessage);
        });
      });
  }
});

export const {
  checkAndUpdateChannelLastCheck,
  checkAndUpdateConversationLastCheck,
  upsertThreadMessage,
  upsertConversationMessage,
  upsertAllConversationMessages,
  resetMessagingData
} = slice.actions;
export default slice.reducer;

export const selectChannelThreads = ({ workspaceId, channelId }: {
  workspaceId: string,
  channelId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.channelThreadMap?.[channelId];

export const selectChannelLastCheck = ({ workspaceId, channelId }: {
  workspaceId: string,
  channelId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.channelLastCheckMap?.[channelId];

export const selectChannelLastActivity = ({ workspaceId, channelId }: {
  workspaceId: string,
  channelId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.channelLastActivityMap?.[channelId];


export const selectThreadMessages = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.threadMessageMap?.[threadId];

export const selectThreadMessageInfo = ({ workspaceId, threadId }: {
  workspaceId: string,
  threadId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.threadMessageInfoMap?.[threadId];

export const selectConversationMessages = ({ workspaceId, conversationId }: {
  workspaceId: string,
  conversationId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.conversationMessageMap?.[conversationId];

export const selectConversationMessagesMap = (workspaceId: string) => (state: RootState) => state.messagingSlice[workspaceId]?.conversationMessageMap;

export const selectConversationLastCheck = ({ workspaceId, conversationId }: {
  workspaceId: string,
  conversationId: string
}) => (state: RootState) => state.messagingSlice[workspaceId]?.conversationLastCheckMap[conversationId];

export const selectConversationLastCheckMap = (workspaceId: string) => (state: RootState) => state.messagingSlice[workspaceId]?.conversationLastCheckMap;
