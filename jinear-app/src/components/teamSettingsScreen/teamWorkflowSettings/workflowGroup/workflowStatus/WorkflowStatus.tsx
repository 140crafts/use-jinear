import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {TeamWorkflowStatusDto} from "@/model/be/jinear-core";
import {useAppDispatch} from "@/store";
import {
    useChangeTeamWorkflowStatusNameMutation,
    useRemoveTeamWorkflowStatusMutation,
} from "@/store/api/teamWorkflowStatusApi";
import {closeDialogModal, popDialogModal} from "@/store/slice/modalSlice";
import {retrieveTaskStatusIcon} from "@/util/taskIconFactory";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {type ChangeEvent, type KeyboardEvent, useEffect, useRef, useState} from "react";
import {toast} from "react-hot-toast";
import {IoCaretDown, IoCaretUp, IoClose, IoPencil, IoReorderThreeOutline} from "react-icons/io5";
import styles from "./WorkflowStatus.module.scss";

type DragHandlers = Pick<
    React.HTMLAttributes<HTMLDivElement>,
    "onDragStart" | "onDragOver" | "onDragLeave" | "onDragEnd" | "onDrop"
>;

interface WorkflowStatusProps {
    editable: boolean;
    deletable: boolean;
    orderChangable: boolean;
    className?: string;
    dragHandlers: DragHandlers;
    canMoveUp: boolean;
    canMoveDown: boolean;
    onMoveUp: () => void;
    onMoveDown: () => void;
    workflowDto: TeamWorkflowStatusDto;
}

const WorkflowStatus: React.FC<WorkflowStatusProps> = ({
                                                           workflowDto,
                                                           deletable,
                                                           orderChangable,
                                                           editable,
                                                           className,
                                                           dragHandlers,
                                                           canMoveUp,
                                                           canMoveDown,
                                                           onMoveUp,
                                                           onMoveDown,
                                                       }) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [isEditing, setIsEditing] = useState<boolean>(false);

    const [name, setName] = useState<string>(workflowDto.name);
    const nameInputRef = useRef<HTMLInputElement>(null);

    const [changeTeamWorkflowStatusName, {isLoading: isNameChangeLoading, isSuccess: isNameChangeSuccess}] =
        useChangeTeamWorkflowStatusNameMutation();
    const [removeTeamWorkflowStatus, {isLoading: isRemoveLoading}] = useRemoveTeamWorkflowStatusMutation();

    const StatusIcon = retrieveTaskStatusIcon(workflowDto.workflowStateGroup);

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

    const cancelEditing = () => {
        setName(workflowDto.name);
        setIsEditing(false);
    };

    const onNameInputKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key == "Enter") {
            e.preventDefault();
            submitNewName();
        }
        if (e.key == "Escape") {
            e.preventDefault();
            cancelEditing();
        }
    };

    const deleteWorkflowStatus = () => {
        removeTeamWorkflowStatus({
            teamId: workflowDto.teamId,
            teamWorkflowStatusId: workflowDto.teamWorkflowStatusId,
        })
            .unwrap()
            .catch(() => {
                toast(t("workflowStatusDeleteFailed"));
            });
        dispatch(closeDialogModal());
    };

    const popAreYouSureModalForDeleteWorkflowStatus = () => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("workflowStatusDeleteAreYouSureTitle"),
                content: t("workflowStatusDeleteAreYouSureText"),
                confirmButtonLabel: t("workflowStatusDeleteAreYouSureConfirmLabel"),
                onConfirm: deleteWorkflowStatus,
            })
        );
    };

    const draggable = orderChangable && !isEditing;

    return (
        <div
            className={cn(styles.container, className, draggable && styles.draggable)}
            data-tooltip={workflowDto.name?.length > 36 ? workflowDto.name : undefined}
            draggable={draggable}
            {...dragHandlers}
        >
            {draggable && (
                <div className={styles.dragHandle} data-tooltip-right={t("workflowStatusDragTooltip")}>
                    <IoReorderThreeOutline size={18}/>
                </div>
            )}
            <div className={styles.icon}><StatusIcon size={20}/></div>

            {!isEditing && <div className={cn(styles.name, "single-line")}>{workflowDto.name}</div>}
            {isEditing && editable && (
                <input ref={nameInputRef} type="text" value={name} onChange={onNameChange}
                       onKeyDown={onNameInputKeyDown} className={styles.nameInput}/>
            )}

            {!isEditing && <div className="flex-1"/>}
            {!isEditing && (
                <div className={styles.actionContainer}>
                    {orderChangable && canMoveDown && (
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.hoverFilled2}
                            data-tooltip-right={t("workflowStatusOrderDownTooltip")}
                            onClick={onMoveDown}
                        >
                            <IoCaretDown/>
                        </Button>
                    )}
                    {orderChangable && canMoveUp && (
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.hoverFilled2}
                            data-tooltip-right={t("workflowStatusOrderUpTooltip")}
                            onClick={onMoveUp}
                        >
                            <IoCaretUp/>
                        </Button>
                    )}

                    {editable && (
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.hoverFilled2}
                            data-tooltip-right={t("workflowStatusEditTooltip")}
                            onClick={() => {
                                setIsEditing(true);
                            }}
                        >
                            <IoPencil/>
                        </Button>
                    )}
                    {deletable && editable && (
                        <Button
                            disabled={isRemoveLoading}
                            loading={isRemoveLoading}
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.hoverFilled2}
                            data-tooltip-right={t("workflowStatusDeleteTooltip")}
                            onClick={popAreYouSureModalForDeleteWorkflowStatus}
                        >
                            <IoClose/>
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
                        onClick={cancelEditing}
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
