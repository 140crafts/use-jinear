import WorkspaceTeamMemberList from "@/components/sideMenu/menuMemberList/workspaceTeamMemberList/WorkspaceTeamMemberList";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import React from "react";
import styles from "./TeamMemberList.module.css";

interface TeamMemberListProps {
  teamId: string;
}

const TeamMemberList: React.FC<TeamMemberListProps> = ({ teamId }) => {
  const {
    data: teamMembersResponse,
    isSuccess,
    isLoading,
    isError,
  } = useRetrieveTeamMembersQuery({ teamId }, { skip: teamId == null });

  return (
    <div className={styles.container}>
      {isSuccess && teamMembersResponse && <WorkspaceTeamMemberList page={teamMembersResponse.data} />}
    </div>
  );
};

export default TeamMemberList;
