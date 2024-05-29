import Dexie, { Table } from "dexie";
import { MessageDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";

const logger = Logger("MessageRepository");

export interface IMessageDto extends MessageDto {
  _timestamp: number;
}

export class MessageRepository extends Dexie {
  message!: Table<IMessageDto>;

  constructor() {
    super("messagesdb");
    const version = 1;
    this.version(version).stores({
      message: "++_id, &messageId, accountId, threadId, conversationId" // Primary key and indexed props
    });
    logger.log(`Messages Db initialized with version ${version}`);
  }
}

let __db: MessageRepository;

export const getDb = () => {
  if (!__db) {
    __db = new MessageRepository();
  }
  return __db;
};

export const insertMessage = async (message: MessageDto) => {
  if (!message) {
    return;
  }
  getDb().message.add({
    _timestamp: new Date(message.createdDate).getTime(),
    ...message
  });
};

export const insertAllMessages = async (messages: MessageDto[]) => {
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


export const deleteAllEntries = () => {
  getDb().message.clear();
};