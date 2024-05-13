"use client";
import React, { useEffect } from "react";
import { useRetrieveMessagingTokenQuery } from "@/api/messageTokenApi";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { selectAuthState } from "@/slice/accountSlice";
import { SOCKET_ROOT } from "@/utils/constants";
import { io } from "socket.io-client";
import Logger from "@/utils/logger";
import { upsertConversationMessage, upsertThreadMessage } from "@/slice/messagingSlice";
import { RichMessageDto } from "@/be/jinear-core";

interface WebsocketHandlerProps {

}

const logger = Logger("WebsocketHandler");

const WebsocketHandler: React.FC<WebsocketHandlerProps> = () => {
  const authState = useTypedSelector(selectAuthState);
  const dispatch = useAppDispatch();
  const { data: retrieveMessagingTokenResponse } = useRetrieveMessagingTokenQuery({}, { skip: authState != "LOGGED_IN" });

  useEffect(() => {
    if (retrieveMessagingTokenResponse) {
      const socket = io(SOCKET_ROOT, {
        path: "/ws",
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
            dispatch(upsertThreadMessage({
              workspaceId: data.thread.channel.workspaceId,
              messageDto: { ...data },
              channelId: data.thread.channelId
            }));
            logger.log({ SocketIoService: "thread-message", data });
          }
        }
      });

      socket.on("conversation-message", (message) => {
        if (message) {
          const data: RichMessageDto = JSON.parse(message);
          logger.log({ SocketIoService: "conversation-message", data });
          if (data) {
            dispatch(upsertConversationMessage({
              workspaceId: data.conversation.workspaceId,
              messageDto: data
            }));
            logger.log({ SocketIoService: "conversation-message", data });
          }
        }
      });

      socket.on("event:notification", (data) => {
        logger.log({ SocketIoService: "event:notification", data });
        // onNotification(data)
      });

    }
  }, [retrieveMessagingTokenResponse]);

  return null;
};

export default WebsocketHandler;