import Button from "@/components/button";
import { LocaleType } from "@/model/be/jinear-core";
import { useCompleteResetPasswordMutation } from "@/store/api/accountPasswordApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface ResetPasswordCompletePageProps {}

const ResetPasswordCompletePage: React.FC<
  ResetPasswordCompletePageProps
> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [completeResetPassword, { isSuccess, isError, isLoading }] =
    useCompleteResetPasswordMutation();

  const token: string = router.query?.token as string;

  useEffect(() => {
    if (token && !isLoading) {
      completeResetPassword({
        uniqueToken: token,
        locale: t("localeType") as LocaleType,
      });
    }
  }, [token]);

  return (
    <div className={styles.container}>
      {isLoading && <CircularProgress size={28} />}
      <div className={styles.infoContainer}>
        {isSuccess && !isError && (
          <>
            <div className={styles.title}>
              {t("engageCompletePasswordResetSuccessTitle")}
            </div>
            <div>{t("engageCompletePasswordResetSuccessText")}</div>
            <div className="spacer-h-4" />
            <div className={styles.actionButtonContainer}>
              <Button href={"/login"}>
                {t("engageCompletePasswordResetLoginPage")}
              </Button>
            </div>
          </>
        )}
        {!isSuccess && isError && (
          <>
            <div className={styles.title}>
              {t("engageCompletePasswordResetErrorTitle")}
            </div>
            <div>{t("engageCompletePasswordResetErrorText")}</div>
            <div className="spacer-h-2" />
            <div className={styles.actionButtonContainer}>
              <Button href={"/forgot-password"}>
                {t("engageCompletePasswordResetForgotPasswordPage")}
              </Button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default ResetPasswordCompletePage;
