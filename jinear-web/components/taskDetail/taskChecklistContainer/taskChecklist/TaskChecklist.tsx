import Button from "@/components/button";
import { ChecklistDto } from "@/model/be/jinear-core";
import {
  usePassivizeChecklistMutation,
  useRetrieveChecklistQuery,
  useUpdateChecklistLabelMutation,
} from "@/store/api/taskChecklistApi";
import { changeLoadingModalVisibility, closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, KeyboardEvent, useEffect, useState } from "react";
import { IoClose } from "react-icons/io5";
import ChecklistItem from "./checklistItem/ChecklistItem";
import styles from "./TaskChecklist.module.scss";

interface TaskChecklistProps {
  initialChecklist: ChecklistDto;
}

export const CHECKLIST_ICON_SIZE = 18;

const TaskChecklist: React.FC<TaskChecklistProps> = ({ initialChecklist }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [checklist, setChecklist] = useState(initialChecklist);
  const [title, setTitle] = useState(initialChecklist.title);

  const {
    data: retrieveChecklistResponse,
    isLoading,
    isFetching: isRetrieveChecklistFetching,
  } = useRetrieveChecklistQuery({ checklistId: initialChecklist.checklistId });

  const [updateChecklistLabel, { isLoading: isUpdateLabelLoading }] = useUpdateChecklistLabelMutation();
  const [passivizeChecklist, { isLoading: isPassivizeChecklistLoading }] = usePassivizeChecklistMutation();

  useEffect(() => {
    if (retrieveChecklistResponse) {
      setChecklist(retrieveChecklistResponse.data);
    }
  }, [retrieveChecklistResponse]);

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isPassivizeChecklistLoading }));
    if (!isPassivizeChecklistLoading) {
      dispatch(closeDialogModal());
    }
  }, [isPassivizeChecklistLoading]);

  const onTitleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setTitle(value);
  };

  const onTitleInputBlur = () => {
    const checklistTitle = initialChecklist.title || "";
    const changed = checklistTitle != title;
    if (changed) {
      const titleToSave = title == "" ? t("checklistNewChecklistTitle") : title;
      updateChecklistLabel({
        title: titleToSave,
        checklistId: checklist.checklistId,
      });
      setTitle(titleToSave);
    }
  };

  const onTitleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key == "Enter") {
      onTitleInputBlur();
    }
  };

  const deleteChecklist = () => {
    passivizeChecklist({ checklistId: checklist.checklistId });
  };

  const popAreYouSureModalForChecklistDelete = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("checklistDeleteAreYouSureTitle"),
        content: t("checklistDeleteAreYouSureText"),
        confirmButtonLabel: t("checklistDeleteAreYouSureConfirmText"),
        onConfirm: deleteChecklist,
      })
    );
  };

  return (
    <div className={styles.container}>
      <div className={styles.headerContainer}>
        <input
          disabled={isRetrieveChecklistFetching || isUpdateLabelLoading}
          type="text"
          value={title}
          className={styles.titleInput}
          onChange={onTitleChange}
          onBlur={onTitleInputBlur}
          onKeyDown={onTitleKeyDown}
        />
        <Button
          disabled={isPassivizeChecklistLoading}
          loading={isPassivizeChecklistLoading}
          onClick={popAreYouSureModalForChecklistDelete}
          data-tooltip-right={t("checklistDeleteButtonTooltip")}
        >
          <IoClose />
        </Button>
      </div>
      <div className={styles.itemsContainer}>
        {checklist.checklistItems.map((checklistItem) => (
          <ChecklistItem
            key={checklistItem.checklistItemId}
            checklistId={initialChecklist.checklistId}
            checklistItem={checklistItem}
          />
        ))}
        <ChecklistItem key={`new-checklistItem`} checklistId={initialChecklist.checklistId} asNewItem={true} />
      </div>
    </div>
  );
};

export default TaskChecklist;
