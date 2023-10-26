"use client";
import Button from "@/components/button";
import { useFirstRender } from "@/hooks/useFirstRender";
import { LocaleType } from "@/model/be/jinear-core";
import { useCompleteResetPasswordMutation } from "@/store/api/accountPasswordApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useSearchParams } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface ResetPasswordCompletePageProps {}

const ResetPasswordCompletePage: React.FC<ResetPasswordCompletePageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useSearchParams();
  const firstRender = useFirstRender();
  const [completeResetPassword, { data, isSuccess, isError, isLoading }] = useCompleteResetPasswordMutation();

  const token: string = params?.get("token") as string;

  useEffect(() => {
    if (token && !isLoading && !data) {
      completeResetPassword({
        uniqueToken: token,
        locale: t("localeType") as LocaleType,
      });
    }
  }, [token, firstRender]);

  return (
    <div className={styles.container}>
      {isLoading && <CircularProgress size={28} />}
      <div className={styles.infoContainer}>
        {isSuccess && !isError && (
          <>
            <div className={styles.title}>{t("engageCompletePasswordResetSuccessTitle")}</div>
            <div>{t("engageCompletePasswordResetSuccessText")}</div>
            <div className="spacer-h-4" />
            <div className={styles.actionButtonContainer}>
              <Button href={"/login"}>{t("engageCompletePasswordResetLoginPage")}</Button>
            </div>
          </>
        )}
        {!isSuccess && isError && (
          <>
            <div className={styles.title}>{t("engageCompletePasswordResetErrorTitle")}</div>
            <div>{t("engageCompletePasswordResetErrorText")}</div>
            <div className="spacer-h-2" />
            <div className={styles.actionButtonContainer}>
              <Button href={"/forgot-password"}>{t("engageCompletePasswordResetForgotPasswordPage")}</Button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default ResetPasswordCompletePage;
