import type { TeamWorkflowStateGroup, TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useReorderTeamWorkflowStatusesMutation } from "@/store/api/teamWorkflowStatusApi";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, { useState } from "react";
import { toast } from "react-hot-toast";
import AddWorkflowStatus from "./addWorkflowStatus/AddWorkflowStatus";
import styles from "./WorkflowGroup.module.css";
import WorkflowStatus from "./workflowStatus/WorkflowStatus";

interface WorkflowGroupProps {
  teamId: string;
  groupType: TeamWorkflowStateGroup;
  statuses: TeamWorkflowStatusDto[] | undefined;
  editable: boolean;
}

const WorkflowGroup: React.FC<WorkflowGroupProps> = ({ teamId, groupType, statuses, editable }) => {
  const { t } = useTranslation();
  const [reorderTeamWorkflowStatuses] = useReorderTeamWorkflowStatusesMutation();

  const [draggedIndex, setDraggedIndex] = useState<number | null>(null);
  const [dragOverIndex, setDragOverIndex] = useState<number | null>(null);

  const statusCount = statuses?.length ?? 0;
  // Backend keeps at least one status per group, so a group's last status cannot be removed.
  const deletable = editable && statusCount > 1;
  const orderChangable = editable && statusCount > 1;

  const submitOrder = (ordered: TeamWorkflowStatusDto[]) => {
    reorderTeamWorkflowStatuses({
      teamId,
      teamWorkflowStatusReorderRequest: {
        workflowStateGroup: groupType,
        orderedTeamWorkflowStatusIds: ordered.map((status) => status.teamWorkflowStatusId),
      },
    })
      .unwrap()
      .catch(() => {
        toast(t("workflowStatusReorderFailed"));
      });
  };

  const moveStatus = (fromIndex: number, toIndex: number) => {
    if (!statuses || fromIndex == toIndex || toIndex < 0 || toIndex >= statuses.length) {
      return;
    }
    const ordered = [...statuses];
    const [moved] = ordered.splice(fromIndex, 1);
    ordered.splice(toIndex, 0, moved);
    submitOrder(ordered);
  };

  const handleDragStart = (e: React.DragEvent<HTMLDivElement>, index: number, teamWorkflowStatusId: string) => {
    setDraggedIndex(index);
    e.dataTransfer.effectAllowed = "move";
    e.dataTransfer.setData("text/plain", teamWorkflowStatusId);
  };

  const handleDragOver = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    // A drag started in another group leaves this group's draggedIndex null,
    // so cross group drops are never accepted.
    if (draggedIndex === null) {
      return;
    }
    e.preventDefault();
    e.dataTransfer.dropEffect = "move";
    setDragOverIndex(index);
  };

  const handleDragLeave = () => {
    setDragOverIndex(null);
  };

  const handleDragEnd = () => {
    setDraggedIndex(null);
    setDragOverIndex(null);
  };

  const handleDrop = (e: React.DragEvent<HTMLDivElement>, dropIndex: number) => {
    e.preventDefault();
    if (draggedIndex === null || draggedIndex === dropIndex) {
      handleDragEnd();
      return;
    }
    moveStatus(draggedIndex, dropIndex);
    handleDragEnd();
  };

  return (
    <div className={styles.container}>
      <div className={styles.groupTitle}>{t(`workflowGroupTitle_${groupType}`)}</div>
      {statuses?.map((workflowDto, index) => (
        <WorkflowStatus
          key={workflowDto.teamWorkflowStatusId}
          workflowDto={workflowDto}
          editable={editable}
          deletable={deletable}
          orderChangable={orderChangable}
          className={cn({
            [styles.dragging]: draggedIndex === index,
            [styles.dragOver]: dragOverIndex === index,
          })}
          dragHandlers={{
            onDragStart: (e) => handleDragStart(e, index, workflowDto.teamWorkflowStatusId),
            onDragOver: (e) => handleDragOver(e, index),
            onDragLeave: handleDragLeave,
            onDragEnd: handleDragEnd,
            onDrop: (e) => handleDrop(e, index),
          }}
          canMoveUp={index > 0}
          canMoveDown={index < statusCount - 1}
          onMoveUp={() => moveStatus(index, index - 1)}
          onMoveDown={() => moveStatus(index, index + 1)}
        />
      ))}
      {editable && <AddWorkflowStatus teamId={teamId} groupType={groupType} />}
    </div>
  );
};

export default WorkflowGroup;
