import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {ChecklistItemDto} from "@/model/be/jinear-core";
import {
    useDeleteChecklistItemMutation,
    useInitializeChecklistItemMutation,
    useUpdateCheckedStatusMutation,
    useUpdateLabelMutation,
} from "@/store/api/taskChecklistItemApi";
import Logger from "@/util/logger";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {type ChangeEvent, type  KeyboardEvent, useRef, useState} from "react";
import {IoCheckmarkCircleSharp, IoRadioButtonOff} from "react-icons/io5";
import {LuTrash2} from "react-icons/lu";
import {CHECKLIST_ICON_SIZE} from "../TaskChecklist";
import styles from "./ChecklistItem.module.css";

interface ChecklistItemProps {
    checklistId: string;
    checklistItem?: ChecklistItemDto;
    asNewItem?: boolean;
}

const logger = Logger("ChecklistItem");

const ChecklistItem: React.FC<ChecklistItemProps> = ({checklistId, checklistItem, asNewItem = false}) => {
    const {t} = useTranslation();
    const [isChecked, setIsChecked] = useState(checklistItem?.isChecked);
    const [label, setLabel] = useState(checklistItem?.label || "");

    const [updateCheckedStatus] = useUpdateCheckedStatusMutation();
    const [updateLabel, {isLoading: isUpdateLabelLoading}] = useUpdateLabelMutation();
    const [deleteChecklistItem, {isLoading: isDeleteLoading}] = useDeleteChecklistItemMutation();
    const [initializeChecklistItem, {isLoading: isInitializeLoading}] = useInitializeChecklistItemMutation();
    const saveInProgressRef = useRef(false);

    const anyOngoingCallExists = isInitializeLoading || isUpdateLabelLoading || isDeleteLoading;
    const savedLabel = checklistItem?.label || "";
    const changed = savedLabel != label;

    const toggleStatus = () => {
        const checked = !isChecked;
        if (checklistItem) {
            updateCheckedStatus({checklistItemId: checklistItem?.checklistItemId, checked});
            setIsChecked(checked);
        }
    };

    const onLabelChange = (event: ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        setLabel(value);
    };

    const onLabelKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
        if (event.key == "Enter") {
            save();
        } else if (event.key == "Escape") {
            setLabel(savedLabel);
        }
    };

    const deleteItem = () => {
        if (checklistItem) {
            deleteChecklistItem({checklistItemId: checklistItem.checklistItemId});
        }
    };

    const clearLabel = () => {
        setLabel("");
    };

    const releaseSave = () => {
        saveInProgressRef.current = false;
    };

    const saveChange = () => {
        logger.log({checklistItemLabel: checklistItem?.label, label});
        if (checklistItem && savedLabel != "" && label == "") {
            return deleteChecklistItem({checklistItemId: checklistItem.checklistItemId}).unwrap();
        }
        if (checklistItem) {
            return updateLabel({
                checklistId: checklistItem.checklistId,
                checklistItemId: checklistItem.checklistItemId,
                label,
            }).unwrap();
        }
        return initializeChecklistItem({checklistId, label}).unwrap().then(clearLabel);
    };

    const save = () => {
        if (!changed || saveInProgressRef.current) {
            return;
        }
        saveInProgressRef.current = true;
        saveChange()
            .catch(logger.log)
            .finally(releaseSave);
    };

    return (
        <div className={cn(styles.container, asNewItem && label == "" && styles.emptyNewItem)}>
            <div className={styles.contentContainer}>
                <Button onClick={toggleStatus} disabled={anyOngoingCallExists || asNewItem}>
                    {isChecked ? <IoCheckmarkCircleSharp size={CHECKLIST_ICON_SIZE}/> :
                        <IoRadioButtonOff size={CHECKLIST_ICON_SIZE}/>}
                </Button>
                <input
                    disabled={anyOngoingCallExists}
                    type={"text"}
                    value={label}
                    className={cn(styles.input, isChecked && styles.inputChecked)}
                    onChange={onLabelChange}
                    onBlur={save}
                    onKeyDown={onLabelKeyDown}
                    placeholder={asNewItem ? t("checklistNewItemPlaceholder") : undefined}
                />
                {changed && (
                    <Button
                        variant={ButtonVariants.contrast}
                        heightVariant={ButtonHeight.short}
                        disabled={anyOngoingCallExists}
                        loading={isInitializeLoading || isUpdateLabelLoading}
                        onClick={save}
                    >
                        {t("checklistItemSaveButton")}
                    </Button>
                )}
                {!changed && checklistItem && (
                    <Button
                        variant={ButtonVariants.hoverFilled2}
                        heightVariant={ButtonHeight.short}
                        className={styles.deleteButton}
                        disabled={anyOngoingCallExists}
                        loading={isDeleteLoading}
                        onClick={deleteItem}
                        data-tooltip-right={t("checklistItemDeleteButtonTooltip")}
                    >
                        <LuTrash2/>
                    </Button>
                )}
            </div>
        </div>
    );
};

export default ChecklistItem;
