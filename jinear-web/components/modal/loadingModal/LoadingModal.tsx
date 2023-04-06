import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { selectLoadingModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import { __DEV__ } from "@/utils/constants";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import Image from "next/image";
import React, { useMemo } from "react";
import Modal from "../modal/Modal";
import styles from "./LoadingModal.module.css";

interface LoadingModalProps {}

const GIF_LOADING_ACCOUNT_IDS = [
  "01gve1623eberp86fc6ec189jm", //pelom
  "01gvrf1yn6xp9p1q3yar7pdkc6", //osti
  "01gp94s0sk9q4g8g3m9jpsvd0t", //irgat
];

const logger = Logger("LoadingModal");

const LoadingModal: React.FC<LoadingModalProps> = ({}) => {
  const visibile = useTypedSelector(selectLoadingModalVisible);

  const currentAccountId = useTypedSelector(selectCurrentAccountId);

  const gifType = Math.random() * 10 < 5 ? "1" : "2";
  const gifLoading = useMemo(() => {
    logger.log(GIF_LOADING_ACCOUNT_IDS.indexOf(currentAccountId || ""));
    return __DEV__ || GIF_LOADING_ACCOUNT_IDS.indexOf(currentAccountId || "") == -1;
  }, [currentAccountId]);

  return (
    <Modal visible={visibile} contentContainerClass={styles.container}>
      <div className={styles.circularProgressContainer}>
        {gifLoading ? (
          <Image src={`/images/gif/loading-${gifType}.gif`} alt="loading-gif" width={150} height={150} className={styles.image} />
        ) : (
          <CircularProgress className={styles.circularProgress} size={24} />
        )}
      </div>
    </Modal>
  );
};

export default LoadingModal;
