import React from "react";
import type {TaskDto} from "@/model/be/jinear-core";
import Button, {ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import {retrieveTaskStatusIcon} from "@/util/taskIconFactory";
import {getTextColor} from "@/util/colorHelper";
import SearchHighlight from "../searchHighlight/SearchHighlight";
import styles from "./SearchResultRow.module.css";

interface TaskSearchResultRowProps {
    task: TaskDto;
    query: string;
    workspaceUsername?: string;
    isMobile: boolean;
    onSelect: () => void;
}

const TaskSearchResultRow: React.FC<TaskSearchResultRowProps> = ({task, query, workspaceUsername, isMobile, onSelect}) => {
    const {t} = useTranslation();
    const statusIcon = retrieveTaskStatusIcon(task.workflowStatus?.workflowStateGroup);
    const taskTag = `${task.team?.tag}-${task.teamTagNo}`;
    const topicColor = task.topic?.color ? `#${task.topic.color}` : undefined;

    return (
        <Button
            className={styles.row}
            variant={ButtonVariants.hoverFilled2}
            href={`/${workspaceUsername}/task/${taskTag}`}
            onClick={onSelect}
        >
            {React.createElement(statusIcon, {className: styles.icon})}
            <div className={styles.textContainer}>
                <div className={styles.title}>
                    {!isMobile && <span className={styles.tag}>{taskTag}</span>}
                    <SearchHighlight text={task.title} query={query}/>
                </div>
                <div className={styles.subtitle}>
                    {isMobile && <span>{taskTag}</span>}
                    {task.team?.name && <span>{task.team.name}</span>}
                    {task.workflowStatus?.name && <span>{task.workflowStatus.name}</span>}
                    {task.topic && (
                        <span className={styles.topic}>
                            {t("globalSearchModalTopic")}
                            <span
                                className={styles.topicChip}
                                style={{backgroundColor: topicColor, color: topicColor ? getTextColor(topicColor) : undefined}}
                            >
                                <SearchHighlight text={task.topic.name} query={query}/>
                            </span>
                        </span>
                    )}
                </div>
            </div>
        </Button>
    );
};

export default TaskSearchResultRow;
