import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import BasicFeedList from "../sideMenu/basicFeedList/BasicFeedList";
import BasicTeamList from "../sideMenu/basicTeamList/BasicTeamList";
import styles from "./TasksSectionSideMenu.module.css";
import {useFeatureFlag} from "@/hooks/useFeatureFlag";
import WorkspaceActionButtons from "@/components/sideMenu/workspaceActionButtons/WorkspaceActionButtons";
import {useParams} from "react-router-dom";

interface TasksSectionSideMenuProps {
}

const TasksSectionSideMenu: React.FC<TasksSectionSideMenuProps> = ({}) => {
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const feedsEnabled = useFeatureFlag("FEEDS");

    return (
        <div className={styles.container}>
            {workspace && (
                <>
                    {workspace && <WorkspaceActionButtons workspace={workspace}/>}
                    {feedsEnabled && workspace && <BasicFeedList workspace={workspace}/>}
                    {workspace && <BasicTeamList workspace={workspace}/>}
                </>
            )}
        </div>
    );
};

export default TasksSectionSideMenu;
