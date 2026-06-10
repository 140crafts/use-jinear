import React from "react";
import styles from "./MilestoneStateToggleButton.module.css";
import { MilestoneStateType } from "@/be/jinear-core";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useUpdateMilestoneStateMutation } from "@/api/projectMilestoneApi";
import { IoCheckmarkCircle, IoEllipseOutline } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";

interface MilestoneStateToggleButtonProps {
  milestoneId: string;
  milestoneState: MilestoneStateType;
}

const MilestoneStateToggleButton: React.FC<MilestoneStateToggleButtonProps> = ({ milestoneId, milestoneState }) => {
  const { t } = useTranslation();
  const [updateMilestoneState, { isLoading }] = useUpdateMilestoneStateMutation();

  const toggleMilestoneState = () => {
    const nextState = milestoneState == "COMPLETED" ? "IN_PROGRESS" : "COMPLETED";
    updateMilestoneState({
      milestoneId,
      milestoneState: nextState
    });
  };

  return (
    <Button
      disabled={isLoading}
      loading={isLoading}
      className={styles.button}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      onClick={toggleMilestoneState}
      data-tooltip-multiline={t(`milestoneStateToggleButtonTooltip_${milestoneState}`)}
    >
      {milestoneState == "IN_PROGRESS" ?
        <IoEllipseOutline size={16} className={styles.icon} /> :
        <IoCheckmarkCircle size={16} className={styles.icon} />
      }
    </Button>
  );
};

export default MilestoneStateToggleButton;