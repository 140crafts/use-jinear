import React from "react";
import styles from "./MilestoneDeleteButton.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuTrash } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { closeDialogModal, popDialogModal } from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import { useDeleteMilestoneMutation } from "@/api/projectMilestoneApi";

interface MilestoneDeleteButtonProps {
  milestoneId: string,
  title: string
}

const MilestoneDeleteButton: React.FC<MilestoneDeleteButtonProps> = ({ milestoneId, title }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [deleteMilestone, { isLoading }] = useDeleteMilestoneMutation();

  const popAreYouSureModal = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteMilestoneAreYouSureTitle"),
        htmlContent: t("deleteMilestoneAreYouSureText").replace("${title}", title),
        confirmButtonLabel: t("taskDetailMediaDeleteMediaAreYouSureConfirmLabel"),
        onConfirm: onDeleteMilestone
      })
    );
  };

  const onDeleteMilestone = () => {
    deleteMilestone(milestoneId);
    dispatch(closeDialogModal());
  };

  return (
    <Button
      disabled={isLoading}
      loading={isLoading}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      className={styles.deleteButton}
      onClick={popAreYouSureModal}
    >
      <LuTrash className={"icon"} />
    </Button>
  );
};

export default MilestoneDeleteButton;