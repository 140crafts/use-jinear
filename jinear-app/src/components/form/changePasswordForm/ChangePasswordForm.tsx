import Button, { ButtonVariants } from "@/components/button";
import { useUpdatePasswordMutation } from "@/store/api/accountPasswordApi";
import Logger from "@/util/logger";
import useTranslation from "@/locales/useTranslation";
import React, { useEffect } from "react";
import { type SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import styles from "./ChangePasswordForm.module.css";

interface ChangePasswordFormProps {
  forced?: boolean;
  onSuccess?: () => void;
}

export interface IChangePasswordForm {
  currentPassword?: string;
  newPassword: string;
  newPasswordControl: string;
}

const logger = Logger("ChangePasswordForm");

const MIN_PASSWORD_LENGTH = 6;

const ChangePasswordForm: React.FC<ChangePasswordFormProps> = ({ forced = false, onSuccess }) => {
  const { t } = useTranslation();
  const {
    register,
    reset,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<IChangePasswordForm>();
  const [updatePassword, { isSuccess, isError, isLoading }] = useUpdatePasswordMutation();

  const newPassword = watch("newPassword") ?? "";
  const newPasswordControl = watch("newPasswordControl");
  const tooShort = newPassword.length < MIN_PASSWORD_LENGTH;
  const passwordsDoesNotMatch = newPassword.length > 0 && newPasswordControl.length > 0 && newPassword != newPasswordControl;

  useEffect(() => {
    if (isSuccess && !isError) {
      toast(t("changePasswordFormSuccessToast"));
      reset();
      onSuccess?.();
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
    <form autoComplete="off" id={"change-password-form"} className={styles.form} onSubmit={handleSubmit(submit)}
          action="#">
      {!forced &&
        <label className={styles.label} htmlFor={"current-password"}>
          {t("changePasswordFormCurrentPassword")}
          <input id={"current-password"} type={"password"} required {...register("currentPassword")} />
        </label>
      }

      <label className={styles.label} htmlFor={"new-password"}>
        {t("changePasswordFormNewPassword")}
        <input id={"new-password"} type={"password"} required {...register("newPassword")} />
        <span className={styles.warningText}>
            {tooShort &&
              t("changePasswordFormNewPasswordTooShort")
                .replace("${max_char}", `${MIN_PASSWORD_LENGTH}`)
                .replace("${needs_char}", `${MIN_PASSWORD_LENGTH - newPassword.length}`)
            }
        </span>
      </label>

      <label className={styles.label} htmlFor={"new-password-confirm"}>
        {t("changePasswordFormNewPasswordConfirm")}
        <input id={"new-password-confirm"} type={"password"} required {...register("newPasswordControl")} />
        <span className={styles.warningText}>
          {passwordsDoesNotMatch && t("changePasswordFormPasswordsNotMatch")}
        </span>
      </label>
      <Button
        disabled={isLoading || passwordsDoesNotMatch || tooShort}
        loading={isLoading}
        type="submit"
        className={styles.button}
        variant={ButtonVariants.contrast}>
        <div>{t("changePasswordFormChangeButton")}</div>
      </Button>
    </form>
  );
};

export default ChangePasswordForm;
