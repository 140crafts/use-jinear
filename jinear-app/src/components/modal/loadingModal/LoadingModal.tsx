import {selectLoadingModalVisible} from "@/store/slice/modalSlice";
import {useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./LoadingModal.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface LoadingModalProps {
}

const logger = Logger("LoadingModal");

const LoadingModal: React.FC<LoadingModalProps> = ({}) => {
    const visible = useTypedSelector(selectLoadingModalVisible);

    return (
        <Modal visible={visible} contentContainerClass={styles.container}>
            <div className={styles.circularProgressContainer}>
                <CircularLoading progressClassName={styles.circularProgress} size={24}/>
            </div>
        </Modal>
    );
};

export default LoadingModal;
