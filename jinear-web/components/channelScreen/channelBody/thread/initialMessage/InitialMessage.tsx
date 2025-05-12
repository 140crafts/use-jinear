import React from "react";
import styles from "./InitialMessage.module.css";
import Tiptap from "@/components/tiptap/Tiptap";
import ProfilePhoto from "@/components/profilePhoto";
import { format } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import Link from "next/link";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import { useLiveQuery } from "dexie-react-hooks";
import { getThreadInitialMessage } from "../../../../../repository/IndexedDbRepository";
import { BsRobot } from "react-icons/bs";
import ProfilePhotoRobot from "@/components/profilePhotoRobot/ProfilePhotoRobot";

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
        {message.account?.accountId ?
          <ProfilePhoto
            boringAvatarKey={message.account?.accountId || ""}
            url={message.account?.profilePicture?.url}
            wrapperClassName={styles.profilePic}
          /> :
          <ProfilePhotoRobot wrapperClassName={styles.profilePic} />
        }

        <div className={styles.accountAndMessageInfoContainer}>
          <b>{message?.account?.username || message?.robot?.robotName}</b>
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