import {useRetrieveTaskBoardQuery} from "@/store/api/taskBoardRetrieveApi";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import styles from "./index.module.css";
import TaskBoard from "@/components/taskLists/taskBoardList/taskBoard/TaskBoard";
import {useParams} from "react-router-dom";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TaskBoardDetailScreenProps {
}

const TaskBoardDetailScreen: React.FC<TaskBoardDetailScreenProps> = ({}) => {
    const params = useParams();
    const taskBoardId: string = params?.taskBoardId as string;
    const workspaceName: string = params?.workspaceName as string;
    const teamUsername: string = params?.teamUsername as string;

    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const {
        data: teamsResponse,
        isFetching: isTeamsFetching
    } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
        skip: workspace == null
    });
    const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

    const {
        data: taskBoardResponse,
        isLoading,
        isFetching
    } = useRetrieveTaskBoardQuery({taskBoardId}, {skip: taskBoardId == null});

    return (
        <div className={styles.container}>
            {(isLoading || isFetching) && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={14}/>
                </div>
            )}
            {team && workspace && taskBoardResponse && (
                <TaskBoard
                    taskBoard={taskBoardResponse.data}
                    team={team}
                    workspace={workspace}
                />
            )}
        </div>
    );
};

export default TaskBoardDetailScreen;
