import React, { useMemo } from "react";
import styles from "./MaterialViewRow.module.css";
import { MaterialDto } from "@/be/jinear-core";
import { differenceInDays, differenceInHours, differenceInMinutes, format } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import { humanReadibleFileSize } from "@/utils/FileSizeFormatter";
import { LuFile, LuFileImage, LuFileVideo2, LuFolder } from "react-icons/lu";

interface MaterialViewRowProps {
  material: MaterialDto;
  cdFolder: (materialId?: string) => void;
}

const ICON_MAP ={
  video : LuFileVideo2,
  image : LuFileImage,
  // audio: LuFileMusic
}

const MaterialViewRow: React.FC<MaterialViewRowProps> = ({ material, cdFolder }) => {
  const { t } = useTranslation();

  const dateToUse = material.lastUpdatedDate ? material.lastUpdatedDate : material.createdDate;
  const fullDate = format(new Date(dateToUse), t("dateTimeFormat"));

  const dateDiff = useMemo(() => {
    const diffInDays = differenceInDays(new Date(), new Date(dateToUse));
    if (diffInDays != 0) {
      return t("dateDiffLabelDateInDays")?.replace("${num}", `${diffInDays}`);
    }
    const diffInHours = differenceInHours(new Date(), new Date(dateToUse));
    if (diffInHours != 0) {
      return t("dateDiffLabelDateInHours")?.replace("${num}", `${diffInHours}`);
    }
    const diffInMinutes = differenceInMinutes(new Date(), new Date(dateToUse));
    if (diffInMinutes != 0) {
      return t("dateDiffLabelDateInMinutes")?.replace("${num}", `${diffInMinutes}`);
    }
    return t("dateDiffLabelDateJustNow");
  }, [t, dateToUse]);


  return (
    <tr key={material.materialId}
        className={styles.container} onClick={() => {
      material.materialType == "FOLDER" && cdFolder(material.materialId);
    }}>
      <td>
        <div className={styles.materialName}>
          {material.materialType == "FOLDER" ?
            <LuFolder size={16} className={styles.icon} /> : <LuFile size={16} className={styles.icon} />}
          <span className={"line-clamp"}>
          {material.name}
        </span>
        </div>
      </td>
      <td className={styles.infoColumn} data-tooltip={fullDate}>{dateDiff}</td>
      <td className={styles.infoColumn}>{material.media?.contentType ?? " - "}</td>
      <td className={styles.infoColumn}>{humanReadibleFileSize(material.media?.size)}</td>
    </tr>
  );
};

export default MaterialViewRow;