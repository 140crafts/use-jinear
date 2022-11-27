import { selectLoadingModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./LoadingModal.module.css";

interface LoadingModalProps {}

const LoadingModal: React.FC<LoadingModalProps> = ({}) => {
  const visibile = useTypedSelector(selectLoadingModalVisible);

  return (
    <Modal visible={visibile} contentContainerClass={styles.container}>
      <div className={styles.circularProgressContainer}>
        <CircularProgress className={styles.circularProgress} size={24} />
      </div>
    </Modal>
  );
};

export default LoadingModal;
