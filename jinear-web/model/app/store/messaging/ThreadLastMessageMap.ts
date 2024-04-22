import { MessageDto } from "@/be/jinear-core";

export interface ThreadLastMessageMap {
  [threadId: string]: MessageDto;
}