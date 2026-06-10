"use client";
import Button from "@/components/button";
import { useFirstRender } from "@/hooks/useFirstRender";
import { useConfirmAccountDeleteMutation } from "@/store/api/accountDeleteApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useSearchParams } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface DeleteAccountCompletePageProps {}

const DeleteAccountCompletePage: React.FC<DeleteAccountCompletePageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useSearchParams();
  const firstRender = useFirstRender();
  const [confirmAccountDelete, { data, isSuccess, isError, isLoading }] = useConfirmAccountDeleteMutation();

  const token: string = params?.get("token") as string;

  useEffect(() => {
    if (token && !isLoading && !data) {
      confirmAccountDelete(token);
    }
  }, [token, firstRender]);

  return (
    <div className={styles.container}>
      {isLoading && <CircularProgress size={28} />}
      <div className={styles.infoContainer}>
        {isSuccess && !isError && (
          <>
            <div className={styles.title}>{t("accountDeleteConfirmPageSuccessTitle")}</div>
          </>
        )}
        {!isSuccess && isError && (
          <>
            <div className={styles.title}>{t("accountDeleteConfirmPageFailureTitle")}</div>
            <div>{t("accountDeleteConfirmPageFailureText")}</div>
            <Button href="/home">{t("notFoundModalReturnHomeButtonLabel")}</Button>
          </>
        )}
      </div>
    </div>
  );
};

export default DeleteAccountCompletePage;
