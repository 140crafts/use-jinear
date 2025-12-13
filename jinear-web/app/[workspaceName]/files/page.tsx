"use client";

import React, { useMemo } from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import WorkspaceFilesPage, { IFilesPageFilter } from "@/components/workspaceFilesPage/WorkspaceFilesPage";
import { useTypedSelector } from "@/store/store";
import { selectWorkspaceFromWorkspaceUsername } from "@/slice/accountSlice";
import { MaterialSearchContentFilterType, MaterialSearchSortType, WorkspaceDto } from "@/be/jinear-core";
import { queryStateBooleanParser, useQueryState } from "@/hooks/useQueryState";

interface FilesPageProps {

}

const FilesPage: React.FC<FilesPageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName)) as WorkspaceDto;

  const parentMaterialId = useQueryState<string>("parentMaterialId");
  const materialSearchSortType = useQueryState<MaterialSearchSortType | null>("materialSearchSortType");
  const materialSearchContentFilterType = useQueryState<MaterialSearchContentFilterType | null>("materialSearchContentFilterType");
  const shared = useQueryState<boolean | null>("shared", queryStateBooleanParser) ?? false;
  const deleted = useQueryState<boolean | null>("deleted", queryStateBooleanParser) ?? false;

  const filter: IFilesPageFilter = useMemo(() => ({
    parentMaterialId,
    materialSearchSortType,
    materialSearchContentFilterType,
    shared,
    deleted
  }), [parentMaterialId, materialSearchSortType, materialSearchContentFilterType, shared, deleted]);

  return (
    <div className={styles.container}>
      {workspace && <WorkspaceFilesPage workspace={workspace} filter={filter} />}
    </div>
  );
};

export default FilesPage;