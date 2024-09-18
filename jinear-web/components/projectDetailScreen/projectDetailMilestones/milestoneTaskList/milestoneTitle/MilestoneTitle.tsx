import React, { useEffect, useState } from "react";
import styles from "./MilestoneTitle.module.scss";
import { CircularProgress } from "@mui/material";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuDiamond, LuPencil } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/slice/modalSlice";
import { useUpdateMilestoneTitleMutation } from "@/api/projectMilestoneApi";

interface MilestoneTitleProps {
  milestoneId: string;
  title?: string;
  isFetching?: boolean;
}

const MilestoneTitle: React.FC<MilestoneTitleProps> = ({ milestoneId, title, isFetching }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [milestoneTitle, setMilestoneTitle] = useState(title);
  const [updateMilestoneTitle, { isLoading: isUpdateMilestoneTitleLoading }] = useUpdateMilestoneTitleMutation();

  useEffect(() => {
    setMilestoneTitle(title);
  }, [title]);

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    const req = {
      milestoneId,
      title
    };
    updateMilestoneTitle(req);
    setMilestoneTitle(title);
  };

  const popTitleChangeModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("milestoneTitleChangeModalTitle"),
        infoText: t("milestoneTitleChangeModalInfoText"),
        initialText: title,
        onSubmit: changeTitle
      })
    );
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>
        <b className={"line-clamp"}>{milestoneTitle}</b>
      </h2>
      {isUpdateMilestoneTitleLoading &&
        <span className={styles.savingLabel}>
          <CircularProgress size={12} className={styles.loading} />
          {t("milestoneDescriptionSaving")}
      </span>}
      {!isUpdateMilestoneTitleLoading && (
        <Button
          disabled={isUpdateMilestoneTitleLoading || isFetching}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.contrast}
          className={styles.editTitleButton}
          onClick={popTitleChangeModal}
        >
          <LuPencil className={"icon"} />
        </Button>
      )}
    </div>
  );
};

export default MilestoneTitle;