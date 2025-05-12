import Button from "@/components/button";
import { ChecklistItemDto } from "@/model/be/jinear-core";
import {
  useDeleteChecklistItemMutation,
  useInitializeChecklistItemMutation,
  useUpdateCheckedStatusMutation,
  useUpdateLabelMutation,
} from "@/store/api/taskChecklistItemApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, KeyboardEvent, useEffect, useState } from "react";
import { IoCheckmarkCircleSharp, IoRadioButtonOff } from "react-icons/io5";
import { CHECKLIST_ICON_SIZE } from "../TaskChecklist";
import styles from "./ChecklistItem.module.css";

interface ChecklistItemProps {
  checklistId: string;
  checklistItem?: ChecklistItemDto;
  asNewItem?: boolean;
}

const logger = Logger("ChecklistItem");

const ChecklistItem: React.FC<ChecklistItemProps> = ({ checklistId, checklistItem, asNewItem = false }) => {
  const { t } = useTranslation();
  const [isChecked, setIsChecked] = useState(checklistItem?.isChecked);
  const [label, setLabel] = useState(checklistItem?.label || "");

  const [updateCheckedStatus] = useUpdateCheckedStatusMutation();
  const [updateLabel, { isLoading: isUpdateLabelLoading }] = useUpdateLabelMutation();
  const [deleteChecklistItem, { isLoading: isDeleteLoading }] = useDeleteChecklistItemMutation();
  const [initializeChecklistItem, { isSuccess: isInitializeSuccess, isLoading: isInitializeLoading }] =
    useInitializeChecklistItemMutation();

  const anyOngoingCallExists = isInitializeLoading || isUpdateLabelLoading || isDeleteLoading;

  useEffect(() => {
    if (isInitializeSuccess) {
      setLabel("");
    }
  }, [isInitializeSuccess]);

  const toggleStatus = () => {
    const checked = !isChecked;
    if (checklistItem) {
      updateCheckedStatus({ checklistItemId: checklistItem?.checklistItemId, checked });
      setIsChecked(checked);
    }
  };

  const onLabelChange = (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setLabel(value);
  };

  const onLabelKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key == "Enter") {
      onLabelBlur();
    }
  };

  const onLabelBlur = () => {
    const itemLabel = checklistItem?.label || "";
    const changed = itemLabel != label;
    const oldLabelExistsNewLabelEmpty = itemLabel != "" && label == "";
    logger.log({ checklistItemLabel: checklistItem?.label, label });
    if (changed && checklistItem && oldLabelExistsNewLabelEmpty) {
      deleteChecklistItem({ checklistItemId: checklistItem?.checklistItemId });
    } else if (changed && checklistItem) {
      updateLabel({
        checklistId: checklistItem.checklistId,
        checklistItemId: checklistItem.checklistItemId,
        label: label || "",
      });
    } else if (changed) {
      initializeChecklistItem({
        checklistId: checklistId,
        label: label || "",
      });
    }
  };

  return (
    <div className={cn(styles.container, asNewItem && label == "" && styles.emptyNewItem)}>
      <div className={styles.contentContainer}>
        <Button onClick={toggleStatus} disabled={anyOngoingCallExists || asNewItem}>
          {isChecked ? <IoCheckmarkCircleSharp size={CHECKLIST_ICON_SIZE} /> : <IoRadioButtonOff size={CHECKLIST_ICON_SIZE} />}
        </Button>
        <input
          disabled={anyOngoingCallExists}
          type={"text"}
          value={label}
          className={cn(styles.input, isChecked && styles.inputChecked)}
          onChange={onLabelChange}
          onBlur={onLabelBlur}
          onKeyDown={onLabelKeyDown}
          placeholder={asNewItem ? t("checklistNewItemPlaceholder") : undefined}
        />
      </div>
    </div>
  );
};

export default ChecklistItem;
