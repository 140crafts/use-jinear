import React from "react";
import styles from "./MaterialAccessModal.module.css";
import Modal from "@/components/modal/modal/Modal";
import useWindowSize from "@/hooks/useWindowSize";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import {
  closeMaterialAccessModal,
  selectMaterialAccessModalMaterialId, selectMaterialAccessModalResetList,
  selectMaterialAccessModalVisible
} from "@/slice/modalSlice";
import MaterialAccessUpdate from "@/components/modal/materialAccessModal/materialAccessUpdate/MaterialAccessUpdate";
import { useRetrieveMaterialQuery } from "@/api/materialApi";

interface MaterialAccessModalProps {

}

const MaterialAccessModal: React.FC<MaterialAccessModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectMaterialAccessModalVisible);
  const materialId = useTypedSelector(selectMaterialAccessModalMaterialId);
  const { data: retrieveMaterialResponse, isLoading:isRetrieveMaterialLoading } = useRetrieveMaterialQuery({ materialId: materialId ?? "" }, { skip: !materialId });
  const material = retrieveMaterialResponse?.data;

  const close = () => {
    dispatch(closeMaterialAccessModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "xlarge"}
      title={t("materialAccessChangeModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.contentContainer}
    >
      {material && <MaterialAccessUpdate material={material} />}
    </Modal>
  );
};

export default MaterialAccessModal;