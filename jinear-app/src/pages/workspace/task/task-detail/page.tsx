import TaskDetail from "@/components/taskDetail/TaskDetail";
import TaskPageHeader from "@/components/taskDetail/taskPageHeader/TaskPageHeader";
import {useRetrieveWithWorkspaceNameAndTeamTagNoQuery} from "@/store/api/taskApi";
import Logger from "@/util/logger";
import React from "react";
import styles from "./index.module.scss";
import {useParams} from "react-router-dom";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TaskDetailPageProps {
}

const logger = Logger("TaskDetailPage");

const TaskDetailPage: React.FC<TaskDetailPageProps> = ({}) => {
    const {workspaceName, taskTag} = useParams();

    const {
        currentData: taskResponse,
        isFetching: isTaskRetrieveFetching,
    } = useRetrieveWithWorkspaceNameAndTeamTagNoQuery(
        {workspaceName: workspaceName ?? '', taskTag: taskTag ?? ''},
        {skip: workspaceName == null || taskTag == null}
    );

    return (
        <div className={styles.container}>
            {isTaskRetrieveFetching && taskResponse == null && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={21}/>
                </div>
            )}
            {taskResponse && taskTag && (
                <>
                    <TaskPageHeader taskTag={taskTag} title={taskResponse.data.title}/>
                    <div className={styles.contentContainer}>
                        {/* <TaskDetailHeader task={taskResponse.data} backButtonVisible={true} /> */}
                        <TaskDetail task={taskResponse.data}/>
                    </div>
                </>
            )}
        </div>
    );
};

export default TaskDetailPage;
