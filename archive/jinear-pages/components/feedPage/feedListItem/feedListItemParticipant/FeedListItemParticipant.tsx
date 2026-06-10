import { FeedItemParticipant } from "@/model/be/jinear-core";
import React from "react";
import styles from "./FeedListItemParticipant.module.css";

interface FeedListItemParticipantProps {
  participant: FeedItemParticipant;
  index: number;
}

const FeedListItemParticipant: React.FC<FeedListItemParticipantProps> = ({ participant, index }) => {
  const name = participant?.name ? participant.name : participant.address;
  return (
    <>
      {index != 0 && " | "}
      <div className={styles.container}>
        <div className={styles.name} data-tooltip-right={participant.address}>
          {name}
        </div>
        {/* <div className={styles.address}>{address}</div> */}
      </div>
    </>
  );
};

export default FeedListItemParticipant;
