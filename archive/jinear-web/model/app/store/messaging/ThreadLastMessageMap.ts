import { MessageDto, ThreadDto, ThreadMessageInfoDto } from "@/be/jinear-core";

export interface MessageMap {
  [messageId: string]: MessageDto;
}

export interface IChannelThreadMap {
  [channelId: string]: IThreadMap;
}

export interface IThreadMap {
  [threadId: string]: ThreadDto;
}

export interface IThreadMessageMap {
  [threadId: string]: MessageMap;
}

export interface IThreadMessageInfoMap {
  [threadId: string]: ThreadMessageInfoDto;
}

export interface IConversationMessageMap {
  [conversationId: string]: MessageMap;
}

export interface IConversationLastCheckMap {
  [conversationId: string]: Date;
}

export interface IChannelDateMap {
  [channelId: string]: Date;
}