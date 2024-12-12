import React, { useEffect, useState } from "react";
import styles from "./ProjectTitle.module.scss";
import { ProjectDto } from "@/be/jinear-core";
import { CircularProgress } from "@mui/material";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPencil } from "react-icons/lu";
import { useUpdateProjectTitleMutation } from "@/api/projectOperationApi";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/slice/modalSlice";

interface ProjectTitleProps {
  projectId: string;
  title?: string;
  isFetching?: boolean;
  editable?: boolean;
}

const ProjectTitle: React.FC<ProjectTitleProps> = ({ projectId, title, isFetching, editable = true }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [taskTitle, setTaskTitle] = useState(title);
  const [updateProjectTitle, { isLoading: isUpdateProjectTitleLoading }] = useUpdateProjectTitleMutation();

  useEffect(() => {
    setTaskTitle(title);
  }, [title]);

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    const req = {
      projectId,
      body: { title }
    };
    updateProjectTitle(req);
    setTaskTitle(title);
  };

  const popTitleChangeModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("projectTitleChangeModalTitle"),
        infoText: t("projectTitleChangeModalInfoText"),
        initialText: title,
        onSubmit: changeTitle
      })
    );
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>
        <b>{taskTitle}</b>
      </h1>
      {isUpdateProjectTitleLoading &&
        <span className={styles.savingLabel}>
          <CircularProgress size={12} className={styles.loading} />
          {t("taskDescriptionSaving")}
      </span>}
      {!isUpdateProjectTitleLoading && editable && (
        <Button
          disabled={isUpdateProjectTitleLoading || isFetching}
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

export default ProjectTitle;