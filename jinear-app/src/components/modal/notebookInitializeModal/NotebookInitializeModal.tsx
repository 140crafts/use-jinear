import NotebookInitializeForm from "@/components/form/notebookInitializeForm/NotebookInitializeForm";
import useWindowSize from "@/hooks/useWindowSize";
import {
    closeNotebookInitializeModal,
    selectNotebookInitializeModalVisible,
    selectNotebookInitializeModalWorkspace
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NotebookInitializeModal.module.css";

interface NotebookInitializeModalProps {
}

const NotebookInitializeModal: React.FC<NotebookInitializeModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();
    const visible = useTypedSelector(selectNotebookInitializeModalVisible);
    const workspace = useTypedSelector(selectNotebookInitializeModalWorkspace);

    const close = () => {
        dispatch(closeNotebookInitializeModal());
    };
    return (
        <Modal
            visible={visible}
            title={t("notebookInitializeModalTitle")}
            bodyClass={styles.container}
            width={isMobile ? "fullscreen" : "large"}
            hasTitleCloseButton={true}
            requestClose={close}
        >
            {workspace && <NotebookInitializeForm close={close} workspace={workspace}/>}
        </Modal>
    );
};

export default NotebookInitializeModal;
