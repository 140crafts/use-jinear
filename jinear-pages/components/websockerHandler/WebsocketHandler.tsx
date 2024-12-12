"use client";
import React, { useEffect } from "react";
import { useRetrieveMessagingTokenQuery } from "@/api/messageTokenApi";
import { useTypedSelector } from "@/store/store";
import { selectAuthState } from "@/slice/accountSlice";
import { SOCKET_ROOT } from "@/utils/constants";
import { io } from "socket.io-client";
import Logger from "@/utils/logger";
import { RichMessageDto } from "@/be/jinear-core";
import { insertMessage } from "../../repository/IndexedDbRepository";
import { useFeatureFlag } from "@/hooks/useFeatureFlag";

interface WebsocketHandlerProps {

}

const logger = Logger("WebsocketHandler");

const WebsocketHandler: React.FC<WebsocketHandlerProps> = () => {
  const authState = useTypedSelector(selectAuthState);
  // const messagingEnabled = useFeatureFlag("MESSAGING");
  const messagingEnabled = false;
  const { data: retrieveMessagingTokenResponse } = useRetrieveMessagingTokenQuery({}, { skip: authState != "LOGGED_IN" });

  useEffect(() => {
    if (retrieveMessagingTokenResponse && messagingEnabled) {
      const socket = io(SOCKET_ROOT, {
        path: "/ws",
        withCredentials: true,
        extraHeaders: { "X-TOKEN": retrieveMessagingTokenResponse.data.token }
      });

      socket.on("connect", () => {
        logger.log({ SocketIoService: "onConnect", connected: socket.connected });
      });

      socket.on("disconnect", () => {
        logger.log({ SocketIoService: "onDisconnect" });
      });

      socket.on("thread-message", (message) => {
        if (message) {
          const data: RichMessageDto = JSON.parse(message);
          logger.log({ SocketIoService: "thread-message", data });
          if (data) {
            insertMessage(data.thread.channel.workspaceId, { ...data });
            logger.log({ SocketIoService: "thread-message", data });
          }
        }
      });

      socket.on("conversation-message", (message) => {
        if (message) {
          const data: RichMessageDto = JSON.parse(message);
          logger.log({ SocketIoService: "conversation-message", data });
          if (data?.conversationId) {
            insertMessage(data.conversation.workspaceId, { ...data });
            logger.log({ SocketIoService: "conversation-message", data });
          }
        }
      });

      socket.on("event:notification", (data) => {
        logger.log({ SocketIoService: "event:notification", data });
        // onNotification(data)
      });

    }
  }, [retrieveMessagingTokenResponse, messagingEnabled]);

  return null;
};

export default WebsocketHandler;