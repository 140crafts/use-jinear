import { useRetrieveWorkspaceMembersQuery } from "@/store/api/workspaceMemberApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import WorkspaceMemberList from "../menuMemberList/workspaceMemberList/WorkspaceMemberList";
import styles from "./WorkspaceMembers.module.css";

interface WorkspaceMembersProps {}

const WorkspaceMembers: React.FC<WorkspaceMembersProps> = ({}) => {
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const {
    data: workplaceMembersResponse,
    isSuccess,
    isLoading,
    isError,
  } = useRetrieveWorkspaceMembersQuery(
    { workspaceId: preferredWorkspace?.workspaceId || "" },
    {
      skip: !preferredWorkspace?.workspaceId,
    }
  );

  return (
    <div className={styles.container}>
      {isSuccess && preferredWorkspace && workplaceMembersResponse && (
        <WorkspaceMemberList page={workplaceMembersResponse.data} workspaceUsername={preferredWorkspace.username} />
      )}
    </div>
  );
};

export default WorkspaceMembers;
