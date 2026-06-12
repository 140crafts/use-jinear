import React, { useEffect, useState } from "react";
import styles from "./MaterialFolderPickerModal.module.css";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store";
import {
  closeMaterialFolderPickerModal,
  selectMaterialFolderPickerModalOnPick, selectMaterialFolderPickerModalTitle,
  selectMaterialFolderPickerModalVisible,
  selectMaterialFolderPickerModalWorkspaceId
} from "@/store/slice/modalSlice";
import useWindowSize from "@/hooks/useWindowSize";
import Modal from "@/components/modal/modal/Modal";
import { materialListingApi, useSearchMaterialQuery } from "@/api/materialListingApi";
import type { MaterialDto } from "@/model/be/jinear-core";
import Breadcrumb from "@/components/workspaceFilesPage/folderHeader/breadcrumb/Breadcrumb";
import Button, { ButtonVariants } from "@/components/button";
import GradientLine from "@/components/gradientLine/GradientLine";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading";
import { LuFolder } from "react-icons/lu";
import cn from "classnames";
import { getDeviceProfile } from "@/util/deviceProfile";

interface MaterialMoveModalProps {

}

const MaterialFolderPickerModal: React.FC<MaterialMoveModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectMaterialFolderPickerModalVisible);
  const workspaceId = useTypedSelector(selectMaterialFolderPickerModalWorkspaceId) ?? "";
  const onPick = useTypedSelector(selectMaterialFolderPickerModalOnPick);
  const title = useTypedSelector(selectMaterialFolderPickerModalTitle);

  const { isMobile } = useWindowSize();

  const [currentParentId, setCurrentParentId] = useState<string>();
  const [page, setPage] = useState<number>(0);
  const [materialList, setMaterialList] = useState<MaterialDto[]>([]);

  const prefetchSearchMaterial = materialListingApi.usePrefetch("searchMaterial");
  const { data: paginatedMaterialSearchResponse, isLoading, isFetching } = useSearchMaterialQuery({
    workspaceId,
    page,
    parentMaterialId: currentParentId,
    materialType: "FOLDER"
  }, { skip: workspaceId == "" });

  const container = paginatedMaterialSearchResponse?.data.container;

  useEffect(() => {
    if (isFetching) return;
    const children = paginatedMaterialSearchResponse?.data?.content;
    if (children?.hasContent && workspaceId != "") {
      setMaterialList(current => [...current, ...children.content]);
      children.hasNext && setPage(page => page + 1);

      const {tier, saveData, slowNetwork} = getDeviceProfile();
      if (tier === "low" || saveData || slowNetwork) {
        return;
      }

      const MAX_PREFETCH = 10;
      const DELAY_MS = 150;

      const folders = children.content
        .filter(material => material.materialType === "FOLDER")
        .slice(0, MAX_PREFETCH);

      const timeoutIds = folders.map((folder, index) =>
        setTimeout(() => {
          prefetchSearchMaterial({
            workspaceId,
            materialType: "FOLDER",
            parentMaterialId: folder.materialId,
            page: 0
          });
        }, index * DELAY_MS)
      );

      return () => {
        timeoutIds.forEach(clearTimeout);
      };
    }
  }, [paginatedMaterialSearchResponse, isFetching, visible]);

  const onBreadcrumbClick = (materialId?: string) => {
    materialSelect(materialId);
  };

  const materialSelect = (materialId?: string) => {
    resetList();
    setCurrentParentId(materialId);
  };

  const resetList = () => {
    setMaterialList([]);
  };

  const close = () => {
    resetList();
    dispatch(closeMaterialFolderPickerModal());
    setCurrentParentId(undefined);
    setPage(0);
  };

  const pickAndClose = () => {
    onPick?.(currentParentId);
    close?.();
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={title ?? t("materialFolderPickerModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.contentContainer}
    >
      <div className={styles.breadcrumbContainer}>
        <Breadcrumb container={container} onClick={onBreadcrumbClick} />
        <GradientLine />
        {isFetching && <InfiniteLineLoading />}
      </div>

      <div className={styles.foldersContainer}>
        {materialList?.map((material: MaterialDto) =>
          <Button
            key={material.materialId}
            className={styles.button}
            onClick={() => materialSelect(material.materialId)}
          >
            <LuFolder size={16} className={styles.icon} />
            <span className={cn(styles.name, "line-clamp")}>
              {material.name}
            </span>
          </Button>)}

        {materialList?.length == 0 && !paginatedMaterialSearchResponse?.data?.content?.hasContent && !isFetching &&
          <div className={styles.emptyContainer}>
            {t("materialFolderPickerModalFolderEmpty")}
          </div>
        }
      </div>

      <div className={styles.footerContainer}>
        <GradientLine />
        <div className={"spacer-h-1"} />
        <Button
          variant={ButtonVariants.contrast}
          onClick={pickAndClose}
        >
          {t("materialFolderPickerModalSelect")}
        </Button>
        <div className={"spacer-h-1"} />
        <Button onClick={close}>
          {t("materialFolderPickerModalCancel")}
        </Button>
      </div>
    </Modal>
  );
};

export default MaterialFolderPickerModal;