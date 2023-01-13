import { Topic } from "@/model/be/jinear-core";
import { getTextColor } from "@/utils/colorHelper";
import cn from "classnames";
import React from "react";
import styles from "./TopicInfo.module.css";

interface TopicInfoProps {
  topic: Topic;
}

const TopicInfo: React.FC<TopicInfoProps> = ({ topic }) => {
  return (
    <div
      className={cn(styles.container)}
      style={{
        backgroundColor: topic ? `#${topic?.color}` : undefined,
      }}
    >
      <b
        className="line-clamp"
        style={{
          textAlign: "center",
          color: topic ? getTextColor(`#${topic.color}`) : undefined,
        }}
      >
        {topic.name}
      </b>
    </div>
  );
};

export default TopicInfo;
