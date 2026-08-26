import Button, {ButtonVariants} from "@/components/button";
import Modal from "@/components/modal/modal/Modal";
import type {AdminAccountCreateRequest} from "@/model/be/jinear-core";
import {useAdminCreateAccountMutation} from "@/store/api/adminAccountApi";
import {closeAdminCreateAccountModal, selectAdminCreateAccountModalVisible} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import toast from "react-hot-toast";
import styles from "./AdminCreateAccountModal.module.css";

interface AdminCreateAccountModalProps {
}

const AdminCreateAccountModal: React.FC<AdminCreateAccountModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectAdminCreateAccountModalVisible);
    const {register, handleSubmit, reset} = useForm<AdminAccountCreateRequest>();
    const [adminCreateAccount, {isLoading, isSuccess}] = useAdminCreateAccountMutation();

    const close = () => {
        dispatch(closeAdminCreateAccountModal());
    };

    useEffect(() => {
        if (isSuccess) {
            toast(t("adminAccountCreatedToast"));
            reset();
            close();
        }
    }, [isSuccess]);

    const submit: SubmitHandler<AdminAccountCreateRequest> = (data) => {
        adminCreateAccount({email: data.email, password: data.password} as AdminAccountCreateRequest);
    };

    return (
        <Modal
            visible={visible}
            title={t("adminNewAccountModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <form autoComplete="off" id={"admin-new-account-form"} className={styles.form}
                  onSubmit={handleSubmit(submit)} action="#">
                <label className={styles.label} htmlFor={"admin-new-account-email"}>
                    {`${t("adminNewAccountFormEmailLabel")} *`}
                    <input
                        id={"admin-new-account-email"}
                        type={"email"}
                        {...register("email", {required: t("formRequiredField")})}
                    />
                </label>

                <label className={styles.label} htmlFor={"admin-new-account-password"}>
                    {`${t("adminNewAccountFormPasswordLabel")} *`}
                    <input
                        id={"admin-new-account-password"}
                        type={"password"}
                        minLength={6}
                        {...register("password", {required: t("formRequiredField"), minLength: 6})}
                    />
                </label>

                <div className={styles.footerContainer}>
                    <Button type={"button"} disabled={isLoading} onClick={close} className={styles.footerButton}>
                        {t("newWorkspaceFormCancel")}
                    </Button>
                    <Button
                        type={"submit"}
                        disabled={isLoading}
                        loading={isLoading}
                        className={styles.footerButton}
                        variant={ButtonVariants.contrast}
                    >
                        {t("adminNewAccountFormSubmitLabel")}
                    </Button>
                </div>
            </form>
        </Modal>
    );
};

export default AdminCreateAccountModal;
