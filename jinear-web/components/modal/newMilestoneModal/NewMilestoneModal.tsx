import React from "react";
import Modal from "@/components/modal/modal/Modal";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import {
  closeNewMilestoneModal,
  selectNewMilestoneModalProject,
  selectNewMilestoneModalVisible
} from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import NewMilestoneForm from "@/components/form/newMilestoneForm/NewMilestoneForm";

interface NewMilestoneModalProps {

}

const NewMilestoneModal: React.FC<NewMilestoneModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewMilestoneModalVisible);
  const project = useTypedSelector(selectNewMilestoneModalProject);

  const close = () => {
    dispatch(closeNewMilestoneModal());
  };

  return (<Modal
      visible={visible}
      title={t("newMilestoneModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {project && <NewMilestoneForm project={project} onClose={close} />}
    </Modal>
  );
};

export default NewMilestoneModal;