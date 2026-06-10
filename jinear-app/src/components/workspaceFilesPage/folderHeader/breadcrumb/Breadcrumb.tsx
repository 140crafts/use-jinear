import React, {type DragEvent} from "react";
import styles from "./Breadcrumb.module.css";
import type {PathAwareMaterialDto} from "@/be/jinear-core";
import Button, {ButtonHeight} from "@/components/button";
import Logger from "@/util/logger";
import useTranslation from "@/locals/useTranslation";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil";
import {LuHouse} from "react-icons/lu";

interface BreadcrumbProps {
    container?: PathAwareMaterialDto;
    onClick: (materialId?: string) => void;
    onBreadcrumbDrop?: (breadCrumbMaterialId?: string) => void;
    hideBreadcrumbs?: boolean;
}

const logger = Logger("Breadcrumb");

const Breadcrumb: React.FC<BreadcrumbProps> = ({container, onClick, onBreadcrumbDrop, hideBreadcrumbs = false}) => {
    const {t} = useTranslation();
    const materialPath = container?.materialPath;

    const onDragOver = (e: DragEvent<HTMLDivElement>) => e.preventDefault();
    const onDragEnter = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        e.currentTarget.dataset.dragover = "true";
    };
    const onDragLeave = (e: DragEvent<HTMLDivElement>) => {
        if (!e.currentTarget.contains(e.relatedTarget as Node)) {
            delete e.currentTarget.dataset.dragover;
        }
    };
    const onDrop = (e: DragEvent<HTMLDivElement>, materialId?: string) => {
        delete e.currentTarget.dataset.dragover;
        onBreadcrumbDrop?.(materialId);
    };

    return (
        <div className={styles.container}>
            {!hideBreadcrumbs &&
                <>
                    <div className={styles.breadcrumb}
                         onDragOver={onDragOver}
                         onDragEnter={onDragEnter}
                         onDragLeave={onDragLeave}
                         onDrop={onDrop}>
                        <Button
                            heightVariant={ButtonHeight.short}
                            onClick={() => onClick()}
                            className={styles.button}>
                            <LuHouse className={"icon"}/>
                            <span>{t("folderBreadcrumbHome")}</span>
                        </Button>
                        {(materialPath?.path?.length ?? 0) > 0 && <span>/</span>}
                    </div>

                    {materialPath?.path?.map((path, index) =>
                        <div
                            key={`breadcrumb-button-${path.materialId}`}
                            className={styles.breadcrumb}
                            onDragOver={onDragOver}
                            onDragEnter={onDragEnter}
                            onDragLeave={onDragLeave}
                            onDrop={(e) => onDrop(e, path.materialId)}
                        >
                            <Button
                                heightVariant={ButtonHeight.short2x}
                                onClick={() => onClick(path.materialId)}
                                className={styles.button}
                            >
                                {shortenStringIfMoreThanMaxLength({text: path.name, maxLength: 16})}
                            </Button>
                            {materialPath?.path?.[index + 1] && <span>/</span>}
                        </div>
                    )}
                </>}
        </div>
    );
};

export default Breadcrumb;