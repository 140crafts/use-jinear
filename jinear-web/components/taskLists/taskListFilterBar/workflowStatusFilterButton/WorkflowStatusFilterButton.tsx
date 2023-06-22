import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { popTeamWorkflowStatusPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useSelectedWorkflowStatuses, useSetSelectedWorkflowStatuses, useTeam } from "../context/TaskListFilterBarContext";
import styles from "./WorkflowStatusFilterButton.module.css";

interface WorkflowStatusFilterButtonProps {}

const WorkflowStatusFilterButton: React.FC<WorkflowStatusFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const team = useTeam();
  const selectedWorkflowStatuses = useSelectedWorkflowStatuses();
  const setSelectedWorkflowStatuses = useSetSelectedWorkflowStatuses();
  const isEmpty = selectedWorkflowStatuses?.length == 0;

  const onPick = (pickedList: TeamWorkflowStatusDto[]) => {
    setSelectedWorkflowStatuses?.(pickedList);
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
