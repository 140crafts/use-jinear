import React from 'react';
import cn from "classnames";
import styles from './NoteHistoryModal.module.css';
import Modal from "@/components/modal/modal/Modal";
import useTranslation from "@/locals/useTranslation.ts";
import useWindowSize from "@/hooks/useWindowSize";
import {useAppDispatch, useTypedSelector} from "@/store";
import {
    closeNoteHistoryModal,
    selectNoteHistoryModalRichTextId,
    selectNoteHistoryModalVisible
} from "@/slice/modalSlice.ts";
import NoteHistoryContent from "./noteHistoryContent/NoteHistoryContent.tsx";

const NoteHistoryModal: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {isMobile} = useWindowSize();
    const visible = useTypedSelector(selectNoteHistoryModalVisible);
    const richTextId = useTypedSelector(selectNoteHistoryModalRichTextId);

    const close = () => {
        dispatch(closeNoteHistoryModal());
    };

    return (
        <Modal
            visible={visible}
            width={isMobile ? "fullscreen" : "page-extended"}
            title={t("noteHistoryModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            contentContainerClass={cn(styles.modalContainer, isMobile && styles.mobileContainer)}
            bodyClass={styles.modalBody}
        >
            {visible && richTextId && <NoteHistoryContent key={richTextId} richTextId={richTextId} close={close}/>}
        </Modal>
    );
};

export default NoteHistoryModal;
