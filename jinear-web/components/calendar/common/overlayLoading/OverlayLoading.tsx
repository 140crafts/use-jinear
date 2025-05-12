import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./OverlayLoading.module.css";

interface OverlayLoadingProps {
  isFetching: boolean;
}

const OverlayLoading: React.FC<OverlayLoadingProps> = ({ isFetching }) => {
  const { t } = useTranslation();
  return isFetching ? (
    <div className={styles.loadingContainer}>
      <CircularProgress size={14} />
      <span>{t("calendarSyncing")}</span>
    </div>
  ) : null;
};

export default OverlayLoading;
