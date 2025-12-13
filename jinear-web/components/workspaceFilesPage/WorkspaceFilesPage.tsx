import React, { useEffect, useState } from "react";
import styles from "./WorkspaceFilesPage.module.css";
import { MaterialDto, MaterialSearchContentFilterType, MaterialSearchSortType, WorkspaceDto } from "@/be/jinear-core";
import { useSearchMaterialQuery } from "@/api/materialListingApi";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading";
import FolderHeader, { useWorkspaceFilesViewType } from "@/components/workspaceFilesPage/folderHeader/FolderHeader";
import Button from "../button";
import { useSetQueryStateMultiple } from "@/hooks/useQueryState";
import MaterialListView from "@/components/workspaceFilesPage/materialListView/MaterialListView";

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

const WorkspaceFilesPage: React.FC<WorkspaceFilesPageProps> = ({ workspace, filter }) => {
  const setQueryStateMultiple = useSetQueryStateMultiple();
  const [page, setPage] = useState<number>(0);
  const parentMaterialId = filter.parentMaterialId;
  const viewType = useWorkspaceFilesViewType();

  const { data: paginatedMaterialSearchResponse, isLoading, isFetching } = useSearchMaterialQuery({
    workspaceId: workspace.workspaceId,
    page,
    ...filter
  });
  const container = paginatedMaterialSearchResponse?.data?.container;
  const [materialList, setMaterialList] = useState<MaterialDto[]>([]);

  useEffect(() => {
    setMaterialList([]);
  }, [filter]);

  useEffect(() => {
    const children = paginatedMaterialSearchResponse?.data?.content;
    if (children?.hasContent) {
      setMaterialList(current => [...current, ...children.content]);
      children.hasNext && setPage(page => page + 1);
    }
  }, [paginatedMaterialSearchResponse]);

  const cdFolder = (materialId?: string) => {
    setQueryStateMultiple(
      new Map([
        ["parentMaterialId", materialId]
      ]));
  };

  return (
    <div className={styles.container}>
      <FolderHeader container={container} cdFolder={cdFolder} />
      <div className={styles.loadingContainer}>
        {isFetching && <InfiniteLineLoading />}
      </div>
      <div className={styles.contentContainer}>
        {viewType == "list" && <MaterialListView content={materialList} cdFolder={cdFolder} />}
        {/*{materialList?.map(material => <Button*/}
        {/*  className={styles.materialButton}*/}
        {/*  key={material.materialId}*/}
        {/*  onClick={() => material.materialType == "FOLDER" && cdFolder(material.materialId)}>{material.name}</Button>*/}
        {/*)}*/}
      </div>
    </div>
  );
};

export default WorkspaceFilesPage;