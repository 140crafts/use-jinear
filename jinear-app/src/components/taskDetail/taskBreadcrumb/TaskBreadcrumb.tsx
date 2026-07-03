import React from 'react';
import styles from './TaskBreadcrumb.module.css';
import {useTask} from "@/components/taskDetail/context/TaskDetailContext.ts";
import TaskTagNoButton from "@/components/taskDetail/taskActionBar/taskTagNoButton/TaskTagNoButton.tsx";
import {LuChevronRight} from "react-icons/lu";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";

interface TaskBreadcrumbProps {
    withGoToTaskButton?: boolean,
    onGoToTaskButtonClick?: () => void
}

const TaskBreadcrumb: React.FC<TaskBreadcrumbProps> = ({withGoToTaskButton, onGoToTaskButtonClick}) => {
    const task = useTask();
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <Button
                variant={ButtonVariants.link}
                href={`/${task?.workspace?.username}/tasks/${task?.team?.username}`}
                className={styles.workspaceButton}
                heightVariant={ButtonHeight.short}
            >
                {task?.team?.name}
            </Button>
            <LuChevronRight className={'icon'}/>
            <TaskTagNoButton className={styles.tagButton}/>

            <div className={'flex-1'}/>

            {withGoToTaskButton &&
                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.outline}
                    onClick={onGoToTaskButtonClick}
                    href={`/${task?.workspace?.username}/task/${task?.team?.tag}-${task?.teamTagNo}`}
                >
                    {/*<IoResize/>*/}
                    <b>{t("taskOverviewModalGoToTask")}</b>
                </Button>
            }
        </div>
    );
}

export default TaskBreadcrumb;