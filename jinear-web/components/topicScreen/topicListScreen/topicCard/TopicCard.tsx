import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TopicDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPencil } from "react-icons/io5";
import styles from "./TopicCard.module.scss";

interface TopicCardProps {
  topic: TopicDto;
  workspaceName: string;
  teamName: string;
}

const TopicCard: React.FC<TopicCardProps> = ({
  topic,
  workspaceName,
  teamName,
}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <div className={styles.nameContainer}>{topic.name}</div>
      <div className={styles.actionContainer}>
        <Button
          href={`/${workspaceName}/${teamName}/topic/edit/${topic.topicId}`}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.hoverFilled2}
          data-tooltip-right={t("topicCardEditTooltip")}
        >
          <IoPencil />
        </Button>
      </div>
      <div className={styles.topicTag}>{topic.tag}</div>
      <input disabled type={"color"} value={`#${topic.color}`} />
    </div>
  );
};

export default TopicCard;
