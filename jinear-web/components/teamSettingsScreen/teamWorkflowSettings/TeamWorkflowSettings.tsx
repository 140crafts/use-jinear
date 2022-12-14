import Transition from "@/components/transition/Transition";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import SectionTitle from "./sectionTitle/SectionTitle";
import styles from "./TeamWorkflowSettings.module.css";
import WorkflowGroup from "./workflowGroup/WorkflowGroup";

interface TeamWorkflowSettingsProps {
  teamId: string;
}

const TeamWorkflowSettings: React.FC<TeamWorkflowSettingsProps> = ({
  teamId,
}) => {
  const { t } = useTranslation();
  const { data: teamWorkflowListData, isLoading: isTeamWorkflowListLoading } =
    useRetrieveAllFromTeamQuery({ teamId }, { skip: teamId == null });
  return (
    <div className={styles.container}>
      <SectionTitle
        title={t("teamSettingsScreenWorkflowSectionTitle")}
        description={t("teamSettingsScreenWorkflowSectionDescription")}
      />
      {isTeamWorkflowListLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={21} />
        </div>
      )}

      {!isTeamWorkflowListLoading && teamWorkflowListData && (
        <Transition initial={true} className={styles.content}>
          <WorkflowGroup
            groupType={"BACKLOG"}
            statuses={
              teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["BACKLOG"]
            }
          />
          <WorkflowGroup
            groupType={"NOT_STARTED"}
            statuses={
              teamWorkflowListData.data.groupedTeamWorkflowStatuses?.[
                "NOT_STARTED"
              ]
            }
          />
          <WorkflowGroup
            groupType={"STARTED"}
            statuses={
              teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["STARTED"]
            }
          />
          <WorkflowGroup
            groupType={"COMPLETED"}
            statuses={
              teamWorkflowListData.data.groupedTeamWorkflowStatuses?.[
                "COMPLETED"
              ]
            }
          />
          <WorkflowGroup
            groupType={"CANCELLED"}
            statuses={
              teamWorkflowListData.data.groupedTeamWorkflowStatuses?.[
                "CANCELLED"
              ]
            }
          />
        </Transition>
      )}
    </div>
  );
};

export default TeamWorkflowSettings;
