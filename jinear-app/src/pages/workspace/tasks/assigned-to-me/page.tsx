import AssignedToMeScreenHeader
    from "@/components/assignedToMeScreen/assignedToMeScreenHeader/AssignedToMeScreenHeader";
import PrefilteredPaginatedTaskList
    from "@/components/taskLists/prefilteredPaginatedTaskList/PrefilteredPaginatedTaskList";
import type {TeamDto} from "@/be/jinear-core";
import {selectCurrentAccountId, selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React, {useState} from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";

interface AssignedToMePageProps {
}

const AssignedToMePage: React.FC<AssignedToMePageProps> = ({}) => {
    const currentAccountId = useTypedSelector(selectCurrentAccountId);
    const params = useParams();
    const workspaceName: string = params?.workspaceName as string;
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const [filterBy, setFilterBy] = useState<TeamDto>();

    return (
        <div className={styles.container}>
            {workspace &&
                <AssignedToMeScreenHeader filterBy={filterBy} setFilterBy={setFilterBy} workspace={workspace}/>}
            {workspace && currentAccountId && (
                <PrefilteredPaginatedTaskList
                    id={"assigned-to-me-task-list"}
                    filter={{
                        workspaceId: workspace.workspaceId,
                        assigneeIds: [currentAccountId],
                        teamIdList: filterBy ? [filterBy.teamId] : undefined
                    }}
                />
            )}
        </div>
    );
};

export default AssignedToMePage;
