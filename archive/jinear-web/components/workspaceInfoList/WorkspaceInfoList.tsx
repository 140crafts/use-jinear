import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import OrLine from "../orLine/OrLine";
import styles from "./WorkspaceInfoList.module.css";
import NewWorkspaceButton from "./newWorkspaceButton/NewWorkspaceButton";
import WorkspaceInfoListItem from "./workspaceInfoListItem/WorkspaceInfoListItem";

interface WorkspaceInfoListProps {
  onWorkspaceChangeComplete?: () => void;
}

const WorkspaceInfoList: React.FC<WorkspaceInfoListProps> = ({ onWorkspaceChangeComplete }) => {
  const workspaces = useTypedSelector(selectCurrentAccountsWorkspaces);

  return (
    <div className={styles.container}>
      {workspaces?.map((workspace, i) => (
        <WorkspaceInfoListItem
          key={`wpsace-info-list-item-${workspace.workspaceId}`}
          workspace={workspace}
          onWorkspaceChangeComplete={onWorkspaceChangeComplete}
        />
      ))}
      <OrLine />
      <NewWorkspaceButton />
    </div>
  );
};

export default WorkspaceInfoList;
