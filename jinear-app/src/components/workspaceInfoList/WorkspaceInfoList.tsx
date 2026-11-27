import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store";
import React from "react";
import OrLine from "../or-line/or-line";
import styles from "./WorkspaceInfoList.module.css";
import NewWorkspaceButton from "./newWorkspaceButton/NewWorkspaceButton";
import WorkspaceInfoListItem from "./workspaceInfoListItem/WorkspaceInfoListItem";
import { useInstanceFlag } from "@/hooks/useInstanceFlag";
import { useParams } from "react-router-dom";

interface WorkspaceInfoListProps {
  onWorkspaceChangeComplete?: () => void;
}

const WorkspaceInfoList: React.FC<WorkspaceInfoListProps> = ({ onWorkspaceChangeComplete }) => {
  const workspaces = useTypedSelector(selectCurrentAccountsWorkspaces);
  const workspaceInitEnabled = useInstanceFlag("WORKSPACE_INIT");
  const { workspaceName } = useParams();

  return (
    <div className={styles.container}>
      {workspaces?.map((workspace) => (
        <WorkspaceInfoListItem
          key={`wpsace-info-list-item-${workspace.workspaceId}`}
          workspace={workspace}
          isCurrent={workspace.username == workspaceName}
          onWorkspaceChangeComplete={onWorkspaceChangeComplete}
        />
      ))}
      {workspaceInitEnabled && (
        <>
          <OrLine />
          <NewWorkspaceButton />
        </>
      )}
    </div>
  );
};

export default WorkspaceInfoList;
