import {
  closeChangeTaskTopicModal,
  selectChangeTaskTopicModalTeamId,
  selectChangeTaskTopicModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import Modal from "../../modal/Modal";
import styles from "./TaskTopicChangeModal.module.css";
import RemoveCurrentTopic from "./removeCurrentTopic/RemoveCurrentTopic";
import TopicList from "./topicList/TopicList";

interface TaskTopicChangeModalProps {}

const TaskTopicChangeModal: React.FC<TaskTopicChangeModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectChangeTaskTopicModalVisible);
  const teamId = useTypedSelector(selectChangeTaskTopicModalTeamId);
  const filterInputRef = useRef<HTMLInputElement>(null);
  const [filterValue, setFilterValue] = useState<string>("");

  useEffect(() => {
    if (visible) {
      setTimeout(() => {
        filterInputRef.current?.focus();
      }, 100);
    }
  }, [visible]);

  const onFilterValue = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setFilterValue?.(value);
  };

  const close = () => {
    dispatch(closeChangeTaskTopicModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("changeTaskTopicModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <label className={styles.label} htmlFor={"topic-filter"}>
        {t("changeTaskTopicModalFilterLabel")}
        <input ref={filterInputRef} id={"topic-filter"} type={"text"} onChange={onFilterValue} />
      </label>
      {teamId && <TopicList teamId={teamId} filter={filterValue} close={close} />}
      <RemoveCurrentTopic close={close} />
    </Modal>
  );
};

export default TaskTopicChangeModal;
