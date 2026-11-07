import React from "react";
import type {MaterialDto} from "@/model/be/jinear-core";
import Button, {ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import {LuFolder} from "react-icons/lu";
import {getMaterialIcon} from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import {humanReadibleFileSize} from "@/util/FileSizeFormatter";
import {calculateDateDiff} from "@/util/DateHelper";
import {API_ROOT} from "@/util/constants";
import SearchHighlight from "../searchHighlight/SearchHighlight";
import styles from "./SearchResultRow.module.css";

interface MaterialSearchResultRowProps {
    material: MaterialDto;
    query: string;
    workspaceUsername?: string;
    isMobile: boolean;
    onSelect: () => void;
}

const MaterialSearchResultRow: React.FC<MaterialSearchResultRowProps> = ({material, query, workspaceUsername, isMobile, onSelect}) => {
    const {t} = useTranslation();
    const isFolder = material.materialType == "FOLDER";
    const materialIcon = isFolder ? LuFolder : getMaterialIcon(material.media?.contentType ?? "default");
    const location = material.parentMaterialId ? undefined : t("globalSearchModalRootFolder");
    const detail = isFolder ? t("globalSearchModalFolder") : humanReadibleFileSize(material.media?.size);
    const updatedAt = calculateDateDiff(material.lastUpdatedDate ?? material.createdDate);

    const openFile = () => {
        window.open(`${API_ROOT}v1/material/media/${material.materialId}`, isMobile ? undefined : "_blank");
        onSelect();
    };

    return (
        <Button
            className={styles.row}
            variant={ButtonVariants.hoverFilled2}
            href={isFolder ? `/${workspaceUsername}/files?parentMaterialId=${material.materialId}` : undefined}
            onClick={isFolder ? onSelect : openFile}
        >
            {React.createElement(materialIcon, {className: styles.icon})}
            <div className={styles.textContainer}>
                <div className={styles.title}>
                    <SearchHighlight text={material.name} query={query}/>
                </div>
                <div className={styles.subtitle}>
                    {isFolder && <span>{detail}</span>}
                    {location && <span>{location}</span>}
                    {!isFolder && detail && <span>{detail}</span>}
                    {updatedAt && <span>{updatedAt}</span>}
                </div>
            </div>
        </Button>
    );
};

export default MaterialSearchResultRow;
