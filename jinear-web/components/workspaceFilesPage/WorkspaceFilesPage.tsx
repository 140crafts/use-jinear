import React, { useEffect, useMemo, useState } from "react";
import styles from "./WorkspaceFilesPage.module.css";
import { MaterialDto, MaterialSearchContentFilterType, MaterialSearchSortType, WorkspaceDto } from "@/be/jinear-core";
import { materialListingApi, useSearchMaterialQuery } from "@/api/materialListingApi";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading";
import FolderHeader, { useWorkspaceFilesViewType } from "@/components/workspaceFilesPage/folderHeader/FolderHeader";
import { useQueryState, useSetQueryStateMultiple } from "@/hooks/useQueryState";
import MaterialListView from "@/components/workspaceFilesPage/materialListView/MaterialListView";
import MaterialViewContext from "@/components/workspaceFilesPage/context/MaterialViewContext";
import Logger from "@/utils/logger";
import DropZone from "@/components/dropZone/DropZone";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { useInitializeMaterialFileUploadMutation, useNotifyMaterialUploadedMutation } from "@/api/materialOperationApi";
import { pushDataToUploadStatusModalQueue } from "@/slice/modalSlice";

export interface IFilesPageFilter {
  parentMaterialId?: string | null;
  materialSearchSortType?: MaterialSearchSortType | null;
  materialSearchContentFilterType?: MaterialSearchContentFilterType | null;
  shared?: boolean | null;
  deleted?: boolean | null;
}

interface WorkspaceFilesPageProps {
  workspace: WorkspaceDto;
  filter: IFilesPageFilter;
}

const logger = Logger("WorkspaceFilesPage");

const WorkspaceFilesPage: React.FC<WorkspaceFilesPageProps> = ({ workspace, filter }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [initializeMaterialFileUpload, {}] = useInitializeMaterialFileUploadMutation();
  const [notifyMaterialUploaded, {}] = useNotifyMaterialUploadedMutation();

  const setQueryStateMultiple = useSetQueryStateMultiple();
  const [page, setPage] = useState<number>(0);
  const [selectedMaterial, setSelectedMaterial] = useState<MaterialDto | undefined>();
  const parentMaterialId = filter.parentMaterialId;
  const viewType = useWorkspaceFilesViewType();

  const prefetchSearchMaterial = materialListingApi.usePrefetch("searchMaterial");
  const { data: paginatedMaterialSearchResponse, isLoading, isFetching, requestId } = useSearchMaterialQuery({
    workspaceId: workspace.workspaceId,
    page,
    ...filter
  });

  const container = paginatedMaterialSearchResponse?.data?.container;
  const [materialList, setMaterialList] = useState<MaterialDto[]>([]);
  const [dragOverMaterialId, setDragOverMaterialId] = useState<string>();

  const materialSearchContentFilterType = useQueryState<MaterialSearchContentFilterType | null>("materialSearchContentFilterType");
  const materialSearchSortType = useQueryState<MaterialSearchSortType | null>("materialSearchSortType");

  const isInRoot = parentMaterialId == null && materialSearchSortType == null;
  const isInRecent = parentMaterialId == null && materialSearchSortType == "IDATE_DESC" && materialSearchContentFilterType == null;
  const isInImages = parentMaterialId == null && materialSearchContentFilterType == "IMAGE";
  const isInDocuments = parentMaterialId == null && materialSearchContentFilterType == "DOC";
  const isInShared = parentMaterialId == null && materialSearchContentFilterType == "SHARED";

  const shouldDropToUploadDisabled = isInRecent || isInImages || isInDocuments || isInShared;

  useEffect(() => {
    resetList();
    setSelectedMaterial(undefined);
    setPage(0);
  }, [filter]);

  const resetList = () => {
    setMaterialList([]);
  };

  useEffect(() => {
    const children = paginatedMaterialSearchResponse?.data?.content;
    if (children?.hasContent) {
      setMaterialList(current => [...current, ...children.content]);
      children.hasNext && setPage(page => page + 1);

      const MAX_PREFETCH = 10;
      const DELAY_MS = 150;

      const folders = children.content
        .filter(material => material.materialType === "FOLDER")
        .slice(0, MAX_PREFETCH);

      const timeoutIds = folders.map((folder, index) =>
        setTimeout(() => {
          prefetchSearchMaterial({
            ...filter,
            workspaceId: workspace.workspaceId,
            parentMaterialId: folder.materialId,
            page: 0
          });
        }, index * DELAY_MS)
      );

      return () => {
        timeoutIds.forEach(clearTimeout);
      };
    }
  }, [paginatedMaterialSearchResponse]);

  const cdFolder = (materialId?: string) => {
    setQueryStateMultiple(
      new Map([
        ["parentMaterialId", materialId]
      ]));
  };

  const handleUpload = async ({ files = [] }: { files: File[] }) => {
    try {
      const parentMaterialId = container?.materialId;
      const presignedUploadData = [];

      for (const file of files) {
        const body = {
          name: file.name ?? "File",
          contentType: file.type ?? "application/octet-stream",
          fileSize: file.size,
          parentMaterialId
        };
        const { data: presignedUrlResponse } = await initializeMaterialFileUpload({
          workspaceId: workspace.workspaceId,
          body
        }).unwrap();

        const onCompleteNotify = () => {
          notifyMaterialUploaded({ workspaceId: workspace.workspaceId, materialId: presignedUrlResponse.materialId });
          resetList?.();
        };

        const uploadData = {
          relatedObjectId: presignedUrlResponse.materialId,
          presignedUrl: presignedUrlResponse.presignedUrl,
          file,
          onComplete: onCompleteNotify
        };
        presignedUploadData.push(uploadData);
      }

      dispatch(pushDataToUploadStatusModalQueue({
        workspaceId: workspace.workspaceId,
        presignedUploadData,
        visible: true
      }));
    } catch (e) {
      console.log(e);
    }
  };

  const contextValue = useMemo(() => ({
    cdFolder,
    resetList,
    selectedMaterial,
    setSelectedMaterial,
    dragOverMaterialId,
    setDragOverMaterialId,
    isInRoot,
    isInRecent,
    isInImages,
    isInDocuments,
    isInShared
  }), [cdFolder, resetList, selectedMaterial, setSelectedMaterial, dragOverMaterialId, setDragOverMaterialId, isInRoot, isInRecent, isInImages, isInDocuments, isInShared]);

  return (
    <MaterialViewContext.Provider
      value={contextValue}>
      <DropZone
        disabled={shouldDropToUploadDisabled}
        onDrop={handleUpload}
        overlayText={t("materialDropToUploadText")}
      >
        <div className={styles.container}>
          <div className={styles.dropOverlay} />
          <FolderHeader
            container={container}
            workspaceId={workspace.workspaceId}
            handleUpload={handleUpload}
            hideBreadcrumbs={shouldDropToUploadDisabled}
            hideActionBar={shouldDropToUploadDisabled}
          />
          <div className={styles.loadingContainer}>
            {isFetching && <InfiniteLineLoading />}
          </div>
          <div className={styles.contentContainer}>
            {viewType == "list" &&
              <MaterialListView content={materialList} />}
          </div>
        </div>
      </DropZone>
    </MaterialViewContext.Provider>
  );
};

export default WorkspaceFilesPage;