import React from "react";
import styles from "./ReplyMessage.module.css";
import useTranslation from "@/locals/useTranslation";
import ProfilePhoto from "@/components/profilePhoto";
import { format } from "date-fns";
import Tiptap from "@/components/tiptap/Tiptap";
import { MessageDto } from "@/be/jinear-core";

interface ReplyMessageProps {
  replyMessage: MessageDto;
}

const ReplyMessage: React.FC<ReplyMessageProps> = ({ replyMessage }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <div className={styles.profileInfo}>
        <ProfilePhoto
          boringAvatarKey={replyMessage.account?.accountId || ""}
          storagePath={replyMessage.account?.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        />
      </div>

      <div className={styles.messageBody}>
        <div className={styles.accountAndMessageInfoContainer}>
          <b>{replyMessage.account.username}</b>
          <div className={"spacer-w-1"} />
          <div>{format(new Date(replyMessage.createdDate), t("dateTimeFormat"))}</div>
        </div>

        <div className={styles.messageData}>
          <Tiptap
            content={replyMessage.richText?.value}
            className={styles.editor}
            editorClassName={styles.editor}
            editable={false}
          />
        </div>
      </div>
    </div>
  );
};

export default ReplyMessage;
