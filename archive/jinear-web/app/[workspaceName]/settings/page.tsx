"use client";
import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import WorkspaceInfoTab from "@/components/workspaceSettingsScreen/workspaceInfoTab/WorkspaceInfoTab";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface WorkspaceSettingsScreenProps {}

const WorkspaceSettingsScreen: React.FC<WorkspaceSettingsScreenProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
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
      </TabbedPanel>
    </div>
  );
};

export default WorkspaceSettingsScreen;
