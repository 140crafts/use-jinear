import Pagination from "@/components/pagination/Pagination";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./TeamMemberList.module.scss";
import TeamMemberRow from "./teamMemberRow/TeamMemberRow";

interface TeamMemberListProps {
  teamId: string;
}

const TeamMemberList: React.FC<TeamMemberListProps> = ({ teamId }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const { data: teamMembersResponse, isSuccess, isLoading, isFetching, isError } = useRetrieveTeamMembersQuery({ teamId, page });

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        {teamMembersResponse && (
          <Pagination
            id={`active-invitation-list-paginator`}
            className={styles.pagination}
            pageNumber={teamMembersResponse.data.number}
            pageSize={teamMembersResponse.data.size}
            totalPages={teamMembersResponse.data.totalPages}
            totalElements={teamMembersResponse.data.totalElements}
            hasPrevious={teamMembersResponse.data.hasPrevious}
            hasNext={teamMembersResponse.data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={cn(styles.content, styles.gradientBg)}>
        {teamMembersResponse?.data.content.map((member) => (
          <TeamMemberRow key={`list-team-member-${member.teamMemberId}`} teamMember={member} />
        ))}

        {!teamMembersResponse?.data.hasContent && !isFetching && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{t("activeWorkspaceMemberListEmptyMessage")}</div>
          </div>
        )}
      </div>

      {isFetching && (
        <div className={styles.loading}>
          <CircularProgress />
        </div>
      )}
    </div>
  );
};

export default TeamMemberList;
