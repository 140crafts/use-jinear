import React, { useCallback } from "react";
import styles from "./MaterialListView.module.css";
import { MaterialDto } from "@/be/jinear-core";
import MaterialViewRow from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import useElementSize from "@/hooks/useElementSize";
import useTranslation from "@/locals/useTranslation";
import { useSetDragOverMaterialId, useSetSelectedMaterial } from "../context/MaterialViewContext";
import cn from "classnames";
import useWidthLimit, { MOBILE_LAYOUT_BREAKPOINT } from "@/hooks/useWidthLimit";

interface MaterialListViewProps {
  content: MaterialDto[];
}

const MaterialListView: React.FC<MaterialListViewProps> = ({ content }) => {
  const { t } = useTranslation();
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });
  const setSelectedMaterial = useSetSelectedMaterial();
  const setDragOverMaterialId = useSetDragOverMaterialId();

  const body = document.getElementsByTagName("body")[0];
  const workspaceLayoutHeader = document.getElementById("workspace-layout-header");
  const materialFolderHeader = document.getElementById("material-folder-header");
  const { offsetHeight: bodyHeight } = useElementSize(body);
  const { offsetHeight: workspaceLayoutHeaderHeight } = useElementSize(workspaceLayoutHeader);
  const { offsetHeight: materialFolderHeaderHeight } = useElementSize(materialFolderHeader);
  const contentHeight = Math.max((bodyHeight ?? 0) - (workspaceLayoutHeaderHeight ?? 0) - (materialFolderHeaderHeight ?? 0) - 64, 125);

  const handleTableClick = useCallback((e: React.MouseEvent<HTMLTableElement>) => {
    const target = e.target as HTMLElement;
    const clickedRow = target.closest("tbody tr");
    if (!clickedRow) {
      setSelectedMaterial(undefined);
    }
  }, [setSelectedMaterial]);

  const handleTableDragEnd = () => {
    setDragOverMaterialId(undefined);
  };

  return (
    <table className={styles.container}
           style={{ height: contentHeight }}
           onClick={handleTableClick}
           onDragEnd={handleTableDragEnd}
    >
      <colgroup>
        <col span={1} style={{ width: "80%" }} />
        <col span={1} style={{ minWidth: "11ch" }} />
        {!isMobile && <col span={1} />}
        <col span={1} />
      </colgroup>
      <thead>
      <tr className={styles.header}>
        <th><span className={"line-clamp"}>{t("materialListRowTitleName")}</span></th>
        <th><span className={cn("line-clamp", styles.accessLabel)}>{t("materialListRowAccess")}</span></th>
        {!isMobile && <th><span className={"line-clamp"}>{t("materialListRowTitleSize")}</span></th>}
        {/*{!isMobile && <th><span className={"line-clamp"}>{t("materialListRowTitleLastUpdate")}</span></th>}*/}
        <th><span className={"line-clamp"}>{t("materialListRowTitleCreated")}</span></th>
      </tr>
      </thead>
      <tbody>
      {content?.map(material =>
        <MaterialViewRow
          key={material.materialId}
          material={material}
        />
      )}
      </tbody>
    </table>
  );
};

export default MaterialListView;