import Pagination from "@/components/pagination/Pagination";
import { AccountsWorkspacePerspectiveDto } from "@/model/be/jinear-core";
import { useListInvitationsQuery } from "@/store/api/workspaceMemberInvitationApi";
import { selectCurrentAccountsWorkspaceRoleIsAdminOrOwner } from "@/store/slice/accountSlice";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./ActiveInvitationList.module.scss";
import InvitationRow from "./invitationRow/InvitationRow";

interface ActiveInvitationListProps {
  workspace: AccountsWorkspacePerspectiveDto;
}

const ActiveInvitationList: React.FC<ActiveInvitationListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);
  const workspaceRoleIsAdminOrOwner = selectCurrentAccountsWorkspaceRoleIsAdminOrOwner(workspace);
  const {
    data: invitationListResponse,
    isLoading,
    isFetching,
  } = useListInvitationsQuery({ workspaceId: workspace.workspaceId, page });

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>{t("activeInvitationListTitle")}</h2>
        {invitationListResponse && (
          <Pagination
            id={`active-invitation-list-paginator`}
            className={styles.pagination}
            pageNumber={invitationListResponse.data.number}
            pageSize={invitationListResponse.data.size}
            totalPages={invitationListResponse.data.totalPages}
            totalElements={invitationListResponse.data.totalElements}
            hasPrevious={invitationListResponse.data.hasPrevious}
            hasNext={invitationListResponse.data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={cn(styles.content, styles.gradientBg)}>
        {invitationListResponse?.data.content.map((invitation) => (
          <InvitationRow
            key={`list-workspace-invitation-${invitation.workspaceInvitationId}`}
            invitation={invitation}
            workspaceRoleIsAdminOrOwner={workspaceRoleIsAdminOrOwner}
          />
        ))}

        {!invitationListResponse?.data.hasContent && !isFetching && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{t("activeInvitationListEmptyMessage")}</div>
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

export default ActiveInvitationList;
