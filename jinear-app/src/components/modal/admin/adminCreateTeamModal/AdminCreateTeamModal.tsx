import Button, {ButtonVariants} from "@/components/button";
import Modal from "@/components/modal/modal/Modal";
import type {TeamInitializeRequest, WorkspaceDto} from "@/model/be/jinear-core";
import {useAdminInitializeTeamMutation} from "@/store/api/adminTeamApi";
import {
    closeAdminCreateTeamModal,
    popAdminWorkspacePickerModal,
    selectAdminCreateTeamModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import {normalizeUsernameReplaceSpaces} from "@/util/normalizeHelper";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import toast from "react-hot-toast";
import styles from "./AdminCreateTeamModal.module.css";

interface AdminCreateTeamModalProps {
}

interface IAdminNewTeamForm {
    name: string;
    tag: string;
    username: string;
}

const MAX_TAG_LENGTH = 10;

const AdminCreateTeamModal: React.FC<AdminCreateTeamModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectAdminCreateTeamModalVisible);
    const {register, handleSubmit, setValue, watch, reset} = useForm<IAdminNewTeamForm>();
    const [pickedWorkspace, setPickedWorkspace] = useState<WorkspaceDto | undefined>();
    const [adminInitializeTeam, {isLoading, isSuccess}] = useAdminInitializeTeamMutation();
    const currName = watch("name");
    const currTag = watch("tag");

    const close = () => {
        dispatch(closeAdminCreateTeamModal());
    };

    useEffect(() => {
        const normalizedUsername = normalizeUsernameReplaceSpaces(currName);
        const normalizedTag = normalizedUsername?.substring(0, MAX_TAG_LENGTH)?.toLocaleUpperCase();
        setValue("tag", normalizedTag);
        setValue("username", normalizedUsername);
    }, [currName]);

    useEffect(() => {
        const normalizedTag = normalizeUsernameReplaceSpaces(currTag)?.substring(0, MAX_TAG_LENGTH)?.toLocaleUpperCase();
        setValue("tag", normalizedTag);
    }, [currTag]);

    useEffect(() => {
        if (isSuccess) {
            toast(t("adminTeamCreatedToast"));
            reset();
            setPickedWorkspace(undefined);
            close();
        }
    }, [isSuccess]);

    const popWorkspacePicker = () => {
        dispatch(popAdminWorkspacePickerModal({visible: true, onPick: setPickedWorkspace}));
    };

    const submit: SubmitHandler<IAdminNewTeamForm> = (data) => {
        if (!pickedWorkspace) {
            return;
        }
        adminInitializeTeam({
            workspaceId: pickedWorkspace.workspaceId,
            name: data.name,
            username: data.username,
            tag: data.tag
        } as TeamInitializeRequest);
    };

    return (
        <Modal
            visible={visible}
            title={t("adminNewTeamModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <form autoComplete="off" id={"admin-new-team-form"} className={styles.form} onSubmit={handleSubmit(submit)}
                  action="#">
                <input id={"admin-new-team-username"} type={"hidden"} {...register("username")} />

                <div className={styles.label}>
                    {`${t("adminNewTeamFormWorkspaceLabel")} *`}
                    <Button
                        type={"button"}
                        variant={ButtonVariants.filled}
                        onClick={popWorkspacePicker}
                    >
                        {pickedWorkspace ? pickedWorkspace.title : t("adminNewTeamFormPickWorkspaceLabel")}
                    </Button>
                </div>

                <label className={styles.label} htmlFor={"admin-new-team-name"}>
                    {`${t("adminNewTeamFormNameLabel")} *`}
                    <input
                        id={"admin-new-team-name"}
                        type={"text"}
                        {...register("name", {required: t("formRequiredField")})}
                    />
                </label>

                <label className={styles.label} htmlFor={"admin-new-team-tag"}>
                    {`${t("adminNewTeamFormTagLabel")} * ${currTag?.length ?? 0}/${MAX_TAG_LENGTH}`}
                    <input
                        id={"admin-new-team-tag"}
                        type={"text"}
                        minLength={1}
                        maxLength={MAX_TAG_LENGTH}
                        {...register("tag", {required: t("formRequiredField")})}
                    />
                </label>

                <div className={styles.footerContainer}>
                    <Button type={"button"} disabled={isLoading} onClick={close} className={styles.footerButton}>
                        {t("newWorkspaceFormCancel")}
                    </Button>
                    <Button
                        type={"submit"}
                        disabled={isLoading || !pickedWorkspace}
                        loading={isLoading}
                        className={styles.footerButton}
                        variant={ButtonVariants.contrast}
                    >
                        {t("adminNewTeamFormSubmitLabel")}
                    </Button>
                </div>
            </form>
        </Modal>
    );
};

export default AdminCreateTeamModal;
