import React from "react";
import styles from "./InitialMessage.module.css";
import { MessageDto } from "@/be/jinear-core";
import Tiptap from "@/components/tiptap/Tiptap";
import ProfilePhoto from "@/components/profilePhoto";
import { format } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import Link from "next/link";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import { useLiveQuery } from "dexie-react-hooks";
import { getThreadInitialMessage, getThreadMessages } from "../../../../../repository/MessageRepository";

interface MessageProps {
  channelId: string;
  threadId: string;
  workspaceName: string;
  viewingAsDetail: boolean;
}

const InitialMessage: React.FC<MessageProps> = ({ channelId, threadId, workspaceName, viewingAsDetail }) => {
  const { t } = useTranslation();
  const message = useLiveQuery(() => getThreadInitialMessage(threadId));
  const Wrapper = viewingAsDetail ? ClientOnly : Link;
  return message == null ? null : (
    <Wrapper
      href={`/${workspaceName}/conversations/channel/${channelId}/thread/${message.threadId}`}
      className={styles.container}>
      <div className={styles.profileInfo}>
        <ProfilePhoto
          boringAvatarKey={message.account?.accountId || ""}
          storagePath={message.account?.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        />

        <div className={styles.accountAndMessageInfoContainer}>
          <b>{message.account.username}</b>
          <div>{format(new Date(message.createdDate), t("dateTimeFormat"))}</div>
        </div>
      </div>

      <div className={styles.messageBody}>
        <div className={styles.messageData}>
          <Tiptap
            content={message.richText?.value}
            className={styles.editor}
            editorClassName={styles.editor}
            editable={false}
          />
        </div>
      </div>
    </Wrapper>
  );
};

export default InitialMessage;