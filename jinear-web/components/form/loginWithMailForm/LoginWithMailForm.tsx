"use client";
import Button, { ButtonVariants } from "@/components/button";
import OrLine from "@/components/orLine/OrLine";
import { LocaleType } from "@/model/be/jinear-core";
import { useLoginWithPasswordMutation } from "@/store/api/authApi";
import { useRetrieveLoginRedirectInfoQuery } from "@/store/api/googleOAuthApi";
import { changeLoginWith2FaMailModalVisibility } from "@/store/slice/modalSlice";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import Logger from "@/utils/logger";
import { isWebView } from "@/utils/webviewUtils";
import cn from "classnames";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { IoLogoGoogle, IoMail } from "react-icons/io5";
import { useDispatch } from "react-redux";
import FormTitle from "../formTitle/FormTitle";
import styles from "./LoginWithMailForm.module.css";
import { setAuthStateAsNotDecided } from "@/slice/accountSlice";

interface LoginWithMailFormProps {
  initialEmail?: string;
  className?: string;
}

export interface ILoginWithMailForm {
  email: string;
  password: string;
}

const logger = Logger("LoginWithMailForm");

const LoginWithMailForm: React.FC<LoginWithMailFormProps> = ({ className, initialEmail }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useDispatch();
  const _isWebView = isWebView();
  const [loginWithpassword, { isSuccess, isError, isLoading }] = useLoginWithPasswordMutation();
  const {
    data: authRedirectInfoResponse,
    isLoading: isAuthRedirectRetrieveLoading
  } = useRetrieveLoginRedirectInfoQuery();
  const { register, setValue, handleSubmit, setFocus } = useForm<ILoginWithMailForm>();

  useEffect(() => {
    if (isSuccess && !isError) {
      logger.log("Login with password success");
      toast(t("loginSuccessToast"));
      router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [isSuccess, isError]);

  useEffect(() => {
    if (initialEmail) {
      setValue("email", initialEmail);
      setFocus("password");
    }
  }, [initialEmail]);

  const submit: SubmitHandler<ILoginWithMailForm> = (data) => {
    dispatch(setAuthStateAsNotDecided());
    if (isLoading) {
      return;
    }
    loginWithpassword({
      ...data,
      locale: t("localeType") as LocaleType,
      timeZone: format(new Date(), "OOOO"),
      domain: window.location.host
    });
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

      <form autoComplete="off" id={"login-with-email-form"} className={styles.form} onSubmit={handleSubmit(submit)}
            action="#">
        <label className={styles.label} htmlFor={"login-with-email-email"}>
          {t("loginWithEmailEmailLabel")}
          <input id={"login-with-email-email"} type={"email"} {...register("email")} />
        </label>

        <label className={styles.label} htmlFor={"login-with-email-password"}>
          {t("loginWithEmailPasswordLabel")}
          <input id={"login-with-email-password"} type={"password"} {...register("password")} />
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

      <div className="spacer-h-1" />
      <OrLine />
      <div className="spacer-h-1" />

      <div className={styles.otherMethodsContainer}>
        {!_isWebView && (
          <Button
            disabled={isLoading}
            href={authRedirectInfoResponse?.redirectUrl}
            variant={ButtonVariants.outline}
            className={styles.iconButton}
          >
            <IoLogoGoogle className={styles.icon} />
            <div>{t("loginScreenLoginWithGoogle")}</div>
          </Button>
        )}
        <Button onClick={pop2FaMailModal} variant={ButtonVariants.outline} className={styles.iconButton}>
          <IoMail className={styles.icon} />
          <div>{t("loginWith2FaMail")}</div>
        </Button>
      </div>
    </div>
  );
};

export default LoginWithMailForm;
