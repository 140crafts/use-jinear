import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import WorkspaceInfoTab from "@/components/workspaceSettingsScreen/workspaceInfoTab/WorkspaceInfoTab";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";

interface WorkspaceSettingsScreenProps {
}

const WorkspaceSettingsScreen: React.FC<WorkspaceSettingsScreenProps> = ({}) => {
    const {t} = useTranslation();
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    const {data: teamsResponse} = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
        skip: workspace == null,
    });
    const team = teamsResponse?.data?.find((team) => team);

    return (
        <div className={styles.container}>
            <TabbedPanel initialTabName="workflow">
                <TabView name="workspace-info" label={t("workspaceSettingsPageWorkspaceInfoTab")}>
                    {workspace && <WorkspaceInfoTab workspace={workspace}/>}
                </TabView>
            </TabbedPanel>
        </div>
    );
};

export default WorkspaceSettingsScreen;
