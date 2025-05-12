import { useRetrieveWorkspaceMembersQuery } from "@/store/api/workspaceMemberApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import WorkspaceMemberList from "../menuMemberList/workspaceMemberList/WorkspaceMemberList";
import styles from "./WorkspaceMembers.module.css";

interface WorkspaceMembersProps {
  workspaceName: string;
}

const WorkspaceMembers: React.FC<WorkspaceMembersProps> = ({ workspaceName }) => {
  const { t } = useTranslation();
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: workplaceMembersResponse, isSuccess } = useRetrieveWorkspaceMembersQuery(
    { workspaceId: workspace?.workspaceId || "" },
    {
      skip: !workspace?.workspaceId,
    }
  );

  return (
    <div className={styles.container}>
      {isSuccess && workspace && workplaceMembersResponse && <WorkspaceMemberList page={workplaceMembersResponse.data} />}
    </div>
  );
};

export default WorkspaceMembers;
