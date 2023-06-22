import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, TopicDto, WorkspaceDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import styles from "./TopicItemButton.module.css";

interface TopicItemButtonProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  topic: TopicDto;
}

const TopicItemButton: React.FC<TopicItemButtonProps> = ({ workspace, team, topic }) => {
  return (
    <Button
      className={cn(styles.button, "line-clamp")}
      variant={ButtonVariants.hoverFilled}
      heightVariant={ButtonHeight.short}
      href={`/${workspace.username}/${team.username}/tasks?topic=${topic.topicId}`}
    >
      {topic.tag}
    </Button>
  );
};

export default TopicItemButton;
