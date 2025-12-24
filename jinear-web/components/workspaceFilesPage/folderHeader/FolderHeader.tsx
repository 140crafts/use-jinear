import React, { ChangeEvent, useRef } from "react";
import styles from "./FolderHeader.module.css";
import { MaterialSearchContentFilterType, MaterialSearchSortType, PathAwareMaterialDto } from "@/be/jinear-core";
import Breadcrumb from "@/components/workspaceFilesPage/folderHeader/breadcrumb/Breadcrumb";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuCamera, LuFileText, LuFolder, LuFolderPlus, LuHaze, LuHome as LuHouse, LuPlus, LuSendToBack } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import cn from "classnames";
import { useQueryState, useSetQueryStateMultiple } from "@/hooks/useQueryState";
import { useAppDispatch } from "@/store/store";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/slice/modalSlice";
import { useInitializeFolderMutation, useMoveToFolderMutation } from "@/api/materialOperationApi";

import {
  useCdFolder,
  useIsInDocuments,
  useIsInImages,
  useIsInRecent,
  useIsInRoot,
  useIsInShared,
  useResetList,
  useSelectedMaterial,
  useSetDragOverMaterialId
} from "@/components/workspaceFilesPage/context/MaterialViewContext";
import Logger from "@/utils/logger";
import strings from "@/locals/strings";

interface FolderHeaderProps {
  container?: PathAwareMaterialDto;
  workspaceId: string;
  handleUpload: ({ files }: { files: File[] }) => void;
  hideBreadcrumbs?: boolean;
  hideActionBar?: boolean;
}

export const useWorkspaceFilesViewType = () => {
  const defaultViewType = "list";
  const queryViewType = useQueryState<"list" | "grid">("viewType") ?? defaultViewType;
  return ["list", "grid"]?.indexOf(queryViewType.toLowerCase()) == -1 ? defaultViewType : queryViewType;
};

const getIconAndTitle = ({ isInRoot, isInRecent, isInImages, isInDocuments, isInShared }: {
  isInRoot?: boolean, isInRecent?: boolean, isInImages?: boolean, isInDocuments?: boolean, isInShared?: boolean
}) => {
  if (isInRoot) {
    return { Icon: LuHouse, title: "folderHeaderTitleHome" as keyof typeof strings };
  } else if (isInRecent) {
    return { Icon: LuHaze, title: "sideMenuFilesRecentFiles" as keyof typeof strings };
  } else if (isInImages) {
    return { Icon: LuCamera, title: "sideMenuFilesImageFiles" as keyof typeof strings };
  } else if (isInDocuments) {
    return { Icon: LuFileText, title: "sideMenuFilesDocumentFiles" as keyof typeof strings };
  } else if (isInShared) {
    return { Icon: LuSendToBack, title: "sideMenuSharedFiles" as keyof typeof strings };
  }
  return { Icon: LuFolder, title: "folderHeaderTitleDefault" as keyof typeof strings };
};

const logger = Logger("FolderHeader");

const FolderHeader: React.FC<FolderHeaderProps> = ({
                                                     container,
                                                     workspaceId,
                                                     handleUpload,
                                                     hideBreadcrumbs,
                                                     hideActionBar
                                                   }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const setQueryStateMultiple = useSetQueryStateMultiple();

  const materialSearchContentFilterType = useQueryState<MaterialSearchContentFilterType | null>("materialSearchContentFilterType");
  const materialSearchSortType = useQueryState<MaterialSearchSortType | null>("materialSearchSortType");

  const isInRoot = useIsInRoot();
  const isInRecent = useIsInRecent();
  const isInImages = useIsInImages();
  const isInDocuments = useIsInDocuments();
  const isInShared = useIsInShared();

  const { Icon, title } = getIconAndTitle({ isInRoot, isInRecent, isInImages, isInDocuments, isInShared });

  const selectedMaterial = useSelectedMaterial();
  const selectedMaterialId = selectedMaterial?.materialId;
  const setDragOverMaterialId = useSetDragOverMaterialId();

  const cdFolder = useCdFolder();
  const resetList = useResetList();

  const [initializeFolder, {}] = useInitializeFolderMutation();
  const [moveToFolder, {}] = useMoveToFolderMutation();

  const materialPicker = useRef<HTMLInputElement>(null);

  const changeView = (viewType?: "list" | "grid") => {
    setQueryStateMultiple(
      new Map([
        ["viewType", viewType]
      ]));
  };

  const changeViewToList = () => {
    changeView("list");
  };

  const changeViewToGrid = () => {
    changeView("grid");
  };


  const createNewFolder = (name: string) => {
    dispatch(closeBasicTextInputModal());
    const parentMaterialId = container?.materialType == "FOLDER" ? container?.materialId : undefined;
    const body = { name, parentMaterialId };
    initializeFolder({ workspaceId, body });
    resetList();
  };

  const popNewFolderModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("newMaterialFolderModalTitle"),
        infoText: t("newMaterialFolderModalText"),
        onSubmit: createNewFolder,
        initialText: ""
      })
    );
  };

  const pickFile = () => {
    if (materialPicker.current) {
      materialPicker.current.value = "";
      materialPicker.current.click();
    }
  };

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target?.files?.length) {
      const files = Array.from(target.files);
      handleUpload({ files });
    }
  };

  const onBreadcrumbDrop = (breadCrumbMaterialId?: string) => {
    if (selectedMaterialId) {
      logger.log({ droppedTo: breadCrumbMaterialId, selectedMaterialId });
      const body = { materialId: selectedMaterialId, parentMaterialId: breadCrumbMaterialId };
      moveToFolder({ body, onFulfilled: resetList });
      setDragOverMaterialId();
    }
  };

  return (
    <div id={"material-folder-header"} className={styles.container}>
      <div className={styles.titleContainer}>
        {container && <>
          <LuFolder size={24} />
          <h1 className={cn("line-clamp", styles.title)}>
            {container.name}
          </h1>
        </>}
        {!container && <>
          <Icon size={24} />
          <h1 className={cn("line-clamp", styles.title)}>
            {t(title)}
          </h1>
        </>}
      </div>

      <Breadcrumb
        container={container}
        onClick={cdFolder}
        onBreadcrumbDrop={onBreadcrumbDrop}
        hideBreadcrumbs={hideBreadcrumbs}
      />

      {!hideActionBar &&
        <div className={styles.actionBarContainer}>
          <Button
            className={styles.button}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.filled}
            data-tooltip-right-mobile={t("folderHeaderActionButtonNewFolder")}
            onClick={popNewFolderModal}
          >
            <LuFolderPlus className={"icon"} />
            <span className={"hidden-mobile"}>{t("folderHeaderActionButtonNewFolder")}</span>
          </Button>

          <Button
            className={styles.button}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            onClick={pickFile}
          >
            <LuPlus className={"icon"} />
            <span><b>{t("sideMenuFilesUpload")}</b></span>
          </Button>

          <input
            ref={materialPicker}
            id={"material-picker"}
            type="file"
            accept="*/*"
            className={styles.attachmentInput}
            onChange={onSelectFile}
            multiple={true}
          />
        </div>
      }

    </div>
  );
};

export default FolderHeader;