import React from "react";
import styles from "./MaterialListView.module.css";
import { MaterialDto } from "@/be/jinear-core";
import MaterialViewRow from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import useElementSize from "@/hooks/useElementSize";

interface MaterialListViewProps {
  content: MaterialDto[];
  cdFolder: (materialId?: string) => void;
}

const MaterialListView: React.FC<MaterialListViewProps> = ({ content, cdFolder }) => {

  const body = document.getElementsByTagName("body")[0];
  const workspaceLayoutHeader = document.getElementById("workspace-layout-header");
  const materialFolderHeader = document.getElementById("material-folder-header");
  const { offsetHeight: bodyHeight } = useElementSize(body);
  const { offsetHeight: workspaceLayoutHeaderHeight } = useElementSize(workspaceLayoutHeader);
  const { offsetHeight: materialFolderHeaderHeight } = useElementSize(materialFolderHeader);
  const contentHeight = Math.max((bodyHeight ?? 0) - (workspaceLayoutHeaderHeight ?? 0) - (materialFolderHeaderHeight ?? 0) - 64, 125);

  return (
    <table className={styles.container}
           style={{ height: contentHeight }}
    >
      <thead>
      <tr className={styles.header}>
        <th>Name</th>
        <th>Date Modified</th>
        <th>Type</th>
        <th>Size</th>
      </tr>
      </thead>
      <tbody>
      {content?.map(material => <MaterialViewRow key={material.materialId} material={material} cdFolder={cdFolder} />)}
      </tbody>
    </table>
  );
};

export default MaterialListView;