import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { queryStateAnyToStringConverter, queryStateArrayParser, useQueryState, useSetQueryState } from "@/hooks/useQueryState";
import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { popTeamWorkflowStatusPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTeam } from "../context/TaskListFilterBarContext";
import styles from "./WorkflowStatusFilterButton.module.css";

interface WorkflowStatusFilterButtonProps {}

const WorkflowStatusFilterButton: React.FC<WorkflowStatusFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const team = useTeam();
  const setQueryState = useSetQueryState();
  const selectedWorkflowStatusIds = useQueryState<string[]>("workflowStatusIdList", queryStateArrayParser);
  const isEmpty = selectedWorkflowStatusIds == null || selectedWorkflowStatusIds.length == 0;

  const { data: teamWorkflowStatusListResponse } = useRetrieveAllFromTeamQuery(
    { teamId: team?.teamId || "" },
    { skip: team == null }
  );

  const notStartedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.NOT_STARTED || [];
  const startedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.STARTED || [];
  const completedStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.COMPLETED || [];
  const cancelledStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.CANCELLED || [];
  const backlogStatuses = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.BACKLOG || [];
  const activeStatuses = [...notStartedStatuses, ...startedStatuses];
  const undoneStatuses = [...backlogStatuses, ...notStartedStatuses, ...startedStatuses];
  const archivedStatuses = [...completedStatuses, ...cancelledStatuses];
  const allStatuses = [...notStartedStatuses, ...startedStatuses, ...completedStatuses, ...cancelledStatuses, ...backlogStatuses];

  const selectedWorkflowStatuses = allStatuses.filter(
    (tws) => selectedWorkflowStatusIds && selectedWorkflowStatusIds.indexOf(tws.teamWorkflowStatusId) != -1
  );

  const onPick = (pickedList: TeamWorkflowStatusDto[]) => {
    const pickedIds = pickedList.map((tws) => tws.teamWorkflowStatusId);
    setQueryState("workflowStatusIdList", queryStateAnyToStringConverter(pickedIds));
  };

  const popPicker = () => {
    dispatch(
      popTeamWorkflowStatusPickerModal({
        visible: true,
        teamId: team?.teamId,
        multiple: true,
        initialSelectionOnMultiple: selectedWorkflowStatuses,
        onPick,
      })
    );
  };
  const Icon = retrieveTaskStatusIcon(selectedWorkflowStatuses[0]?.workflowStateGroup);
  return (
    <Button
      heightVariant={ButtonHeight.short}
      variant={isEmpty ? ButtonVariants.filled : ButtonVariants.filled2}
      onClick={popPicker}
    >
      {isEmpty ? (
        t("taskFilterWorkflowStatusFilterButtonEmpty")
      ) : (
        <b>
          {selectedWorkflowStatuses?.length == 1 ? (
            <div className={styles.singleItem}>
              <Icon />
              {selectedWorkflowStatuses[0]?.name}
            </div>
          ) : (
            t("taskFilterWorkflowStatusFilterButtonSelected")?.replace("${count}", `${selectedWorkflowStatuses?.length}`)
          )}
        </b>
      )}
    </Button>
  );
};

export default WorkflowStatusFilterButton;
