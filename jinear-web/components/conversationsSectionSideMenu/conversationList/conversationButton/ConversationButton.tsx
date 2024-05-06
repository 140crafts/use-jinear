import React, { useMemo } from "react";
import styles from "./ConversationButton.module.css";
import Button, { ButtonHeight } from "@/components/button";
import cn from "classnames";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import { ConversationDto, PlainConversationParticipantDto } from "@/be/jinear-core";
import ProfilePhoto from "@/components/profilePhoto";
import {
  useConversationHasUnreadMessages
} from "@/hooks/messaging/conversationMessage/useConversationHasUnreadMessages";

interface ConversationButtonProps {
  conversation: ConversationDto;
  currentAccountId: string;
  workspaceUsername: string;
}

const MAX_CHAR = 25;
const JOIN_CHAR = ",";

const ConversationButton: React.FC<ConversationButtonProps> = ({
                                                                 workspaceUsername,
                                                                 conversation,
                                                                 currentAccountId
                                                               }) => {
  const participantsInfo = useMemo(() => {
    let joinedName = "";
    let shownParticipants: PlainConversationParticipantDto[] = [];
    let remaining = 0;
    const participants = conversation.participants;
    for (let i = 0; i < participants.length; i++) {
      const participant = participants[i];
      if (participant.accountId == currentAccountId) {
        continue;
      }
      const participantUsername = participant.account.username;
      const currJoinChar = i == 0 ? "" : JOIN_CHAR;
      if (joinedName.length + currJoinChar.length + participantUsername.length < MAX_CHAR) {
        joinedName += currJoinChar + participantUsername;
        shownParticipants.push(participant);
      } else {
        remaining = participants.length - i;
        break;
      }
    }

    return { joinedName, shownParticipants, remaining };
  }, [conversation, currentAccountId]);

  const isUnread = useConversationHasUnreadMessages({
    conversationId: conversation.conversationId,
    workspaceId: conversation.workspaceId
  });

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x}
            href={`/${workspaceUsername}/conversations/${conversation.conversationId}`}>
      <div className={styles.profilePicContainer}>
        {participantsInfo.shownParticipants.map(participant => <ProfilePhoto
          key={participant.conversationParticipantId}
          boringAvatarKey={participant.accountId}
          storagePath={participant.account.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        />)}
      </div>

      <span className={cn(styles.conversationName, "single-line", isUnread ? styles.unread : undefined)}>
        {shortenStringIfMoreThanMaxLength({
          text: participantsInfo.joinedName,
          maxLength: 29
        })}
      </span>
      {participantsInfo.remaining != 0 && <b>{`+${participantsInfo.remaining}`}</b>}
    </Button>
  );
};

export default ConversationButton;