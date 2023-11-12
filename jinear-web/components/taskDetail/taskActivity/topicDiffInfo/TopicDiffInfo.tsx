import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TopicDiffInfo.module.css";

interface TopicDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const MAX_CHAR = 36;

const TopicDiffInfo: React.FC<TopicDiffInfoProps> = ({ activity }) => {
  const oldTopicTooLong = activity.oldTopicDto && activity.oldTopicDto.name.length > 36;
  const oldTopic = activity.oldTopicDto?.name
    ? activity.oldTopicDto?.name?.substring(0, MAX_CHAR) + (oldTopicTooLong ? "..." : "")
    : " - ";

  const newTopicTooLong = activity.newTopicDto && activity.newTopicDto.name.length > 36;
  const newTopic = activity.newTopicDto?.name
    ? activity.newTopicDto?.name?.substring(0, MAX_CHAR) + (newTopicTooLong ? "..." : "")
    : " - ";

  return (
    <div className={styles.container}>
      <div className={styles.cell} data-tooltip-multiline={oldTopicTooLong ? activity.oldTopicDto?.name : undefined}>
        {oldTopic}
      </div>
      {"->"}
      <div className={styles.cell} data-tooltip-multiline={newTopicTooLong ? activity.newTopicDto?.name : undefined}>
        {newTopic}
      </div>
    </div>
  );
};

export default TopicDiffInfo;
