import { useRetrieveWorkspaceMembersQuery } from "@/store/api/workspaceMemberApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import MenuMemberList from "../menuMemberList/MenuMemberList";
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
  } = useRetrieveWorkspaceMembersQuery(preferredWorkspace?.workspaceId || "", {
    skip: !preferredWorkspace?.workspaceId && !preferredWorkspace?.isPersonal,
  });

  return (
    <div className={styles.container}>
      {/* <MenuGroupTitle label={t("sideMenuWorkspaceMembers")} /> */}
      {/* <div className="spacer-h-1" /> */}
      {isSuccess && !preferredWorkspace?.isPersonal && workplaceMembersResponse && (
        <MenuMemberList page={workplaceMembersResponse.data} type="workspace" />
      )}
    </div>
  );
};

export default WorkspaceMembers;
