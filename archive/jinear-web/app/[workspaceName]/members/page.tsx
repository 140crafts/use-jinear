"use client";
import ActiveInvitationList from "@/components/workspaceMembersScreen/activeInvitationList/ActiveInvitationList";
import MemberList from "@/components/workspaceMembersScreen/memberList/MemberList";
import WorkspaceMembersScreenHeader from "@/components/workspaceMembersScreen/workspaceMembersScreenHeader/WorkspaceMembersScreenHeader";
import {
  selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithWorkspaceUsername,
  selectWorkspaceFromWorkspaceUsername,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface WorkspaceMembersScreenProps {}

const WorkspaceMembersScreen: React.FC<WorkspaceMembersScreenProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const isWorkspaceAdminOrOwner = useTypedSelector(
    selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithWorkspaceUsername(workspaceName)
  );

  return (
    <div className={styles.container}>
      {workspace && <WorkspaceMembersScreenHeader workspace={workspace} isWorkspaceAdminOrOwner={isWorkspaceAdminOrOwner} />}
      {workspace && <MemberList workspace={workspace} />}
      <div className="spacer-h-4" />
      {workspace && isWorkspaceAdminOrOwner && <ActiveInvitationList workspace={workspace} />}
    </div>
  );
};

export default WorkspaceMembersScreen;
