import Dexie, { Table } from "dexie";
import { MessageDto, ThreadDto, ThreadMessageInfoDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import { isAfter, isBefore } from "date-fns";

const logger = Logger("MessageRepository");

export interface IMessageDto extends MessageDto {
  _timestamp: number;
  workspaceId: string;
}

export interface IThreadDto extends ThreadDto {
  _timestamp: number;
  _last_activity_timestamp: number;
  workspaceId: string;
}

export interface IThreadMessageInfoDto extends ThreadMessageInfoDto {
  workspaceId: string;
}

export interface IThreadWithMessages extends IThreadDto {
  messages?: IMessageDto[];
}

export interface ILastCheckInfo {
  workspaceId: string;
  kind: "conversation-last-check" | "channel-last-check" | "channel-last-activity";
  _timestamp: number;
  conversationId?: string;
  channelId?: string;
}

const VERSION = 5;
const NAME = "messages-db-";

export class IndexedDbRepository extends Dexie {
  message!: Table<IMessageDto>;
  thread!: Table<IThreadDto>;
  threadMessageInfo!: Table<IThreadMessageInfoDto>;
  lastCheckInfo!: Table<ILastCheckInfo>;

  constructor() {
    super(`${NAME}${VERSION}`);
    this.version(VERSION).stores({
      // Primary key and indexed props
      message: "messageId, accountId, threadId, conversationId",
      thread: "threadId, channelId",
      threadMessageInfo: "threadId",
      lastCheckInfo: "++_id, conversationId, channelId"
    });
    logger.log(`Messages Db initialized with version ${VERSION}`);
  }
}

export const deleteAllIndexedDbs = () => {
  if (typeof window === "object") {
    for (let i = 0; i <= VERSION; i++) {
      try {
        window.indexedDB.deleteDatabase(`${NAME}${VERSION}`);
      } catch (e) {
        console.error(e);
      }
    }
  }
};

let __db: IndexedDbRepository;

export const getDb = () => {
  if (!__db) {
    __db = new IndexedDbRepository();
  }
  return __db;
};

export const insertMessage = (workspaceId: string, message: MessageDto) => {
  if (!message) {
    return;
  }
  getDb().message.add({
    _timestamp: new Date(message.createdDate).getTime(),
    workspaceId,
    ...message
  });
};

export const insertAllMessages = (workspaceId: string, messages: MessageDto[]) => {
  getDb().message.bulkPut(messages.filter(message => message != null).map(message => {
    return {
      workspaceId,
      _timestamp: new Date(message.createdDate).getTime(),
      ...message
    };
  }));
};

export const getConversationMessages = async (conversationId: string) => {
  return getDb().message.filter(message => message.conversationId == conversationId).reverse().sortBy("_timestamp");
};

export const getConversationEarliestMessage = async (conversationId: string) => {
  const messages = await getDb().message
    .filter(message => message.conversationId == conversationId)
    .sortBy("_timestamp");
  logger.log({ conversationId, getConversationLastMessage: messages });
  return messages?.[0];
};

export const getConversationLastMessage = async (conversationId: string) => {
  const messages = await getDb().message
    .filter(message => message.conversationId == conversationId)
    .reverse()
    .sortBy("_timestamp");
  logger.log({ conversationId, getConversationLastMessage: messages });
  return messages?.[0];
};

export const getSortedConversationIds = async () => {
  const allConversationMessagesSorted = await getDb().message
    .filter(message => message.conversationId != null)
    .reverse()
    .sortBy("_timestamp");
  const distinctConversationIds: string[] = [];
  allConversationMessagesSorted.forEach(message => {
    if (message.conversationId && distinctConversationIds.indexOf(message.conversationId) == -1) {
      distinctConversationIds.push(message.conversationId);
    }
  });
  return distinctConversationIds || [];
};

export const getThreadMessages = async (threadId: string) => {
  return getDb().message.filter(message => message.threadId == threadId).reverse().sortBy("_timestamp");
};

export const getThreadInitialMessage = async (threadId: string) => {
  const messages = await getDb().message.filter(message => message.threadId == threadId).sortBy("_timestamp");
  return messages?.[0];
};

export const getThreadFirstReplyMessage = async (threadId: string) => {
  const messages = await getDb().message.filter(message => message.threadId == threadId).sortBy("_timestamp");
  return messages?.[1];
};

export const getThreadLastMessage = async (threadId: string) => {
  const messages = await getDb().message.filter(message => message.threadId == threadId).reverse().sortBy("_timestamp");
  return messages?.[0];
};

export const insertThread = (workspaceId: string, thread: ThreadDto) => {
  if (!thread) {
    return;
  }
  getDb().thread.add({
    workspaceId,
    _timestamp: new Date(thread.createdDate).getTime(),
    _last_activity_timestamp: new Date(thread.lastActivityTime).getTime(),
    ...thread
  });
  const threadMessageInfo = thread.threadMessageInfo;
  getDb().threadMessageInfo.add({ workspaceId, ...threadMessageInfo });
};

export const insertAllThreads = (workspaceId: string, threads: ThreadDto[]) => {
  if (!threads) {
    return;
  }
  getDb().thread.bulkPut(threads.map(thread => {
    return {
      workspaceId,
      _timestamp: new Date(thread.createdDate).getTime(),
      _last_activity_timestamp: new Date(thread.lastActivityTime).getTime(),
      ...thread
    };
  }));
  getDb().threadMessageInfo.bulkPut(threads.map(thread => {
    return {
      workspaceId,
      ...thread.threadMessageInfo
    };
  }));
};

export const getThreads = async (channelId: string) => {
  return getDb().thread.filter(thread => thread.channelId == channelId).reverse().sortBy("_last_activity_timestamp");
};

export const getThreadsWithMessages = async (channelId: string): Promise<IThreadWithMessages[]> => {
  const threads = await getDb().thread.filter(thread => thread.channelId == channelId).reverse().sortBy("_last_activity_timestamp");
  threads?.map(thread => getThreadMessages);
  return await Promise.all(threads.map(async thread => {
    const messages = await getThreadMessages(thread.threadId);
    return { ...thread, messages };
  }));
};

export const getThreadWithMessages = async (threadId: string): Promise<IThreadWithMessages | undefined> => {
  const thread = await getDb().thread.filter(thread => thread.threadId == threadId).first();
  const messages = await getThreadMessages(threadId);
  if (thread) {
    return { ...thread, messages };
  }
};

export const getThreadMessageInfo = (threadId: string) => {
  return getDb().threadMessageInfo.filter(threadMessageInfo => threadMessageInfo.threadId == threadId).first();
};

export const getConversationLastCheck = async (conversationId: string) => {
  return getDb().lastCheckInfo.filter(lastCheckInfo => (lastCheckInfo.kind == "conversation-last-check") && (lastCheckInfo.conversationId == conversationId)).first();
};

export const getChannelLastCheck = async (channelId: string) => {
  return getDb().lastCheckInfo.filter(lastCheckInfo => (lastCheckInfo.kind == "channel-last-check") && (lastCheckInfo.channelId == channelId)).first();
};

export const getChannelLastActivity = async (channelId: string) => {
  return getDb().lastCheckInfo.filter(lastCheckInfo => (lastCheckInfo.kind == "channel-last-activity") && (lastCheckInfo.channelId == channelId)).first();
};

export const checkAndUpdateConversationLastCheck = async ({ workspaceId, conversationId, date = new Date(0) }: {
  workspaceId: string,
  conversationId: string,
  date?: Date
}) => {
  const kind = "conversation-last-check";
  const lastCheckInfo = await getDb().lastCheckInfo.filter(lastCheckInfo => lastCheckInfo.kind == kind && lastCheckInfo.conversationId == conversationId).first();
  const lastCheckTs = compareAndRetrieveLatest(lastCheckInfo, date);

  await getDb().lastCheckInfo.put({
    ...lastCheckInfo,
    workspaceId,
    kind,
    conversationId,
    _timestamp: lastCheckTs
  });

};

export const checkAndUpdateChannelLastCheck = async ({ workspaceId, channelId, date = new Date(0) }: {
  workspaceId: string,
  channelId: string,
  date?: Date
}) => {
  const kind = "channel-last-check";
  const lastCheckInfo = await getDb().lastCheckInfo.filter(lastCheckInfo => lastCheckInfo.kind == kind && lastCheckInfo.channelId == channelId).first();
  const lastCheckTs = compareAndRetrieveLatest(lastCheckInfo, date);
  getDb().lastCheckInfo.put({
    ...lastCheckInfo,
    workspaceId,
    kind,
    channelId,
    _timestamp: lastCheckTs
  });
};

export const checkAndUpdateChannelLastActivity = async ({ workspaceId, channelId, date = new Date(0) }: {
  workspaceId: string,
  channelId: string,
  date?: Date
}) => {
  const kind = "channel-last-activity";
  const lastCheckInfo = await getDb().lastCheckInfo.filter(lastCheckInfo => lastCheckInfo.kind == kind && lastCheckInfo.channelId == channelId).first();
  const lastCheckTs = compareAndRetrieveLatest(lastCheckInfo, date);
  getDb().lastCheckInfo.put({
    ...lastCheckInfo,
    workspaceId,
    kind,
    channelId,
    _timestamp: lastCheckTs
  });
};

export const getUnreadConversationCount = async (workspaceId: string, currentAccountId?: string) => {
  if (!currentAccountId) {
    return 0;
  }
  const conversationIds = new Set<string>();
  await getDb().message.filter(message => message.conversationId != null).each(message => message.conversationId && conversationIds.add(message.conversationId));
  const unreadArr = await Promise.all(Array.from(conversationIds).map(async conversationId => {
    const conversationLastMessage = await getConversationLastMessage(conversationId);
    const conversationLastCheck = await getConversationLastCheck(conversationId);
    const result = conversationLastMessage && conversationLastCheck && currentAccountId != null && conversationLastMessage.accountId != currentAccountId && isBefore(new Date(conversationLastCheck._timestamp), new Date(conversationLastMessage._timestamp));
    logger.log({ conversationLastMessage, conversationLastCheck, result, currentAccountId });
    return result;
  }));
  let count = 0;
  logger.log({ unreadArr });
  unreadArr.forEach(value => {
    if (value) {
      count++;
    }
  });
  return count;
};

export const deleteAllEntries = () => {
  getDb().message.clear();
  getDb().thread.clear();
  getDb().threadMessageInfo.clear();
};

const compareAndRetrieveLatest = (lastCheckInfo: ILastCheckInfo | undefined, date: Date) => {
  if (lastCheckInfo) {
    return isAfter(new Date(lastCheckInfo._timestamp), date) ? lastCheckInfo._timestamp : date.getTime();
  }
  return date.getTime();
};