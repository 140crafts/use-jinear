import React from "react";
import styles from './ProjectPrioritySelectModal.module.css';
import {
  closeProjectPrioritySelectModal,
  selectProjectPrioritySelectModalOnPick,
  selectProjectPrioritySelectModalVisible
} from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import GenericSelectModal, {
  IGenericPickerModalElement
} from "@/components/modal/genericSelectModal/GenericSelectModal";
import useTranslation from "@/locals/useTranslation";
import { ProjectPriorityType } from "@/be/jinear-core";
import ProjectPriorityIcon from "@/components/projectPriorityIcon/ProjectPriorityIcon";

interface ProjectPrioritySelectModalProps {

}

const PRIORITIES: ProjectPriorityType[] = ["URGENT", "HIGH", "MEDIUM", "LOW"];

const ProjectPrioritySelectModal: React.FC<ProjectPrioritySelectModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectProjectPrioritySelectModalVisible);
  const onPick = useTypedSelector(selectProjectPrioritySelectModalOnPick);

  const close = () => {
    dispatch(closeProjectPrioritySelectModal());
  };

  const onGenericModalPick = (picked: IGenericPickerModalElement[]) => {
    if (picked && picked?.[0]) {
      onPick?.(picked[0].data);
    }
    close();
  };

  return (
    <GenericSelectModal
      visible={visible}
      title={t("projectPrioritySelectModalTitle")}
      multiple={false}
      modalData={PRIORITIES.map(p => {
        return {
          id: p,
          label: t(`projectPriority_${p}`),
          data: p,
          Icon: <ProjectPriorityIcon projectPriority={p} size={18} className={styles.icon} />
        };
      })}
      onPick={onGenericModalPick}
      searchable={false}
      requestClose={close}
    />
  );
};

export default ProjectPrioritySelectModal;