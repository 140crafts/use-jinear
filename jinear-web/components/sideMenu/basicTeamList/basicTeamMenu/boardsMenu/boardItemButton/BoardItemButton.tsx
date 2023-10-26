import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskBoardDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import { usePathname } from "next/navigation";
import React from "react";
import styles from "./BoardItemButton.module.css";

interface BoardItemButtonProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  taskBoardDto: TaskBoardDto;
}

const BoardItemButton: React.FC<BoardItemButtonProps> = ({ workspace, team, taskBoardDto }) => {
  const pathname = usePathname();
  const boardPath = `/${workspace.username}/tasks/${team.username}/task-boards/${taskBoardDto.taskBoardId}`;

  return (
    <Button
      className={cn(styles.button, "line-clamp")}
      variant={pathname == boardPath ? ButtonVariants.filled : ButtonVariants.hoverFilled}
      heightVariant={ButtonHeight.short}
      href={`/${workspace.username}/tasks/${team.username}/task-boards/${taskBoardDto.taskBoardId}`}
    >
      {shortenStringIfMoreThanMaxLength({ text: taskBoardDto.title, maxLength: 34 })}
    </Button>
  );
};

export default BoardItemButton;
