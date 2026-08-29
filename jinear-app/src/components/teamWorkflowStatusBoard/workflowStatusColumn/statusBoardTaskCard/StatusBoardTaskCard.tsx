import AssigneeCell from "@/components/assigneeCell/AssigneeCell";
import Button from "@/components/button";
import TopicInfo from "@/components/taskRow/topicInfo/TopicInfo";
import TeamTagCell from "@/components/teamTagCell/TeamTagCell";
import useWindowSize from "@/hooks/useWindowSize";
import type {TaskDto} from "@/model/be/jinear-core";
import {popChangeTaskDateModal, popTaskOverviewModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import cn from "classnames";
import React from "react";
import {IoTime} from "react-icons/io5";
import styles from "./StatusBoardTaskCard.module.css";
import {Link} from "react-router-dom";

interface StatusBoardTaskCardProps {
    task: TaskDto;
    index?: number;
    onDragStart?: (e: React.DragEvent<HTMLAnchorElement>, taskId: string) => void;
    onDragEnd?: () => void;
    isDragging?: boolean;
}

const StatusBoardTaskCard: React.FC<StatusBoardTaskCardProps> = ({
                                                                     task,
                                                                     index = 0,
                                                                     onDragStart,
                                                                     onDragEnd,
                                                                     isDragging = false
                                                                 }) => {
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();

    const popChangeDatesModal = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event?.preventDefault?.();
        dispatch(popChangeTaskDateModal({visible: true, task}));
    };

    const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
        if (!isMobile) {
            event?.preventDefault();
            openTaskOverviewModal();
        }
    };

    const openTaskOverviewModal = () => {
        const workspaceName = task?.workspace?.username;
        const taskTag = `${task?.team?.tag}-${task?.teamTagNo}`;
        dispatch(popTaskOverviewModal({taskTag, workspaceName, task: task ?? undefined, visible: true}));
    };

    const handleDragStart = (e: React.DragEvent<HTMLAnchorElement>) => {
        e.dataTransfer.effectAllowed = "move";
        e.dataTransfer.setData("text/plain", task.taskId);
        onDragStart?.(e, task.taskId);
    };

    return (
        <Link
            to={`/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`}
            className={cn(styles.container, {[styles.dragging]: isDragging})}
            onClick={onLinkClick}
            draggable
            onDragStart={handleDragStart}
            onDragEnd={onDragEnd}
            onDragLeave={onDragEnd}
        >
            <div className={cn(styles.title)}>{task.title}</div>
            <div className={styles.infoContainer}>
                {task.topic && <TopicInfo topic={task.topic}/>}
                <Button className={styles.taskIconButton} onClick={popChangeDatesModal}>
                    <IoTime size={12}/>
                </Button>
                <AssigneeCell
                    task={task}
                    tooltipPosition={task.workflowStatus.workflowStateGroup == "BACKLOG" ? "left" : "right"}
                    className={styles.taskIconButton}
                />
                <TeamTagCell task={task} className={styles.taskTagCell}/>
            </div>
        </Link>
    );
};

export default StatusBoardTaskCard;
