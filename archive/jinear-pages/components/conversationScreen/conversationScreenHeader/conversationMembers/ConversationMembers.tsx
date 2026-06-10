import React from "react";
import styles from "./ConversationMembers.module.css";
import {
  useOtherConversationParticipants
} from "@/hooks/messaging/conversationParticipant/useOtherConversationParticipants";
import ProfilePhoto from "@/components/profilePhoto";
import Logger from "@/utils/logger";

interface ConversationMembersProps {
  conversationId: string,
  workspaceId: string,
}

const MAX_PROFILE_PIC_COUNT = 3;
const logger = Logger("ConversationMembers");

const ConversationMembers: React.FC<ConversationMembersProps> = ({ conversationId, workspaceId }) => {
  const otherParticipants = useOtherConversationParticipants({ conversationId, workspaceId });
  const joinedName = otherParticipants?.map(participant => participant.account.username).join(", ");
  const unshownAvatarCount = (otherParticipants?.length || 0) - MAX_PROFILE_PIC_COUNT;

  return (
    <div className={styles.container}>
      <div className={styles.profilePicContainer}>
        {otherParticipants?.slice(0, MAX_PROFILE_PIC_COUNT).map((participant, i) =>
          <div key={participant.conversationParticipantId}
               data-tooltip={participant.account?.username}
               style={{ marginLeft: i == 0 ? 0 : -10, zIndex: i }}>
            <ProfilePhoto
              boringAvatarKey={participant.accountId}
              url={participant.account.profilePicture?.url}
              wrapperClassName={styles.profilePic}
            />
          </div>
        )}
        {unshownAvatarCount > 0 && <div className={styles.unshownAvatarCount}>{`+${unshownAvatarCount}`}</div>}
        <div className={"spacer-w-1"} />
      </div>
      <div className={styles.participantNamesContainer}>
        <b className={"flex-1"}>{joinedName}</b>
      </div>
    </div>
  );
};

export default ConversationMembers;