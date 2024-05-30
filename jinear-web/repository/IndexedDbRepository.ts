import Dexie, { Table } from "dexie";
import { MessageDto, ThreadDto, ThreadMessageInfoDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";

const logger = Logger("MessageRepository");

export interface IMessageDto extends MessageDto {
  _timestamp: number;
}

export interface IThreadDto extends ThreadDto {
  _timestamp: number;
  _last_activity_timestamp: number;
}

export interface IThreadMessageInfoDto extends ThreadMessageInfoDto {

}

export interface IThreadWithMessages extends IThreadDto {
  messages?: IMessageDto[];
}

export class IndexedDbRepository extends Dexie {
  message!: Table<IMessageDto>;
  thread!: Table<IThreadDto>;
  threadMessageInfo!: Table<IThreadMessageInfoDto>;

  constructor() {
    super("messagesdb");
    const version = 2;
    this.version(version).stores({
      // Primary key and indexed props
      message: "++_id, &messageId, accountId, threadId, conversationId",
      thread: "++_id, &threadId, channelId",
      threadMessageInfo: "++_id, &threadId"
    });
    logger.log(`Messages Db initialized with version ${version}`);
  }
}

let __db: IndexedDbRepository;

export const getDb = () => {
  if (!__db) {
    __db = new IndexedDbRepository();
  }
  return __db;
};

export const insertMessage = (message: MessageDto) => {
  if (!message) {
    return;
  }
  getDb().message.add({
    _timestamp: new Date(message.createdDate).getTime(),
    ...message
  });
};

export const insertAllMessages = (messages: MessageDto[]) => {
  getDb().message.bulkPut(messages.map(message => {
    return {
      _timestamp: new Date(message.createdDate).getTime(),
      ...message
    };
  }));
};

export const getConversationLastMessage = async (conversationId: string) => {
  const messages = await getDb().message
    .where("conversationId")
    .equals(conversationId)
    .sortBy("_timestamp");
  return messages?.[0];
};

export const getConversationMessages = async (conversationId: string) => {
  return getDb().message.filter(message => message.conversationId == conversationId).reverse().sortBy("_timestamp");
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

export const insertThread = (thread: ThreadDto) => {
  if (!thread) {
    return;
  }
  getDb().thread.add({
    _timestamp: new Date(thread.createdDate).getTime(),
    _last_activity_timestamp: new Date(thread.lastActivityTime).getTime(),
    ...thread
  });
  const threadMessageInfo = thread.threadMessageInfo;
  getDb().threadMessageInfo.add({ ...threadMessageInfo });
};

export const insertAllThreads = (threads: ThreadDto[]) => {
  if (!threads) {
    return;
  }
  getDb().thread.bulkPut(threads.map(thread => {
    return {
      _timestamp: new Date(thread.createdDate).getTime(),
      _last_activity_timestamp: new Date(thread.lastActivityTime).getTime(),
      ...thread
    };
  }));
  getDb().threadMessageInfo.bulkPut(threads.map(thread => thread.threadMessageInfo));
};

export const getThreads = async (channelId: string) => {
  return getDb().thread.where("channelId").equals(channelId).reverse().sortBy("_last_activity_timestamp");
};

export const getThreadsWithMessages = async (channelId: string): Promise<IThreadWithMessages[]> => {
  const threads = await getDb().thread.where("channelId").equals(channelId).reverse().sortBy("_last_activity_timestamp");
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

export const deleteAllEntries = () => {
  getDb().message.clear();
  getDb().thread.clear();
  getDb().threadMessageInfo.clear();
};