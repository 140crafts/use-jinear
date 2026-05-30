import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {TaskBoardDto, TeamDto, WorkspaceDto} from "@/model/be/jinear-core";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil";
import cn from "classnames";
import {useLocation} from "react-router-dom";
import React from "react";
import styles from "./BoardItemButton.module.css";

interface BoardItemButtonProps {
    workspace: WorkspaceDto;
    team: TeamDto;
    taskBoardDto: TaskBoardDto;
}

const BoardItemButton: React.FC<BoardItemButtonProps> = ({workspace, team, taskBoardDto}) => {
    const {pathname} = useLocation();
    const boardPath = `/${workspace.username}/tasks/${team.username}/task-boards/${taskBoardDto.taskBoardId}`;

    return (
        <Button
            className={cn(styles.button, "line-clamp")}
            variant={pathname == boardPath ? ButtonVariants.filled : ButtonVariants.hoverFilled}
            heightVariant={ButtonHeight.short}
            href={`/${workspace.username}/tasks/${team.username}/task-boards/${taskBoardDto.taskBoardId}`}
        >
            {shortenStringIfMoreThanMaxLength({text: taskBoardDto.title, maxLength: 34})}
        </Button>
    );
};

export default BoardItemButton;
