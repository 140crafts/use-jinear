import Pagination from "@/components/pagination/Pagination";
import { AccountsWorkspacePerspectiveDto } from "@/model/be/jinear-core";
import { useRetrieveWorkspaceMembersQuery } from "@/store/api/workspaceMemberApi";
import { selectCurrentAccountsWorkspaceRoleIsAdminOrOwner } from "@/store/slice/accountSlice";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./MemberList.module.scss";
import MemberRow from "./memberRow/MemberRow";

interface MemberListProps {
  workspace: AccountsWorkspacePerspectiveDto;
}

const MemberList: React.FC<MemberListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);
  const workspaceRoleIsAdminOrOwner = selectCurrentAccountsWorkspaceRoleIsAdminOrOwner(workspace);
  const {
    data: workplaceMembersResponse,
    isLoading,
    isFetching,
  } = useRetrieveWorkspaceMembersQuery({ workspaceId: workspace.workspaceId, page });

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>{t("activeWorkspaceMemberListTitle")}</h2>
        {workplaceMembersResponse && (
          <Pagination
            id={`active-invitation-list-paginator`}
            className={styles.pagination}
            pageNumber={workplaceMembersResponse.data.number}
            pageSize={workplaceMembersResponse.data.size}
            totalPages={workplaceMembersResponse.data.totalPages}
            totalElements={workplaceMembersResponse.data.totalElements}
            hasPrevious={workplaceMembersResponse.data.hasPrevious}
            hasNext={workplaceMembersResponse.data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={cn(styles.content, styles.gradientBg)}>
        {workplaceMembersResponse?.data.content.map((member) => (
          <MemberRow
            key={`list-workspace-member-${member.workspaceMemberId}`}
            member={member}
            workspaceRoleIsAdminOrOwner={workspaceRoleIsAdminOrOwner}
          />
        ))}

        {!workplaceMembersResponse?.data.hasContent && !isFetching && (
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

export default MemberList;
