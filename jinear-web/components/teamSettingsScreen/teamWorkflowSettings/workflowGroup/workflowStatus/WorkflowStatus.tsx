import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useChangeTeamWorkflowStatusNameMutation } from "@/store/api/teamWorkflowStatusApi";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import {
  IoCaretDown,
  IoCaretUp,
  IoCheckmarkCircle,
  IoClose,
  IoCloseCircle,
  IoContrast,
  IoEllipseOutline,
  IoPauseCircleOutline,
  IoPencil,
} from "react-icons/io5";
import styles from "./WorkflowStatus.module.scss";

interface WorkflowStatusProps {
  deletable: boolean;
  orderChangable: boolean;
  workflowDto: TeamWorkflowStatusDto;
}

const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={20} />,
  NOT_STARTED: <IoEllipseOutline size={20} />,
  STARTED: <IoContrast size={20} />,
  COMPLETED: <IoCheckmarkCircle size={20} />,
  CANCELLED: <IoCloseCircle size={20} />,
};

const WorkflowStatus: React.FC<WorkflowStatusProps> = ({
  workflowDto,
  deletable,
  orderChangable,
}) => {
  const { t } = useTranslation();
  const [isEditing, setIsEditing] = useState<boolean>(false);

  const [name, setName] = useState<string>(workflowDto.name);
  const nameInputRef = useRef<HTMLInputElement>(null);

  const [
    changeTeamWorkflowStatusName,
    { isLoading: isNameChangeLoading, isSuccess: isNameChangeSuccess },
  ] = useChangeTeamWorkflowStatusNameMutation();

  useEffect(() => {
    if (isEditing) {
      nameInputRef.current?.focus();
    }
  }, [isEditing]);

  useEffect(() => {
    if (!isNameChangeLoading && isNameChangeSuccess) {
      setIsEditing(false);
    }
  }, [isNameChangeLoading, isNameChangeSuccess]);

  const onNameChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setName?.(value);
  };

  const submitNewName = () => {
    changeTeamWorkflowStatusName({
      name,
      teamId: workflowDto.teamId,
      teamWorkflowStatusId: workflowDto.teamWorkflowStatusId,
    });
  };

  return (
    <div
      className={styles.container}
      data-tooltip={
        workflowDto.name?.length > 36 ? workflowDto.name : undefined
      }
    >
      <div className={styles.icon}>
        {groupIconMap[workflowDto.workflowStateGroup]}
      </div>

      {!isEditing && (
        <div className={cn(styles.name, "single-line")}>{workflowDto.name}</div>
      )}
      {isEditing && (
        <input
          ref={nameInputRef}
          type="text"
          value={name}
          onChange={onNameChange}
          className={styles.nameInput}
        />
      )}

      {!isEditing && <div className="flex-1" />}
      {!isEditing && (
        <div className={styles.actionContainer}>
          {orderChangable && (
            <Button
              heightVariant={ButtonHeight.short}
              variant={ButtonVariants.hoverFilled2}
              data-tooltip-right={t("workflowStatusOrderDownTooltip")}
            >
              <IoCaretDown />
            </Button>
          )}
          {orderChangable && workflowDto.order != 0 && (
            <Button
              heightVariant={ButtonHeight.short}
              variant={ButtonVariants.hoverFilled2}
              data-tooltip-right={t("workflowStatusOrderUpTooltip")}
            >
              <IoCaretUp />
            </Button>
          )}

          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.hoverFilled2}
            data-tooltip-right={t("workflowStatusEditTooltip")}
            onClick={() => {
              setIsEditing(true);
            }}
          >
            <IoPencil />
          </Button>
          {deletable && (
            <Button
              heightVariant={ButtonHeight.short}
              variant={ButtonVariants.hoverFilled2}
              data-tooltip-right={t("workflowStatusDeleteTooltip")}
            >
              <IoClose />
            </Button>
          )}
        </div>
      )}
      {isEditing && (
        <div className={styles.editingActionContainer}>
          <Button
            disabled={isNameChangeLoading}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.hoverFilled2}
            onClick={() => {
              setIsEditing(false);
            }}
          >
            {t("workflowStatusNameEditCancel")}
          </Button>
          <Button
            disabled={isNameChangeLoading}
            loading={isNameChangeLoading}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            onClick={submitNewName}
          >
            {t("workflowStatusNameEditSave")}
          </Button>
        </div>
      )}
    </div>
  );
};

export default WorkflowStatus;
