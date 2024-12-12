import React from "react";
import styles from "./Message.module.css";
import { MessageDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import ProfilePhoto from "@/components/profilePhoto";
import { format, isSameDay } from "date-fns";
import Tiptap from "@/components/tiptap/Tiptap";
import cn from "classnames";

interface MessageProps {
  message: MessageDto;
  renderMessageFrom: boolean;
}

const Message: React.FC<MessageProps> = ({ message, renderMessageFrom }) => {
  const { t } = useTranslation();
  const sentToday = isSameDay(new Date(), new Date(message.createdDate));
  const messageTime = format(new Date(message.createdDate), t("timeFormat"));
  const messageFullDate = format(new Date(message.createdDate), t("dateTimeFormat"));

  return (
    <div className={cn(styles.container, !renderMessageFrom && styles.small)}>
      <div className={styles.profileInfo}>
        {renderMessageFrom ? <ProfilePhoto
          boringAvatarKey={message.account?.accountId || ""}
          storagePath={message.account?.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        /> : <div className={styles.leftTimeText} data-tooltip={messageFullDate}>{messageTime}</div>}
      </div>

      <div className={styles.messageBody}>
        {renderMessageFrom && (
          <div className={styles.accountAndMessageInfoContainer}>
            <b className={styles.username}>{message.account.username}</b>
            <div className={"spacer-w-1"} />
            <div className={styles.date}
                 data-tooltip={sentToday ? messageFullDate : undefined}>
              {sentToday ? messageTime : messageFullDate}
            </div>
          </div>
        )}

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