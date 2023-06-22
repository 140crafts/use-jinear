import ActiveInvitationList from "@/components/workspaceMembersScreen/activeInvitationList/ActiveInvitationList";
import MemberList from "@/components/workspaceMembersScreen/memberList/MemberList";
import WorkspaceMembersScreenHeader from "@/components/workspaceMembersScreen/workspaceMembersScreenHeader/WorkspaceMembersScreenHeader";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface WorkspaceMembersScreenProps {}

const WorkspaceMembersScreen: React.FC<WorkspaceMembersScreenProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return (
    <div className={styles.container}>
      {workspace && <WorkspaceMembersScreenHeader workspace={workspace} />}
      {workspace && <MemberList workspace={workspace} />}
      <div className="spacer-h-4" />
      {workspace && <ActiveInvitationList workspace={workspace} />}
    </div>
  );
};

export default WorkspaceMembersScreen;
