import Button, { ButtonVariants } from "@/components/button";
import { useUpdatePasswordMutation } from "@/store/api/accountPasswordApi";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import styles from "./ChangePasswordForm.module.css";

interface ChangePasswordFormProps {}

export interface IChangePasswordForm {
  currentPassword: string;
  newPassword: string;
  newPasswordControl: string;
}

const logger = Logger("ChangePasswordForm");

const ChangePasswordForm: React.FC<ChangePasswordFormProps> = ({}) => {
  const { t } = useTranslation();
  const {
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm<IChangePasswordForm>();
  const [updatePassword, { isSuccess, isError, isLoading }] =
    useUpdatePasswordMutation();

  useEffect(() => {
    if (isSuccess && !isError) {
      toast(t("changePasswordFormSuccessToast"));
      reset();
    }
  }, [isSuccess, isError]);

  const submit: SubmitHandler<IChangePasswordForm> = (data) => {
    if (isLoading) {
      return;
    }
    if (data.newPassword != data.newPasswordControl) {
      toast(t("changePasswordFormPasswordsNotMatch"));
      return;
    }
    updatePassword(data);
  };

  return (
    <form
      autoComplete="off"
      id={"change-password-form"}
      className={styles.form}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <label className={styles.label} htmlFor={"current-password"}>
        {t("changePasswordFormCurrentPassword")}
        <input
          id={"current-password"}
          type={"password"}
          required
          {...register("currentPassword")}
        />
      </label>

      <label className={styles.label} htmlFor={"new-password"}>
        {t("changePasswordFormNewPassword")}
        <input
          id={"new-password"}
          type={"password"}
          required
          {...register("newPassword")}
        />
      </label>
      <label className={styles.label} htmlFor={"new-password-confirm"}>
        {t("changePasswordFormNewPasswordConfirm")}
        <input
          id={"new-password-confirm"}
          type={"password"}
          required
          {...register("newPasswordControl")}
        />
      </label>
      <Button
        disabled={isLoading}
        loading={isLoading}
        type="submit"
        className={styles.button}
        variant={ButtonVariants.filled}
      >
        <div>{t("changePasswordFormChangeButton")}</div>
      </Button>
    </form>
  );
};

export default ChangePasswordForm;
