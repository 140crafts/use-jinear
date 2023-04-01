import TeamMemberList from "@/components/teamMembersScreen/teamMemberList/TeamMemberList";
import TeamMembersScreenHeader from "@/components/teamMembersScreen/teamMembersScreenHeader/TeamMembersScreenHeader";
import { selectCurrentAccountsPreferredTeam } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface TeamMembersScreenProps {}

const TeamMembersScreen: React.FC<TeamMembersScreenProps> = ({}) => {
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  return (
    <div className={styles.container}>
      <TeamMembersScreenHeader />
      {team && <TeamMemberList teamId={team.teamId} />}
    </div>
  );
};

export default TeamMembersScreen;
