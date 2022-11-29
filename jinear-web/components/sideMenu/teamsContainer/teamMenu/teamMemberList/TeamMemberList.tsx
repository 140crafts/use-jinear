import MenuMemberList from "@/components/sideMenu/menuMemberList/MenuMemberList";
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
  } = useRetrieveTeamMembersQuery(teamId, { skip: teamId == null });

  return (
    <div className={styles.container}>
      {isSuccess && teamMembersResponse && (
        <MenuMemberList page={teamMembersResponse.data} type="team" />
      )}
    </div>
  );
};

export default TeamMemberList;
