import TeamFileList from "@/components/teamFilesScreen/TeamFileList";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";

interface FilesScreenProps {
}

const FilesScreen: React.FC<FilesScreenProps> = ({}) => {
    const params = useParams();
    const workspaceName: string = params?.workspaceName as string;
    const teamUsername: string = params?.teamUsername as string;

    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const {
        data: teamsResponse,
        isFetching: isTeamsFetching
    } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
        skip: workspace == null,
    });
    const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

    return <div className={styles.container}>{team && <TeamFileList teamId={team.teamId}/>}</div>;
};

export default FilesScreen;
