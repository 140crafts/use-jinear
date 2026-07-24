import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import type {NotebookDto, NotebookVisibilityType} from "@/model/be/jinear-core";
import {useUpdateNotebookMutation} from "@/store/api/notebookOperationApi";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import toast from "react-hot-toast";
import styles from "./NotebookGeneralSettings.module.css";

interface NotebookGeneralSettingsProps {
    notebook: NotebookDto;
    accountHasEditRole: boolean;
}

const VISIBILITY_TYPES: NotebookVisibilityType[] = ["PUBLIC_WITHIN_WORKSPACE", "SHARED", "PRIVATE"];

const NotebookGeneralSettings: React.FC<NotebookGeneralSettingsProps> = ({notebook, accountHasEditRole}) => {
    const {t} = useTranslation();
    const [updateNotebook, {isLoading, isSuccess}] = useUpdateNotebookMutation();

    const [title, setTitle] = useState<string>(notebook.title);
    const [visibility, setVisibility] = useState<NotebookVisibilityType>(notebook.visibility);

    useEffect(() => {
        setTitle(notebook.title);
        setVisibility(notebook.visibility);
    }, [notebook]);

    useEffect(() => {
        if (isSuccess) {
            toast(t("notebookSettingsSavedToast"));
        }
    }, [isSuccess]);

    const changeVisibility = (value: string) => {
        if (value === "PRIVATE" || value === "SHARED" || value === "PUBLIC_WITHIN_WORKSPACE") {
            setVisibility(value);
        }
    };

    const isDirty = title.trim() !== notebook.title || visibility !== notebook.visibility;

    const saveChanges = () => {
        updateNotebook({notebookId: notebook.notebookId, title: title.trim(), visibility});
    };

    return (
        <div className={styles.container}>
            <SectionTitle
                title={t("notebookSettingsGeneralSectionTitle")}
                description={t("notebookSettingsGeneralSectionDescription")}
            />

            <div className={styles.content}>
                <label className={cn(styles.label, "flex-1")} htmlFor={"notebook-settings-title"}>
                    {t("notebookSettingsTitleLabel")}
                    <input
                        id={"notebook-settings-title"}
                        type={"text"}
                        value={title}
                        disabled={!accountHasEditRole}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                </label>

                <label className={cn(styles.label, "flex-1")} htmlFor={"notebook-settings-visibility-segment-control"}>
                    {t("notebookInitializeFormVisibility")}
                    <div className={styles.visibilityTypeContainer}>
                        <SegmentedControl
                            id="notebook-settings-visibility-segment-control"
                            name="notebook-settings-visibility-segment-control"
                            defaultIndex={VISIBILITY_TYPES.indexOf(visibility)}
                            segments={VISIBILITY_TYPES.map((type) => ({
                                label: t(`notebookVisibility_${type}`),
                                value: type,
                                inputProps: {disabled: !accountHasEditRole}
                            }))}
                            callback={changeVisibility}
                        />
                        <label>{t(`notebookVisibilityDetail_${visibility}`)}</label>
                    </div>
                </label>

                <div className={styles.actionButtonContainer}>
                    {accountHasEditRole && isDirty && (
                        <Button
                            loading={isLoading}
                            disabled={isLoading || title.trim().length === 0}
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.contrast}
                            onClick={saveChanges}
                        >
                            {t("notebookSettingsSaveButton")}
                        </Button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default NotebookGeneralSettings;
