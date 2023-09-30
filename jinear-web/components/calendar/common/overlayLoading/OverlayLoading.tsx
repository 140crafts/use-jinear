import { CircularProgress } from "@mui/material";
import React from "react";
import styles from "./OverlayLoading.module.css";

interface OverlayLoadingProps {
  isFetching: boolean;
}

const OverlayLoading: React.FC<OverlayLoadingProps> = ({ isFetching }) => {
  return isFetching ? (
    <div className={styles.loadingContainer}>
      <CircularProgress />
    </div>
  ) : null;
};

export default OverlayLoading;
