import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {TeamWorkflowStateGroup} from "@/model/be/jinear-core";
import {useInitializeTeamWorkflowStatusMutation} from "@/store/api/teamWorkflowStatusApi";
import useTranslation from "@/locales/useTranslation";
import React, {type ChangeEvent, type KeyboardEvent, useEffect, useRef, useState} from "react";
import {toast} from "react-hot-toast";
import {IoAdd} from "react-icons/io5";
import styles from "./AddWorkflowStatus.module.scss";

interface AddWorkflowStatusProps {
    teamId: string;
    groupType: TeamWorkflowStateGroup;
}

const AddWorkflowStatus: React.FC<AddWorkflowStatusProps> = ({teamId, groupType}) => {
    const {t} = useTranslation();
    const [isAdding, setIsAdding] = useState<boolean>(false);
    const [name, setName] = useState<string>("");
    const nameInputRef = useRef<HTMLInputElement>(null);

    const [initializeTeamWorkflowStatus, {isLoading, isSuccess}] = useInitializeTeamWorkflowStatusMutation();

    useEffect(() => {
        if (isAdding) {
            nameInputRef.current?.focus();
        }
    }, [isAdding]);

    useEffect(() => {
        if (!isLoading && isSuccess) {
            setName("");
            setIsAdding(false);
        }
    }, [isLoading, isSuccess]);

    const onNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        setName(e.target.value);
    };

    const closeAdding = () => {
        setName("");
        setIsAdding(false);
    };

    const submitNewStatus = () => {
        const trimmedName = name.trim();
        // Backend marks the name as not blank, so an empty submit would fail with an unexplained 400.
        if (trimmedName.length == 0) {
            return;
        }
        initializeTeamWorkflowStatus({
            teamId,
            initializeTeamWorkflowStatusRequest: {
                workflowStateGroup: groupType,
                name: trimmedName,
            },
        })
            .unwrap()
            .catch(() => {
                toast(t("workflowStatusAddFailed"));
            });
    };

    const onNameInputKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key == "Enter") {
            e.preventDefault();
            submitNewStatus();
        }
        if (e.key == "Escape") {
            e.preventDefault();
            closeAdding();
        }
    };

    if (!isAdding) {
        return (
            <Button
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.hoverFilled2}
                className={styles.addButton}
                onClick={() => setIsAdding(true)}
            >
                <IoAdd size={16}/>
                <span>{t("workflowStatusAddButton")}</span>
            </Button>
        );
    }

    return (
        <div className={styles.container}>
            <input
                ref={nameInputRef}
                type="text"
                value={name}
                onChange={onNameChange}
                onKeyDown={onNameInputKeyDown}
                placeholder={t("workflowStatusAddPlaceholder")}
                className={styles.nameInput}
            />
            <div className={styles.actionContainer}>
                <Button
                    disabled={isLoading}
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.hoverFilled2}
                    onClick={closeAdding}
                >
                    {t("workflowStatusAddCancel")}
                </Button>
                <Button
                    disabled={isLoading || name.trim().length == 0}
                    loading={isLoading}
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.contrast}
                    onClick={submitNewStatus}
                >
                    {t("workflowStatusAddSave")}
                </Button>
            </div>
        </div>
    );
};

export default AddWorkflowStatus;
