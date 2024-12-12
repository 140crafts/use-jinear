import React from "react";
import styles from "./ThreadMessage.module.css";
import useTranslation from "@/locals/useTranslation";
import ProfilePhoto from "@/components/profilePhoto";
import { format } from "date-fns";
import Tiptap from "@/components/tiptap/Tiptap";
import { MessageDto } from "@/be/jinear-core";
import { BsRobot } from "react-icons/bs";
import ProfilePhotoRobot from "@/components/profilePhotoRobot/ProfilePhotoRobot";

interface ThreadMessageProps {
  threadMessage: MessageDto;
}

const ThreadMessage: React.FC<ThreadMessageProps> = ({ threadMessage }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <div className={styles.profileInfo}>
        {threadMessage.account?.accountId ?
          <ProfilePhoto
            boringAvatarKey={threadMessage.account?.accountId || ""}
            storagePath={threadMessage.account?.profilePicture?.storagePath}
            wrapperClassName={styles.profilePic}
          /> :
          <ProfilePhotoRobot wrapperClassName={styles.profilePic} />
        }
      </div>

      <div className={styles.messageBody}>
        <div className={styles.accountAndMessageInfoContainer}>
          <b>{threadMessage?.account?.username || threadMessage?.robot?.robotName}</b>
          <div className={"spacer-w-1"} />
          <div>{format(new Date(threadMessage.createdDate), t("dateTimeFormat"))}</div>
        </div>

        <div className={styles.messageData}>
          <Tiptap
            content={threadMessage.richText?.value}
            className={styles.editor}
            editorClassName={styles.editor}
            editable={false}
          />
        </div>
      </div>
    </div>
  );
};

export default ThreadMessage;
