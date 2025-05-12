import Button, { ButtonVariants } from "@/components/button";
import { SerializedError } from "@reduxjs/toolkit";
import { FetchBaseQueryError } from "@reduxjs/toolkit/dist/query";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./InvitationRetrieveInfoError.module.css";

interface InvitationRetrieveInfoErrorProps {
  responseError?: FetchBaseQueryError | SerializedError;
  isError: boolean;
}

const InvitationRetrieveInfoError: React.FC<InvitationRetrieveInfoErrorProps> = ({ responseError, isError }) => {
  const { t } = useTranslation();
  return isError ? (
    <div className={styles.errorContainer}>
      {/* @ts-ignore */}
      <div>{responseError?.data?.consumerErrorMessage || t("genericError")}</div>
      <div className={styles.errorActionContainer}>
        <Button variant={ButtonVariants.filled} href="/">
          {t("engageWorkspaceInvitationGoBackButton")}
        </Button>
      </div>
    </div>
  ) : null;
};

export default InvitationRetrieveInfoError;
