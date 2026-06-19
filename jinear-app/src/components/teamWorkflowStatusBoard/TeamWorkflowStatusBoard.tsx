import type {TaskDto} from "@/model/be/jinear-core";
import {useUpdateTaskWorkflowStatusMutation} from "@/store/api/taskWorkflowStatusApi";
import {useRetrieveAllFromTeamQuery} from "@/store/api/teamWorkflowStatusApi";
import Logger from "@/util/logger";
import cn from "classnames";
import React, {useEffect, useState} from "react";
import styles from "./TeamWorkflowStatusBoard.module.css";
import WorkflowStatusColumn from "./workflowStatusColumn/WorkflowStatusColumn";

export interface IWorkflowStatusUpdatePendingTask {
    taskId: string;
    newWorkflowStatusId: string;
}

interface TeamWorkflowStatusBoardProps {
    teamId: string;
    taskList: TaskDto[];
    isTaskListingLoading?: boolean;
    className?: string;
}

const logger = Logger("TeamWorkflowStatusBoard");

const TeamWorkflowStatusBoard: React.FC<TeamWorkflowStatusBoardProps> = ({
                                                                             teamId,
                                                                             taskList,
                                                                             isTaskListingLoading = false,
                                                                             className
                                                                         }) => {
    const {currentData: teamWorkflowListData, isFetching: isTeamWorkflowListFetching} = useRetrieveAllFromTeamQuery(
        {teamId},
        {skip: teamId == null}
    );

    const [updateTaskWorkflowStatus, {isLoading}] = useUpdateTaskWorkflowStatusMutation();
    const [workflowStatusUpdatePendingTask, setWorkflowStatusUpdatePendingTask] = useState<IWorkflowStatusUpdatePendingTask>();
    const [draggingTaskId, setDraggingTaskId] = useState<string | null>(null);

    useEffect(() => {
        if (!isTaskListingLoading) {
            setWorkflowStatusUpdatePendingTask(undefined);
        }
    }, [isTaskListingLoading]);

    const handleTaskDrop = (taskId: string, newWorkflowStatusId: string) => {
        const task = taskList.find(t => t.taskId === taskId);
        if (!task || task.workflowStatus.teamWorkflowStatusId === newWorkflowStatusId) {
            return;
        }

        logger.log({taskId, newWorkflowStatusId});
        setWorkflowStatusUpdatePendingTask({taskId, newWorkflowStatusId});
        updateTaskWorkflowStatus({taskId, workflowStatusId: newWorkflowStatusId});
    };

    const _isLoading = (isTeamWorkflowListFetching && teamWorkflowListData == null) || workflowStatusUpdatePendingTask;

    const columnProps = {
        tasks: taskList,
        workflowStatusUpdatePendingTask,
        onTaskDrop: handleTaskDrop,
        draggingTaskId,
        onDragStart: setDraggingTaskId,
        onDragEnd: () => setDraggingTaskId(null)
    };

    return (
        <div className={cn(styles.container, className, _isLoading ? styles["container-disabled"] : undefined)}>
            <div className={cn(styles.contentContainer)}>
                {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.BACKLOG?.map((workflowDto) => (
                    <WorkflowStatusColumn
                        key={workflowDto.teamWorkflowStatusId}
                        workflowStatusDto={workflowDto}
                        {...columnProps}
                    />
                ))}
                {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.NOT_STARTED?.map((workflowDto) => (
                    <WorkflowStatusColumn
                        key={workflowDto.teamWorkflowStatusId}
                        workflowStatusDto={workflowDto}
                        {...columnProps}
                    />
                ))}
                {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.STARTED?.map((workflowDto) => (
                    <WorkflowStatusColumn
                        key={workflowDto.teamWorkflowStatusId}
                        workflowStatusDto={workflowDto}
                        {...columnProps}
                    />
                ))}
                {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.COMPLETED?.map((workflowDto) => (
                    <WorkflowStatusColumn
                        key={workflowDto.teamWorkflowStatusId}
                        workflowStatusDto={workflowDto}
                        {...columnProps}
                    />
                ))}
                {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.CANCELLED?.map((workflowDto) => (
                    <WorkflowStatusColumn
                        key={workflowDto.teamWorkflowStatusId}
                        workflowStatusDto={workflowDto}
                        {...columnProps}
                    />
                ))}
            </div>
        </div>
    );
};

export default TeamWorkflowStatusBoard;
