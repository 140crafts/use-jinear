import React from "react";
import styles from "./ProjectStateSelectModal.module.css";
import {
  closeProjectStateSelectModal,
  selectProjectStateSelectModalOnPick,
  selectProjectStateSelectModalVisible
} from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import GenericSelectModal, {
  IGenericPickerModalElement
} from "@/components/modal/genericSelectModal/GenericSelectModal";
import { ProjectStateType } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import ProjectStateIcon from "@/components/projectStateIcon/ProjectStateIcon";

interface ProjectStateSelectModalProps {

}

const STATES: ProjectStateType[] = ["BACKLOG", "PLANNED", "IN_PROGRESS", "COMPLETED", "CANCELLED"];

const ProjectStateSelectModal: React.FC<ProjectStateSelectModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const visible = useTypedSelector(selectProjectStateSelectModalVisible);
  const onPick = useTypedSelector(selectProjectStateSelectModalOnPick);

  const onGenericModalPick = (picked: IGenericPickerModalElement[]) => {
    if (picked && picked?.[0]) {
      onPick?.(picked[0].data);
    }
    close();
  };

  const close = () => {
    dispatch(closeProjectStateSelectModal());
  };

  return (
    <GenericSelectModal
      visible={visible}
      title={t("projectStateSelectModalTitle")}
      multiple={false}
      modalData={STATES.map(s => {
        return {
          id: s,
          label: t(`projectState_${s}`),
          data: s,
          Icon: <ProjectStateIcon projectState={s} size={18} className={styles.icon} />
        };
      })}
      onPick={onGenericModalPick}
      searchable={false}
      requestClose={close}
    />
  );
};

export default ProjectStateSelectModal;