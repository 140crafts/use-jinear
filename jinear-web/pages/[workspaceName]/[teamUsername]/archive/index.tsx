import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import MultiViewTaskList from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface TeamArchiveScreenProps {}

const TeamArchiveScreen: React.FC<TeamArchiveScreenProps> = ({}) => {
  const { t } = useTranslation();
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const { data: teamWorkflowStatusListResponse, isFetching } = useRetrieveAllFromTeamQuery(
    { teamId: team?.teamId || "" },
    {
      skip: team == null,
    }
  );

  const completedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.COMPLETED || [];
  const cancelledStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.CANCELLED || [];
  const archivedStatusIds = [...completedStatuses, ...cancelledStatuses].map((status) => status.teamWorkflowStatusId);

  return (
    <div className={styles.container}>
      {!workspace?.isPersonal && <TaskListScreenBreadcrumb type="active" />}
      {isFetching && (
        <div className="loadingContainer">
          <CircularProgress size={14} />
        </div>
      )}
      {team && !isFetching && (
        <MultiViewTaskList
          title={t("archivedTasksPageTitle")}
          workspaceId={team.workspaceId}
          teamId={team.teamId}
          activeDisplayFormat="LIST"
          workflowStatusIdList={archivedStatusIds}
        />
      )}
    </div>
  );
};

export default TeamArchiveScreen;
