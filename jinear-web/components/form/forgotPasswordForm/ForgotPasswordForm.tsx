import Button, { ButtonVariants } from "@/components/button";
import OrLine from "@/components/orLine/OrLine";
import { LocaleType } from "@/model/be/jinear-core";
import { useInitializeResetPasswordMutation } from "@/store/api/accountPasswordApi";
import { changeLoginWith2FaMailModalVisibility } from "@/store/slice/modalSlice";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { useDispatch } from "react-redux";
import FormTitle from "../formTitle/FormTitle";
import styles from "./ForgotPasswordForm.module.css";

interface ForgotPasswordFormProps {
  className?: string;
}

export interface IForgotPasswordForm {
  email: string;
}

const ForgotPasswordForm: React.FC<ForgotPasswordFormProps> = ({ className }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const { register, setValue, handleSubmit } = useForm<IForgotPasswordForm>();
  const dispatch = useDispatch();
  const [initializeResetPassword, { isSuccess, isError, isLoading }] = useInitializeResetPasswordMutation();

  useEffect(() => {
    if (isSuccess && !isError) {
      toast(t("forgotPasswordInitializeSuccess"));
      setTimeout(() => {
        router.replace("/login");
      }, 2500);
    }
  }, [isSuccess, isError]);

  const submit: SubmitHandler<IForgotPasswordForm> = (data) => {
    if (isLoading) {
      return;
    }
    initializeResetPassword({
      email: data.email,
      locale: t("localeType") as LocaleType,
    });
  };

  const pop2FaMailModal = () => {
    dispatch(changeLoginWith2FaMailModalVisibility({ visible: true }));
  };

  return (
    <div className={cn(styles.container, className)}>
      <FormTitle title={t("forgotPasswordFormTitle")} />
      <form autoComplete="off" id={"forgot-password-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
        <label className={styles.label} htmlFor={"reset-password-email"}>
          {t("forgotPasswordEmailLabel")}
          <input id={"reset-password"} type={"email"} {...register("email")} />
        </label>

        <Button
          disabled={isLoading}
          loading={isLoading}
          type="submit"
          className={styles.submitButton}
          variant={ButtonVariants.contrast}
        >
          <div>{t("forgotPasswordFormResetButton")}</div>
        </Button>

        <OrLine />

        <Button onClick={pop2FaMailModal} variant={ButtonVariants.outline}>
          {t("loginWith2FaMail")}
        </Button>
      </form>
    </div>
  );
};

export default ForgotPasswordForm;
