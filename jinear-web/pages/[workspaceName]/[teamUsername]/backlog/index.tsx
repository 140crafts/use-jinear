import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import MultiViewTaskList from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface BacklogTaskListScreenProps {}

const BacklogTaskListScreen: React.FC<BacklogTaskListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const { data: teamWorkflowStatusListResponse, isFetching } = useRetrieveAllFromTeamQuery(
    { teamId: team?.teamId || "" },
    {
      skip: team == null,
    }
  );

  const backlogStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.BACKLOG || [];
  const backlogStatusIds = [...backlogStatuses].map((status) => status.teamWorkflowStatusId);

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
          title={t("backlogTasksPageTitle")}
          workspaceId={team.workspaceId}
          teamId={team.teamId}
          activeDisplayFormat="LIST"
          workflowStatusIdList={backlogStatusIds}
        />
      )}
    </div>
  );
};

export default BacklogTaskListScreen;
