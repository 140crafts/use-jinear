import React, { useMemo } from "react";
import styles from "./MaterialViewRow.module.css";
import { MaterialAccessType, MaterialDto, WorkspaceDto } from "@/be/jinear-core";
import { format } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import { humanReadibleFileSize } from "@/utils/FileSizeFormatter";
import {
  LuFile,
  LuFileArchive,
  LuFileAudio as LuFileMusic,
  LuFileBarChart as LuFileChartColumn,
  LuFileImage,
  LuFileJson,
  LuFilePieChart as LuFileChartPie,
  LuFileText,
  LuFileVideo2,
  LuFolder,
  LuFolderInput,
  LuGlobe as LuEarth, LuLink,
  LuLock,
  LuMinus,
  LuTextCursorInput,
  LuTrash,
  LuUserCheck,
  LuUserPlus,
  LuUsers
} from "react-icons/lu";
import { calculateDateDiff } from "@/utils/DateHelper";
import cn from "classnames";
import {
  useCdFolder,
  useDragOverMaterialId,
  useResetList,
  useSelectedMaterial,
  useSetDragOverMaterialId,
  useSetSelectedMaterial
} from "@/components/workspaceFilesPage/context/MaterialViewContext";
import Logger from "@/utils/logger";
import Button, { ButtonHeight } from "@/components/button";
import {
  useDeleteMaterialPermanentlyMutation,
  useMoveToFolderMutation,
  useRenameMaterialMutation
} from "@/api/materialOperationApi";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import {
  closeBasicTextInputModal,
  closeDialogModal,
  popBasicTextInputModal,
  popDialogModal,
  popMaterialAccessModal,
  popMaterialFolderPickerModal
} from "@/slice/modalSlice";
import { API_ROOT, HOST } from "@/utils/constants";
import useWidthLimit, { MOBILE_LAYOUT_BREAKPOINT } from "@/hooks/useWidthLimit";
import strings from "@/locals/strings";
import ProfilePhoto from "@/components/profilePhoto";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import { useWorkspaceRoleIsAdminOrOwner } from "@/hooks/useWorkspaceRoleIsAdminOrOwner";
import { copyTextToClipboard } from "@/utils/clipboard";
import toast from "react-hot-toast";

interface MaterialViewRowProps {
  material: MaterialDto;
  workspace: WorkspaceDto;
}

export const ICON_MAP = {
  "default": LuFile,
  "video": LuFileVideo2,
  "image": LuFileImage,
  "audio": LuFileMusic,
  "json": LuFileJson,
  "x-compressed": LuFileArchive,
  "x-apple-diskimage": LuFileArchive,
  "iso": LuFileArchive,
  "diskimage": LuFileArchive,
  "zip": LuFileArchive,
  "pdf": LuFileText,
  "text": LuFileText,
  "document": LuFileText,
  "sheet": LuFileChartColumn,
  "presentation": LuFileChartPie
};

export const getMaterialIcon = (contentType?: string) => {
  const parts = contentType?.split("/");
  const type = parts?.find(part => Object.keys(ICON_MAP).find(value => value.indexOf(part) != -1)) ?? "default";
  // @ts-expect-error
  return ICON_MAP[type];
};

const logger = Logger("MaterialViewRow");

const ACCESS_TYPE_TOOLTIP_MAP: Record<MaterialAccessType, keyof typeof strings> = {
  OWNER_ONLY: "materialListRowAccessOnlyYouTooltip",
  WORKSPACE_MEMBERS: "materialListRowAccessWorkspaceMembersTooltip",
  GRAINED: "materialListRowAccessGrainedTooltip",
  ANYONE_WITH_LINK: "materialListRowAccessAnyoneWithTheLinkTooltip"
};

export const ACCESS_TYPE_ICON_MAP = {
  OWNER_ONLY: LuLock,
  WORKSPACE_MEMBERS: LuUsers,
  GRAINED: LuUserCheck,
  ANYONE_WITH_LINK: LuEarth
};

const MaterialViewRow: React.FC<MaterialViewRowProps> = ({ material, workspace }) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const isCurrAccWorkspaceAdminOrOwner = useWorkspaceRoleIsAdminOrOwner({ workspaceId: material.workspaceId });
  const hasUpdatePermission = material.ownerId == currentAccountId || isCurrAccWorkspaceAdminOrOwner;

  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });

  const resetList = useResetList();
  const [moveToFolder, {}] = useMoveToFolderMutation();
  const [renameMaterial, {}] = useRenameMaterialMutation();
  const [permanentlyDeleteMaterial, {}] = useDeleteMaterialPermanentlyMutation();

  const lastTapRef = React.useRef<number>(0);
  const dragCounter = React.useRef(0);
  const dragOverMaterialId = useDragOverMaterialId();
  const setDragOverMaterialId = useSetDragOverMaterialId();

  const isDragOver = material.materialId == dragOverMaterialId;

  const cdFolder = useCdFolder();
  const setSelectedMaterial = useSetSelectedMaterial();
  const selectedMaterial = useSelectedMaterial();
  const selectedMaterialId = selectedMaterial?.materialId;

  const isSelected = selectedMaterialId == material.materialId;

  const createdFullDate = format(new Date(material.createdDate), t("dateTimeFormat"));
  const updateFullDate = material.lastUpdatedDate ? format(new Date(material.lastUpdatedDate), t("dateTimeFormat")) : undefined;

  const { diffInDaysLastUpdate, diffInDaysCreate } = useMemo(() => {
    const diffInDaysLastUpdate = calculateDateDiff(material.lastUpdatedDate);
    const diffInDaysCreate = calculateDateDiff(material.createdDate);
    return { diffInDaysLastUpdate, diffInDaysCreate };
  }, [material.lastUpdatedDate, material.createdDate]);

  const FileIcon = material.materialType == "FOLDER" ? LuFolder : getMaterialIcon(material.media?.contentType ?? "default");
  const shouldOmitDoubleClick = material.materialType == "FILE" && isMobile;

  const accessUserUserNames = material.materialAccesses?.map(access => access.account?.username);
  let accessTypeTooltip = t(ACCESS_TYPE_TOOLTIP_MAP[material.materialAccessType]);
  accessTypeTooltip = material.materialAccessType == "GRAINED" && accessUserUserNames?.length > 0 ? `${accessTypeTooltip} (${accessUserUserNames?.join(",")})` : accessTypeTooltip;
  const AccessTypeIcon = ACCESS_TYPE_ICON_MAP[material.materialAccessType];

  const handleTap = () => {
    const now = Date.now();
    const DOUBLE_TAP_DELAY = 300;

    if (now - lastTapRef.current < DOUBLE_TAP_DELAY) {
      if (material.materialType == "FOLDER") {
        cdFolder(material.materialId);
      } else if (material.materialType == "FILE") {
        window.open(`${API_ROOT}v1/material/media/${material.materialId}`, isMobile ? undefined : "_blank");
      }
      lastTapRef.current = 0;
    } else {
      lastTapRef.current = now;
      setSelectedMaterial(material);
    }
  };

  const handleDragEnter = () => {
    if (material.materialType == "FOLDER") {
      dragCounter.current++;
      setDragOverMaterialId(material.materialId);
    }
  };

  const handleDragLeave = () => {
    if (material.materialType == "FOLDER") {
      dragCounter.current--;
      if (dragCounter.current === 0) {
        setDragOverMaterialId();
      }
    }
  };

  const onDrop = () => {
    if (material.materialType == "FOLDER" && selectedMaterialId) {
      logger.log({ droppedTo: material.materialId, selectedMaterialId });
      const body = { materialId: selectedMaterialId, parentMaterialId: material.materialId };
      moveToFolder({ body, onFulfilled: resetList });
      setDragOverMaterialId();
    }
  };

  const rename = (newName: string) => {
    dispatch(closeBasicTextInputModal());
    renameMaterial({ body: { materialId: material.materialId, newName }, onFulfilled: resetList });
  };

  const popFolderPicker = () => {
    const onPick = (pickedMaterialId?: string) => {
      const body = { materialId: material.materialId, parentMaterialId: pickedMaterialId };
      moveToFolder({
        body,
        onFulfilled: resetList
      });
    };
    dispatch(popMaterialFolderPickerModal({
      workspaceId: material.workspaceId,
      title: t("materialFolderPickerModalMoveTitle"),
      onPick,
      visible: true
    }));
  };

  const popChangeNameModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("renameMaterialModalTitle"),
        infoText: t("renameMaterialModalText"),
        onSubmit: rename,
        initialText: material.name
      })
    );
  };

  const deletePermanently = () => {
    resetList();
    permanentlyDeleteMaterial({ materialId: material.materialId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForPermanentlyDelete = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("materialPermanentlyDeleteAreYouSureTitle"),
        content: t("materialPermanentlyDeleteAreYouSureText"),
        confirmButtonLabel: t("materialPermanentlyDeleteAreYouSureConfirmLabel"),
        onConfirm: deletePermanently
      })
    );
  };

  const popAccessModal = () => {
    dispatch(popMaterialAccessModal({ materialId: material.materialId, visible: true, resetList }));
  };

  const copyMediaLink = () => {
    if (material.materialType == "FILE") {
      copyTextToClipboard(`${API_ROOT}v1/material/media/${material.materialId}`);
    } else if (material.materialType == "FOLDER") {
      copyTextToClipboard(`${HOST}/${workspace.username}/files?parentMaterialId=${material.materialId}`);
    }
    toast(t("materialRowCopyLinkCopiedToast"));
  };

  return (
    <tr
      draggable={true}
      key={material.materialId}
      className={cn(styles.container, isSelected && styles.selected, isDragOver && styles.dragOver)}
      onPointerDown={shouldOmitDoubleClick ? undefined : handleTap}
      onDragOver={(event) => event.preventDefault()}
      onDragEnter={handleDragEnter}
      onDragLeave={handleDragLeave}
      onDrop={onDrop}
    >
      <td>
        <div className={styles.materialName}>
          <FileIcon size={16} className={styles.icon} />
          {material.materialType === "FILE" ? (
            <a
              href={`${API_ROOT}v1/material/media/${material.materialId}`}
              className={cn(isSelected ? styles.nameSelected : styles.name, "line-clamp", "flex-1")}
              target="_blank"
              rel="noopener noreferrer"
              onClick={shouldOmitDoubleClick ? undefined : (e) => e.preventDefault()}
            >
              {material.name}
            </a>
          ) : (
            <span className={cn(isSelected ? styles.nameSelected : styles.name, "line-clamp")}>
              {material.name}
            </span>
          )}
        </div>
      </td>
      <td data-tooltip-right={material.owner?.username}>
        <div className={styles.accessIconContainer}>
          <ProfilePhoto
            boringAvatarKey={material.ownerId}
            url={material.owner?.profilePicture?.url}
            wrapperClassName={styles.accessUserProfilePic}
          />
        </div>
      </td>
      <td
        data-tooltip-right={material.materialAccessType == "GRAINED" ? undefined : accessTypeTooltip}
        data-tooltip-multiline={material.materialAccessType == "GRAINED" ? accessTypeTooltip : undefined}
      >
        <div className={styles.accessIconContainer}>
          <AccessTypeIcon className={"icon"} />
        </div>
      </td>

      {!isMobile &&
        <td className={styles.infoColumn}>
          {material.media?.size ?
            <span className={"line-clamp"}>
              {humanReadibleFileSize(material.media?.size)}
            </span> :
            <LuMinus />
          }
        </td>
      }

      {/*{!isMobile &&*/}
      {/*  <td className={styles.infoColumn} data-tooltip={updateFullDate}>*/}
      {/*  <span className={"line-clamp"}>*/}
      {/*    {diffInDaysLastUpdate}*/}
      {/*  </span>*/}
      {/*  </td>*/}
      {/*}*/}

      <td className={styles.infoColumn} data-tooltip={createdFullDate}>
      <span className={"line-clamp"}>
        {diffInDaysCreate}
        </span>
      </td>


      <td style={{ position: "relative" }}>

        <div
          className={cn(styles.moreButtonContainer, isSelected && styles.moreButtonContainerSelected)}>
          <Button
            heightVariant={ButtonHeight.short2x}
            className={isSelected ? styles.moreButtonSelected : undefined}
            onClick={copyMediaLink}
            data-tooltip-right={t("materialRowCopyLinkButtonTooltip")}
          >
            <LuLink size={14} className={styles.icon} />
          </Button>
          {hasUpdatePermission &&
            <>
              <Button
                heightVariant={ButtonHeight.short2x}
                className={isSelected ? styles.moreButtonSelected : undefined}
                onClick={popAccessModal}
                data-tooltip-right={t("materialRowChangeAccessButtonTooltip")}
              >
                <LuUserPlus size={14} className={styles.icon} />
              </Button>

              <Button
                heightVariant={ButtonHeight.short2x}
                className={isSelected ? styles.moreButtonSelected : undefined}
                onClick={popChangeNameModal}
                data-tooltip-right={t("materialRowChangeNameButtonTooltip")}
              >
                <LuTextCursorInput size={14} className={styles.icon} />
              </Button>
              <Button
                heightVariant={ButtonHeight.short2x}
                className={isSelected ? styles.moreButtonSelected : undefined}
                onClick={popFolderPicker}
                data-tooltip-right={t("materialRowMoveButtonTooltip")}
              >
                <LuFolderInput size={14} className={styles.icon} />
              </Button>
              <Button
                heightVariant={ButtonHeight.short2x}
                className={isSelected ? styles.moreButtonSelected : undefined}
                onClick={popAreYouSureModalForPermanentlyDelete}
                data-tooltip-right={t("materialRowDeleteButtonTooltip")}
              >
                <LuTrash size={14} className={styles.icon} />
              </Button>
            </>
          }
        </div>
      </td>

    </tr>
  );
};

export default MaterialViewRow;