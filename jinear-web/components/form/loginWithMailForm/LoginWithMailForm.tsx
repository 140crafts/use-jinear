import Button, { ButtonVariants } from "@/components/button";
import OrLine from "@/components/orLine/OrLine";
import { useLoginWithPasswordMutation } from "@/store/api/authApi";
import { changeLoginWith2FaMailModalVisibility } from "@/store/slice/modalSlice";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { useDispatch } from "react-redux";
import FormTitle from "../formTitle/FormTitle";
import styles from "./LoginWithMailForm.module.css";

interface LoginWithMailFormProps {
  initialEmail?: string;
  className?: string;
}

export interface ILoginWithMailForm {
  email: string;
  password: string;
}

const logger = Logger("LoginWithMailForm");

const LoginWithMailForm: React.FC<LoginWithMailFormProps> = ({
  className,
  initialEmail,
}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useDispatch();
  const [loginWithpassword, { isSuccess, isError, isLoading }] =
    useLoginWithPasswordMutation();
  const { register, setValue, handleSubmit } = useForm<ILoginWithMailForm>();

  useEffect(() => {
    if (isSuccess && !isError) {
      logger.log("Login with password success");
      toast(t("loginSuccessToast"));
      router.replace("/");
    }
  }, [isSuccess, isError]);

  useEffect(() => {
    if (initialEmail) {
      setValue("email", initialEmail);
    }
  }, [initialEmail]);

  const submit: SubmitHandler<ILoginWithMailForm> = (data) => {
    if (isLoading) {
      return;
    }
    loginWithpassword(data);
  };

  const pop2FaMailModal = () => {
    dispatch(changeLoginWith2FaMailModalVisibility({ visible: true }));
  };

  return (
    <div className={cn(styles.container, className)}>
      <FormTitle
        title={t("loginWithEmailFormTitle")}
        subTitle={t("loginWithEmailFormSubTitle")}
        linkLabel={t("loginWithEmailFormTitleLink")}
        link={"/register"}
      />
      <form
        autoComplete="off"
        id={"login-with-email-form"}
        className={styles.form}
        onSubmit={handleSubmit(submit)}
        action="#"
      >
        <label className={styles.label} htmlFor={"login-with-email-email"}>
          {t("loginWithEmailEmailLabel")}
          <input
            id={"login-with-email-email"}
            type={"email"}
            {...register("email")}
          />
        </label>

        <label className={styles.label} htmlFor={"login-with-email-password"}>
          {t("loginWithEmailPasswordLabel")}
          <input
            id={"login-with-email-password"}
            type={"password"}
            {...register("password")}
          />
        </label>

        <Link className={styles.forgotPasswordLink} href={"/forgot-password"}>
          {t("forgotPasswordLinkLabel")}
        </Link>

        <Button
          disabled={isLoading}
          loading={isLoading}
          type="submit"
          className={styles.submitButton}
          variant={ButtonVariants.contrast}
        >
          <div>{t("loginWithEmailLoginButtonLabel")}</div>
        </Button>
      </form>
      <OrLine />
      <Button onClick={pop2FaMailModal} variant={ButtonVariants.outline}>
        {t("loginWith2FaMail")}
      </Button>
    </div>
  );
};

export default LoginWithMailForm;
