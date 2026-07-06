import Button, {ButtonVariants} from "@/components/button";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import type {NotebookInitializeRequest, NotebookVisibilityType, WorkspaceDto} from "@/model/be/jinear-core";
import {useInitializeNotebookMutation} from "@/store/api/notebookOperationApi";
import {changeLoadingModalVisibility} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import styles from "./NotebookInitializeForm.module.css";

interface NotebookInitializeFormProps {
    workspace: WorkspaceDto;
    close: () => void;
}

const VISIBILITY_TYPES: NotebookVisibilityType[] = ["PUBLIC_WITHIN_WORKSPACE", "SHARED", "PRIVATE"];

const NotebookInitializeForm: React.FC<NotebookInitializeFormProps> = ({workspace, close}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [initializeNotebook, {data: initializeResponse, isLoading}] = useInitializeNotebookMutation();
    const {register, setFocus, handleSubmit} = useForm<NotebookInitializeRequest>();
    const [visibility, setVisibility] = useState<NotebookVisibilityType>("PUBLIC_WITHIN_WORKSPACE");

    useEffect(() => {
        setFocus('title')
    }, []);

    useEffect(() => {
        initializeResponse && close?.();
    }, [initializeResponse]);

    const changeVisibility = (value: string, index: number) => {
        if (value === "PRIVATE" || value === "SHARED" || value === "PUBLIC_WITHIN_WORKSPACE") {
            setVisibility(value);
        }
    };

    const submit: SubmitHandler<NotebookInitializeRequest> = (data) => {
        initializeNotebook({workspaceId: workspace.workspaceId, body: {...data, visibility}});
    };

    return (
        <form autoComplete="off" id={"notebook-initialize-form"} className={styles.form} onSubmit={handleSubmit(submit)}
              action="#">
            <label className={cn(styles.label, "flex-1")} htmlFor={"notebook-initialize-title"}>
                {`${t("notebookInitializeFormTitle")} *`}
                <input
                    id={"notebook-initialize-title"}
                    type={"text"}
                    {...register("title", {required: t("formRequiredField")})}
                />
            </label>

            <label className={cn(styles.label, "flex-1")} htmlFor={"notebook-initialize-visibility-segment-control"}>
                {t("notebookInitializeFormVisibility")}
                <div className={styles.visibilityTypeContainer}>
                    <SegmentedControl
                        id="notebook-initialize-visibility-segment-control"
                        name="notebook-initialize-visibility-segment-control"
                        defaultIndex={VISIBILITY_TYPES.indexOf(visibility)}
                        segments={VISIBILITY_TYPES.map((type) => ({
                            label: t(`notebookVisibility_${type}`),
                            value: type,
                        }))}
                        segmentLabelClassName={styles.viewTypeSegmentLabel}
                        callback={changeVisibility}
                    />
                    <label>{t(`notebookVisibilityDetail_${visibility}`)}</label>
                </div>
            </label>

            <div className={styles.footerContainer}>
                <Button disabled={isLoading} onClick={close} className={styles.footerButton}>
                    {t("notebookInitializeFormCancel")}
                </Button>
                <Button
                    type="submit"
                    disabled={isLoading}
                    loading={isLoading}
                    className={styles.footerButton}
                    variant={ButtonVariants.contrast}
                >
                    {t("notebookInitializeFormCreate")}
                </Button>
            </div>
        </form>
    );
};

export default NotebookInitializeForm;
