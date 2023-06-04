import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TaskRow from "@/components/taskLists/taskRow/TaskRow";
import { TaskBoardEntryDto } from "@/model/be/jinear-core";
import { useDeleteTaskBoardEntryMutation } from "@/store/api/taskBoardEntryApi";
import { changeLoadingModalVisibility, closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { IoRemoveCircleOutline } from "react-icons/io5";
import styles from "./TaskBoardElement.module.scss";

interface TaskBoardElementProps {
  item: TaskBoardEntryDto;
}

const TaskBoardElement: React.FC<TaskBoardElementProps> = ({ item }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [deleteTaskBoardEntry, { isLoading: isDeleteEntryLoading }] = useDeleteTaskBoardEntryMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isDeleteEntryLoading }));
  }, [isDeleteEntryLoading]);

  const removeElementFromBoard = () => {
    dispatch(closeDialogModal());
    deleteTaskBoardEntry({ taskBoardEntryId: item.taskBoardEntryId, taskBoardId: item.taskBoardId });
  };

  const popAreYouSureModalForLogout = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("taskBoardEntryDeleteAreYouSureTitle"),
        content: t("taskBoardEntryDeleteAreYouSureText"),
        confirmButtonLabel: t("taskBoardEntryDeleteAreYouSureConfirmLabel"),
        onConfirm: removeElementFromBoard,
      })
    );
  };

  return (
    <div className={styles.itemContainer}>
      <TaskRow className={styles.taskRow} task={item.task} withBottomBorderLine={false} />
      <Button
        heightVariant={ButtonHeight.short}
        className={styles.itemDetailButton}
        variant={ButtonVariants.filled}
        data-tooltip-right={t("taskBoardItemRemoveElementTooltip")}
        onClick={popAreYouSureModalForLogout}
      >
        <IoRemoveCircleOutline size={14} />
      </Button>
      <div className="spacer-w-1" />
    </div>
  );
};

export default TaskBoardElement;
