import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import TeamWorkflowSettings from "@/components/teamSettingsScreen/teamWorkflowSettings/TeamWorkflowSettings";
import WorkspaceInfoTab from "@/components/workspaceSettingsScreen/workspaceInfoTab/WorkspaceInfoTab";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface WorkspaceSettingsScreenProps {}

const WorkspaceSettingsScreen: React.FC<WorkspaceSettingsScreenProps> = ({}) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const currentTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <TabbedPanel initialTabName="workflow">
        <TabView name="workspace-info" label={t("workspaceSettingsPageWorkspaceInfoTab")}>
          <WorkspaceInfoTab />
        </TabView>
        {currentTeam && currentWorkspace?.isPersonal && (
          <TabView name="plan" label={t("teamSettingsScreenWorkflowSectionTitle")}>
            <TeamWorkflowSettings teamId={currentTeam.teamId} />
          </TabView>
        )}
      </TabbedPanel>
    </div>
  );
};

export default WorkspaceSettingsScreen;
