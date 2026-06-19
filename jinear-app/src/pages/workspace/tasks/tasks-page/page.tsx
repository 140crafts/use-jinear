import CircularLoading from "@/components/circularLoading/CircularLoading";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React, {useEffect} from "react";
import styles from "./page.module.css";
import {useNavigate, useParams} from "react-router-dom";

interface WorkspaceTasksPageProps {
}

const WorkspaceTasksPage: React.FC<WorkspaceTasksPageProps> = ({}) => {
    const navigate = useNavigate();
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    const {data: teamsResponse} = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
        skip: workspace == null,
    });
    const team = teamsResponse?.data?.find((team) => team);

    useEffect(() => {
        if (team && workspace) {
            navigate(`/${workspace.username}/tasks/${team.username}`, {replace: true});
        }
    }, [team, workspace]);

    return (
        <div className={styles.container}>
            <CircularLoading/>
        </div>
    );
};

export default WorkspaceTasksPage;
