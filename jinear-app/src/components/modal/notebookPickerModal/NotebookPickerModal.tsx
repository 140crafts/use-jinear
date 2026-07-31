import React from "react";
import styles from "./NotebookPickerModal.module.css";
import useTranslation from "@/locales/useTranslation";
import {useAppDispatch, useTypedSelector} from "@/store";
import {
    closeNotebookPickerModal,
    selectNotebookPickerModalOnPick,
    selectNotebookPickerModalVisible,
    selectNotebookPickerModalWorkspaceId
} from "@/store/slice/modalSlice";
import useWindowSize from "@/hooks/useWindowSize";
import Modal from "@/components/modal/modal/Modal";
import {useAllWorkspaceNotebooks} from "@/hooks/useAllWorkspaceNotebooks";
import type {NotebookDto} from "@/model/be/jinear-core";
import Button from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import {LuNotebook} from "react-icons/lu";
import cn from "classnames";

interface NotebookPickerModalProps {
}

const NotebookPickerModal: React.FC<NotebookPickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();
    const visible = useTypedSelector(selectNotebookPickerModalVisible);
    const workspaceId = useTypedSelector(selectNotebookPickerModalWorkspaceId);
    const onPick = useTypedSelector(selectNotebookPickerModalOnPick);

    const {notebooks, isLoading} = useAllWorkspaceNotebooks(visible ? workspaceId : undefined);

    const close = () => {
        dispatch(closeNotebookPickerModal());
    };

    const pickAndClose = (notebook: NotebookDto) => {
        onPick?.(notebook);
        close();
    };

    return (
        <Modal
            visible={visible}
            title={t("notebookPickerModalTitle")}
            bodyClass={styles.container}
            // width={isMobile ? "fullscreen" : "medium-fixed"}
            hasTitleCloseButton={true}
            requestClose={close}
            // bottomSheet={isMobile}
        >
            {isLoading && notebooks.length == 0 && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={17}/>
                </div>
            )}
            {notebooks.map((notebook) => (
                <Button
                    key={notebook.notebookId}
                    className={styles.button}
                    onClick={() => pickAndClose(notebook)}
                >
                    <LuNotebook size={16} className={styles.icon}/>
                    <span className={cn(styles.name, "line-clamp")}>{notebook.title}</span>
                </Button>
            ))}
            {!isLoading && notebooks.length == 0 && (
                <div className={styles.emptyContainer}>
                    {t("notebookPickerModalEmpty")}
                </div>
            )}
        </Modal>
    );
};

export default NotebookPickerModal;
