import TeamSettingsScreenBreadcrumb from "@/components/teamSettingsScreen/teamSettingsScreenBreadcrumb/TeamSettingsScreenBreadcrumb";
import TeamWorkflowSettings from "@/components/teamSettingsScreen/teamWorkflowSettings/TeamWorkflowSettings";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface SettingsScreenProps {}

const SettingsScreen: React.FC<SettingsScreenProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const currentTeamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);

  return (
    <div className={styles.container}>
      <TeamSettingsScreenBreadcrumb workspaceName={workspaceName} teamUsername={teamUsername} />
      {currentTeamId && <TeamWorkflowSettings teamId={currentTeamId} />}
    </div>
  );
};

export default SettingsScreen;
