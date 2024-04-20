import React from "react";
import styles from "./Message.module.css";
import { MessageDto } from "@/be/jinear-core";
import Tiptap from "@/components/tiptap/Tiptap";
import ProfilePhoto from "@/components/profilePhoto";
import { format } from "date-fns";
import useTranslation from "@/locals/useTranslation";

interface MessageProps {
  message: MessageDto;
}

const Message: React.FC<MessageProps> = ({ message }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
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
    </div>
  );
};

export default Message;