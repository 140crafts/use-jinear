import Button, {ButtonVariants} from "@/components/button";
import Modal from "@/components/modal/modal/Modal";
import type {AccountDto, AdminWorkspaceInitializeRequest} from "@/model/be/jinear-core";
import {useAdminInitializeWorkspaceMutation} from "@/store/api/adminWorkspaceApi";
import {
    closeAdminCreateWorkspaceModal,
    popAdminAccountPickerModal,
    selectAdminCreateWorkspaceModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import {normalizeUsernameReplaceSpaces} from "@/util/normalizeHelper";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import toast from "react-hot-toast";
import styles from "./AdminCreateWorkspaceModal.module.css";

interface AdminCreateWorkspaceModalProps {
}

interface IAdminNewWorkspaceForm {
    title: string;
    handle: string;
}

const AdminCreateWorkspaceModal: React.FC<AdminCreateWorkspaceModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectAdminCreateWorkspaceModalVisible);
    const {register, handleSubmit, setValue, watch, reset} = useForm<IAdminNewWorkspaceForm>();
    const [pickedOwner, setPickedOwner] = useState<AccountDto | undefined>();
    const [adminInitializeWorkspace, {isLoading, isSuccess}] = useAdminInitializeWorkspaceMutation();
    const currTitle = watch("title");

    const close = () => {
        dispatch(closeAdminCreateWorkspaceModal());
    };

    useEffect(() => {
        if (currTitle && currTitle.length > 0) {
            setValue("handle", normalizeUsernameReplaceSpaces(currTitle)?.substring(0, 255));
        }
    }, [currTitle]);

    useEffect(() => {
        if (isSuccess) {
            toast(t("adminWorkspaceCreatedToast"));
            reset();
            setPickedOwner(undefined);
            close();
        }
    }, [isSuccess]);

    const popOwnerPicker = () => {
        dispatch(popAdminAccountPickerModal({visible: true, onPick: setPickedOwner}));
    };

    const submit: SubmitHandler<IAdminNewWorkspaceForm> = (data) => {
        if (!pickedOwner) {
            return;
        }
        adminInitializeWorkspace({
            title: data.title,
            handle: data.handle,
            ownerAccountId: pickedOwner.accountId
        } as AdminWorkspaceInitializeRequest);
    };

    return (
        <Modal
            visible={visible}
            title={t("adminNewWorkspaceModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <form autoComplete="off" id={"admin-new-workspace-form"} className={styles.form}
                  onSubmit={handleSubmit(submit)} action="#">
                <label className={styles.label} htmlFor={"admin-new-workspace-title"}>
                    {`${t("adminNewWorkspaceFormTitleLabel")} *`}
                    <input
                        id={"admin-new-workspace-title"}
                        type={"text"}
                        {...register("title", {required: t("formRequiredField")})}
                    />
                </label>

                <label className={styles.label} htmlFor={"admin-new-workspace-handle"}>
                    {`${t("adminNewWorkspaceFormHandleLabel")} *`}
                    <input
                        id={"admin-new-workspace-handle"}
                        type={"text"}
                        minLength={3}
                        maxLength={255}
                        {...register("handle", {required: t("formRequiredField"), minLength: 3})}
                    />
                </label>

                <div className={styles.label}>
                    {`${t("adminNewWorkspaceFormOwnerLabel")} *`}
                    <Button
                        type={"button"}
                        variant={ButtonVariants.filled}
                        onClick={popOwnerPicker}
                    >
                        {pickedOwner ? pickedOwner.email : t("adminNewWorkspaceFormPickOwnerLabel")}
                    </Button>
                </div>

                <div className={styles.footerContainer}>
                    <Button type={"button"} disabled={isLoading} onClick={close} className={styles.footerButton}>
                        {t("newWorkspaceFormCancel")}
                    </Button>
                    <Button
                        type={"submit"}
                        disabled={isLoading || !pickedOwner}
                        loading={isLoading}
                        className={styles.footerButton}
                        variant={ButtonVariants.contrast}
                    >
                        {t("adminNewWorkspaceFormSubmitLabel")}
                    </Button>
                </div>
            </form>
        </Modal>
    );
};

export default AdminCreateWorkspaceModal;
