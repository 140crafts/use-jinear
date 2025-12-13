import React from "react";
import styles from "./FavoriteFiles.module.css";
import useTranslation from "@/locals/useTranslation";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";

interface FavoriteFilesProps {

}

const FavoriteFiles: React.FC<FavoriteFilesProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuTaskFilesFavorites")} />

    </div>
  );
};

export default FavoriteFiles;