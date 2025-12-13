import React from "react";
import styles from "./FolderHeader.module.css";
import { PathAwareMaterialDto } from "@/be/jinear-core";
import Breadcrumb from "@/components/workspaceFilesPage/folderHeader/breadcrumb/Breadcrumb";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuFolder, LuFolderPlus, LuGrid, LuList, LuShare, LuUpload } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import cn from "classnames";
import { useQueryState, useSetQueryStateMultiple } from "@/hooks/useQueryState";

interface FolderHeaderProps {
  container?: PathAwareMaterialDto;
  cdFolder: (materialId?: string) => void;
}

export const useWorkspaceFilesViewType = () => {
  const defaultViewType = "list";
  const queryViewType = useQueryState<"list" | "grid">("viewType") ?? defaultViewType;
  return ["list", "grid"]?.indexOf(queryViewType.toLowerCase()) == -1 ? defaultViewType : queryViewType;
};

const FolderHeader: React.FC<FolderHeaderProps> = ({ container, cdFolder }) => {
  const { t } = useTranslation();
  const setQueryStateMultiple = useSetQueryStateMultiple();
  const viewType = useWorkspaceFilesViewType();

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

  return (
    <div id={'material-folder-header'} className={styles.container}>
      <Breadcrumb container={container} cdFolder={cdFolder} />

      <div className={styles.titleContainer}>
        {container && <>
          <LuFolder size={24} />
          <h1 className={cn("line-clamp", styles.title)}>
            {container.name}
          </h1>
        </>}
      </div>

      <div className={styles.actionBarContainer}>
        <div className={styles.viewTypeChangeButtonContainer}>
          <Button
            className={styles.button}
            heightVariant={ButtonHeight.mid}
            variant={viewType == "list" ? ButtonVariants.filled2 : ButtonVariants.filled}
            onClick={changeViewToList}
          >
            <LuList className={"icon"} />
          </Button>
          <Button
            className={styles.button}
            heightVariant={ButtonHeight.mid}
            variant={viewType == "grid" ? ButtonVariants.filled2 : ButtonVariants.filled}
            onClick={changeViewToGrid}
          >
            <LuGrid className={"icon"} />
          </Button>
        </div>

        <Button
          className={styles.button}
          heightVariant={ButtonHeight.mid}
          variant={ButtonVariants.filled}>
          <LuShare className={"icon"} />
          {t("folderHeaderActionButtonShare")}
        </Button>

        <Button
          className={styles.button}
          heightVariant={ButtonHeight.mid}
          variant={ButtonVariants.filled}>
          <LuFolderPlus className={"icon"} />
          {t("folderHeaderActionButtonNewFolder")}
        </Button>

        <Button
          className={styles.button}
          heightVariant={ButtonHeight.mid}
          variant={ButtonVariants.contrast}>
          <LuUpload className={"icon"} />
          <b>{t("sideMenuFilesUpload")}</b>
        </Button>
      </div>
    </div>
  );
};

export default FolderHeader;