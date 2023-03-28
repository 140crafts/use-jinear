import Button, { ButtonVariants } from "@/components/button";
import { LocaleType } from "@/model/be/jinear-core";
import { useConfirmEmailMutation, useResendConfirmEmailMutation } from "@/store/api/accountApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface ConfirmEmailPageProps {}

const logger = Logger("ConfirmEmailPage");
const ConfirmEmailPage: React.FC<ConfirmEmailPageProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [confirmEmail, { data, isSuccess, isError, isLoading }] = useConfirmEmailMutation();
  const [resendConfirmEmail, { error: resendErrorResponse, isLoading: resendLoading, isError: isResendError }] =
    useResendConfirmEmailMutation();
  const token: string = router.query?.token as string;

  useEffect(() => {
    if (token && !isLoading && !data) {
      logger.log({ token, isLoading });
      confirmEmail({
        uniqueToken: token,
      });
    }
  }, [token, data]);

  useEffect(() => {
    logger.log({ resendErrorResponse });
    if (
      resendErrorResponse &&
      // @ts-ignore
      resendErrorResponse?.data?.errorCode == "16002"
    ) {
      router.replace("/");
    }
  }, [resendErrorResponse, isResendError]);

  const resend = () => {
    resendConfirmEmail({
      token,
      locale: t("localeType") as LocaleType,
    });
  };

  return (
    <div className={styles.container}>
      {isLoading && <CircularProgress size={28} />}
      <div className={styles.infoContainer}>
        {isSuccess && !isError && (
          <>
            <div className={styles.title}>{t("engageConfirmEmailTitleSuccess")}</div>
            <div className="spacer-h-4" />
            <div className={styles.actionButtonContainer}>
              <Button href={"/"} variant={ButtonVariants.filled}>
                {t("engageConfirmEmailContinueHomeButton")}
              </Button>
            </div>
          </>
        )}
        {!isSuccess && isError && (
          <>
            <div className={styles.title}>{t("engageConfirmEmailTitleError")}</div>
            <div>{t("engageConfirmEmailTextError")}</div>
            <div className="spacer-h-2" />
            <div className={styles.actionButtonContainer}>
              <Button loading={resendLoading} disabled={resendLoading} onClick={resend} variant={ButtonVariants.filled}>
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
