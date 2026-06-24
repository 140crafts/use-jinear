import CircularLoading from "@/components/circularLoading/CircularLoading";
import MultiViewTaskList, {
    getTeamDefaultDisplayFormat
} from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";

interface TasksScreenProps {
}


const TasksScreen: React.FC<TasksScreenProps> = ({}) => {
    const {t} = useTranslation();
    const params = useParams();
    const workspaceName: string = params?.workspaceName as string;
    const teamUsername: string = params?.teamUsername as string;

    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const {
        currentData: teamsResponse,
        isFetching: isTeamsFetching
    } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
        skip: workspace == null
    });
    const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);
    const displayFormat = getTeamDefaultDisplayFormat(teamUsername);

    return (
        <div className={styles.container}>
            {isTeamsFetching && teamsResponse == null && <CircularLoading/>}
            {workspace && team && (
                <MultiViewTaskList
                    key={team.teamId}
                    workspace={workspace}
                    team={team}
                    activeDisplayFormat={displayFormat}
                    workflowStatusBoardClassName={styles.workflowStatusBoard}
                />
            )}
        </div>
    );
};

export default TasksScreen;
