import {
  closeChangeTaskAssigneeModal,
  selectChangeTaskAssigneeModalTaskCurrentTeamId,
  selectChangeTaskAssigneeModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import Modal from "../../modal/Modal";
import RemoveCurrentAssignee from "./removeCurrentAssignee/RemoveCurrentAssignee";
import styles from "./TaskAssigneeChangeModal.module.css";
import TeamMemberList from "./teamMemberList/TeamMemberList";

interface TaskAssigneeChangeModalProps {}

const TaskAssigneeChangeModal: React.FC<TaskAssigneeChangeModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectChangeTaskAssigneeModalVisible);
  const teamId = useTypedSelector(selectChangeTaskAssigneeModalTaskCurrentTeamId);
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
    dispatch(closeChangeTaskAssigneeModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("changeTaskAssigneeModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <label className={styles.label} htmlFor={"assignee-filter"}>
        {t("changeTaskAssigneeModalFilterLabel")}
        <input ref={filterInputRef} id={"assignee-filter"} type={"text"} onChange={onFilterValue} />
      </label>

      {teamId && <TeamMemberList teamId={teamId} filter={filterValue} close={close} />}
      <RemoveCurrentAssignee close={close} />
    </Modal>
  );
};

export default TaskAssigneeChangeModal;
