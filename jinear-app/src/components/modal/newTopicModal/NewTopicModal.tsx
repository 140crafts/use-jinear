import TopicForm from "@/components/form/topicForm/TopicForm";
import {
    closeNewTopicModal,
    selectNewTopicModalInitialName,
    selectNewTopicModalTeam,
    selectNewTopicModalVisible,
    selectNewTopicModalWorkspace,
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTopicModal.module.css";

interface NewTopicModalProps {
}

const NewTopicModal: React.FC<NewTopicModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectNewTopicModalVisible);
    const workspace = useTypedSelector(selectNewTopicModalWorkspace);
    const team = useTypedSelector(selectNewTopicModalTeam);
    const initialName = useTypedSelector(selectNewTopicModalInitialName);

    const close = () => {
        dispatch(closeNewTopicModal());
    };

    return (
        <Modal
            visible={visible}
            title={t("newTopicModalTitle")}
            bodyClass={styles.container}
            hasTitleCloseButton={true}
            requestClose={close}
        >
            {workspace && team && <TopicForm workspace={workspace} team={team} taskName={initialName} onSuccess={close}/>}
        </Modal>
    );
};

export default NewTopicModal;
