import ActiveInvitationList from "@/components/workspaceMembersScreen/activeInvitationList/ActiveInvitationList";
import MemberList from "@/components/workspaceMembersScreen/memberList/MemberList";
import WorkspaceMembersScreenHeader from "@/components/workspaceMembersScreen/workspaceMembersScreenHeader/WorkspaceMembersScreenHeader";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface WorkspaceMembersScreenProps {}

const WorkspaceMembersScreen: React.FC<WorkspaceMembersScreenProps> = ({}) => {
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  return (
    <div className={styles.container}>
      {preferredWorkspace && <WorkspaceMembersScreenHeader workspaceUsername={preferredWorkspace.username} />}
      {preferredWorkspace && <MemberList workspaceId={preferredWorkspace.workspaceId} />}
      <div className="spacer-h-4" />
      {preferredWorkspace && <ActiveInvitationList workspaceId={preferredWorkspace.workspaceId} />}
    </div>
  );
};

export default WorkspaceMembersScreen;
