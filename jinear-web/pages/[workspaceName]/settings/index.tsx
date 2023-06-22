import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import TeamWorkflowSettings from "@/components/teamSettingsScreen/teamWorkflowSettings/TeamWorkflowSettings";
import WorkspaceInfoTab from "@/components/workspaceSettingsScreen/workspaceInfoTab/WorkspaceInfoTab";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface WorkspaceSettingsScreenProps {}

const WorkspaceSettingsScreen: React.FC<WorkspaceSettingsScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data?.find((team) => team);

  return (
    <div className={styles.container}>
      <TabbedPanel initialTabName="workflow">
        <TabView name="workspace-info" label={t("workspaceSettingsPageWorkspaceInfoTab")}>
          {workspace && <WorkspaceInfoTab workspace={workspace} />}
        </TabView>
        {workspace?.isPersonal && team && (
          <TabView name="plan" label={t("teamSettingsScreenWorkflowSectionTitle")}>
            <TeamWorkflowSettings teamId={team.teamId} />
          </TabView>
        )}
      </TabbedPanel>
    </div>
  );
};

export default WorkspaceSettingsScreen;
