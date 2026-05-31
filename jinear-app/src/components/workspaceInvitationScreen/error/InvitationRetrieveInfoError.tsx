import Button, { ButtonVariants } from "@/components/button";
import type { SerializedError } from "@reduxjs/toolkit";
import type {FetchBaseQueryError} from "@reduxjs/toolkit/query";
import useTranslation from "@/locales/useTranslation";
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
