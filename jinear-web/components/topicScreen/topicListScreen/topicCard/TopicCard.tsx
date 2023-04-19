import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TopicDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPencil } from "react-icons/io5";
import styles from "./TopicCard.module.scss";

interface TopicCardProps {
  topic: TopicDto;
  workspaceName: string;
  teamUsername: string;
}

const TopicCard: React.FC<TopicCardProps> = ({ topic, workspaceName, teamUsername }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <Button className={styles.nameContainer} href={`/${workspaceName}/${teamUsername}/topic/related-tasks/${topic.tag}`}>
        <div className={styles.nameText}>{topic.name}</div>
      </Button>
      <div className={styles.actionContainer}>
        <Button
          href={`/${workspaceName}/${teamUsername}/topic/edit/${topic.topicId}`}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.hoverFilled2}
          data-tooltip-right={t("topicCardEditTooltip")}
        >
          <IoPencil />
        </Button>
      </div>
      <div className={styles.infoContainer}>
        <div className={styles.topicTag}>{topic.tag}</div>
        <input disabled type={"color"} value={`#${topic.color}`} />
      </div>
    </div>
  );
};

export default TopicCard;
