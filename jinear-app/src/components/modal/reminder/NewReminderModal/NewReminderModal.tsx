import {
    closeNewReminderModal,
    selectNewReminderModalDraft,
    selectNewReminderModalOnDraftSave,
    selectNewReminderModalTask,
    selectNewReminderModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import Modal from "../../modal/Modal";
import NewReminderForm from "./newReminderForm/NewReminderForm";
import styles from "./NewReminderModal.module.css";

interface NewReminderModalProps {
}

const NewReminderModal: React.FC<NewReminderModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectNewReminderModalVisible);
    const task = useTypedSelector(selectNewReminderModalTask);
    const draft = useTypedSelector(selectNewReminderModalDraft);
    const onDraftSave = useTypedSelector(selectNewReminderModalOnDraftSave);

    const close = () => {
        dispatch(closeNewReminderModal());
    };

    return (
        <Modal
            visible={visible}
            title={t("taskNewReminderModalTitle")}
            bodyClass={styles.container}
            hasTitleCloseButton={true}
            requestClose={close}
        >
            <NewReminderForm task={task} draft={draft} onDraftSave={onDraftSave} close={close}/>
        </Modal>
    );
};

export default NewReminderModal;
