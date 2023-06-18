import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import MultiViewTaskList from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface ActiveTaskListScreenProps {}

const ActiveTaskListScreen: React.FC<ActiveTaskListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const { data: teamWorkflowStatusListResponse, isFetching } = useRetrieveAllFromTeamQuery(
    { teamId: team?.teamId || "" },
    {
      skip: team == null,
    }
  );

  const notStartedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.NOT_STARTED || [];
  const startedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.STARTED || [];
  const activeStatusIds = [...notStartedStatuses, ...startedStatuses].map((status) => status.teamWorkflowStatusId);

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
          title={t("activeTasksPageTitle")}
          workspaceId={team.workspaceId}
          teamId={team.teamId}
          activeDisplayFormat="LIST"
          workflowStatusIdList={activeStatusIds}
        />
      )}
    </div>
  );
};

export default ActiveTaskListScreen;
