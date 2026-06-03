import React, {useMemo} from "react";
import styles from "./page.module.css";
import WorkspaceFilesPage, {type IFilesPageFilter} from "@/components/workspaceFilesPage/WorkspaceFilesPage";
import {useTypedSelector} from "@/store";
import {selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice";
import type {MaterialSearchContentFilterType, MaterialSearchSortType, WorkspaceDto} from "@/be/jinear-core";
import {useQueryState} from "@/hooks/useQueryState";
import TeamFileList from "@/components/teamFilesScreen/TeamFileList";
import {useParams} from "react-router-dom";

interface FilesPageProps {

}

const FilesPage: React.FC<FilesPageProps> = ({}) => {
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName)) as WorkspaceDto;

    const parentMaterialId = useQueryState<string>("parentMaterialId");
    const materialSearchSortType = useQueryState<MaterialSearchSortType | null>("materialSearchSortType");
    const materialSearchContentFilterType = useQueryState<MaterialSearchContentFilterType | null>("materialSearchContentFilterType");
    const taskFilesTeamId = useQueryState<string>("taskFilesTeamId");

    const filter: IFilesPageFilter = useMemo(() => ({
        parentMaterialId,
        materialSearchSortType,
        materialSearchContentFilterType
    }), [parentMaterialId, materialSearchSortType, materialSearchContentFilterType]);

    return (
        <div className={styles.container}>
            {workspace && (
                taskFilesTeamId ?
                    <TeamFileList teamId={taskFilesTeamId}/> :
                    <WorkspaceFilesPage workspace={workspace} filter={filter}/>
            )}
        </div>
    );
};

export default FilesPage;