import Button from "@/components/button";
import { BaseRequest } from "@/model/be/jinear-core";
import {
  useConfirmEmailMutation,
  useResendConfirmEmailMutation,
} from "@/store/api/accountApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface ConfirmEmailPageProps {}

const ConfirmEmailPage: React.FC<ConfirmEmailPageProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [confirmEmail, { isSuccess, isError, isLoading }] =
    useConfirmEmailMutation();
  const [resendConfirmEmail, { isLoading: resendLoading }] =
    useResendConfirmEmailMutation();
  const token: string = router.query?.token as string;

  useEffect(() => {
    if (token && !isLoading) {
      confirmEmail({
        uniqueToken: token,
      });
    }
  }, [token]);

  const resend = () => {
    resendConfirmEmail({ locale: t("localeType") } as BaseRequest);
  };

  return (
    <div className={styles.container}>
      {isLoading && <CircularProgress size={28} />}
      <div className={styles.infoContainer}>
        {isSuccess && !isError && (
          <>
            <div className={styles.title}>
              {t("engageConfirmEmailTitleSuccess")}
            </div>
            <div className="spacer-h-4" />
            <div className={styles.actionButtonContainer}>
              <Button href={"/"}>
                {t("engageConfirmEmailContinueHomeButton")}
              </Button>
            </div>
          </>
        )}
        {!isSuccess && isError && (
          <>
            <div className={styles.title}>
              {t("engageConfirmEmailTitleError")}
            </div>
            <div>{t("engageConfirmEmailTextError")}</div>
            <div className="spacer-h-2" />
            <div className={styles.actionButtonContainer}>
              <Button
                loading={resendLoading}
                disabled={resendLoading}
                onClick={resend}
              >
                {t("engageConfirmEmailRequestNewMail")}
              </Button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default ConfirmEmailPage;
