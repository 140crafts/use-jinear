import React from "react";
import styles from "./Breadcrumb.module.css";
import { PathAwareMaterialDto } from "@/be/jinear-core";
import Button, { ButtonHeight } from "@/components/button";
import Logger from "@/utils/logger";
import { IoHome } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";

interface BreadcrumbProps {
  container?: PathAwareMaterialDto;
  cdFolder: (materialId?: string) => void;
}

const logger = Logger("Breadcrumb");

const Breadcrumb: React.FC<BreadcrumbProps> = ({ container, cdFolder }) => {
  const { t } = useTranslation();
  const materialPath = container?.materialPath;

  return (
    <div className={styles.container}>
      <div className={styles.breadcrumb}>
        <Button
          heightVariant={ButtonHeight.short2x}
          onClick={() => cdFolder()}
          className={styles.button}
        >
          <IoHome className={"icon"} />
          <span>{t('folderBreadcrumbHome')}</span>
        </Button>
        {<span>/</span>}
      </div>

      {materialPath?.path?.map((path, index) =>
        <div
          key={`breadcrumb-button-${path.materialId}`}
          className={styles.breadcrumb}
        >
          <Button
            heightVariant={ButtonHeight.short2x}
            onClick={() => cdFolder(path.materialId)}
            className={styles.button}
          >
            {path.name}
          </Button>
          {materialPath?.path?.[index + 1] && <span>/</span>}
        </div>
      )}
    </div>
  );
};

export default Breadcrumb;