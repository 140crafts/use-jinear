import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./PageInfo.module.css";

interface PageInfoProps {
  number?: number;
  size?: number;
  totalElements?: number;
}

const PageInfo: React.FC<PageInfoProps> = ({
  number = 0,
  size = 0,
  totalElements = 0,
}) => {
  const { t } = useTranslation();
  const pageStartIndex = number * size + 1;
  const pageLastIndex = number * size + size;
  const pageLastIndexMeaningful = Math.min(pageLastIndex, totalElements);

  const totalRecordCount = t("pageInfoTotalRecords").replace(
    "{total}",
    totalElements.toString()
  );

  return (
    <div
      className={styles.container}
    >{`${pageStartIndex}-${pageLastIndexMeaningful} ${totalRecordCount}`}</div>
  );
};

export default PageInfo;
