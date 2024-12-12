import { CircularProgress } from "@mui/material";
import React from "react";
import styles from "./LoadingBar.module.css";

interface LoadingBarProps {
  isFetching: boolean;
}

const LoadingBar: React.FC<LoadingBarProps> = ({ isFetching }) => {
  return isFetching ? (
    <div className={styles.loadingContainer}>
      <CircularProgress size={18} />
    </div>
  ) : null;
};

export default LoadingBar;
