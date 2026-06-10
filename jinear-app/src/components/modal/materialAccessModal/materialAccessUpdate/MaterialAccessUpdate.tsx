import React, {useEffect, useState} from "react";
import styles from "./MaterialAccessUpdate.module.css";
import type {MaterialAccessType, MaterialDto} from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import {
    ACCESS_TYPE_ICON_MAP,
    getMaterialIcon
} from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import {LuFolder} from "react-icons/lu";
import cn from "classnames";
import {useUpdateMaterialAccessTypeMutation} from "@/api/materialOperationApi";
import MaterialAccessList from "@/components/modal/materialAccessModal/materialAccessList/MaterialAccessList";
import Logger from "@/util/logger";
import Button, {ButtonVariants} from "@/components/button";
import {useTypedSelector} from "@/store";
import {selectMaterialAccessModalResetList} from "@/slice/modalSlice";
import {toast} from "react-hot-toast";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface MaterialAccessUpdateProps {
    material: MaterialDto;
}

const ACCESS_TYPE_OPTIONS: { value: MaterialAccessType; labelKey: string }[] = [
    {value: "OWNER_ONLY", labelKey: "materialAccessType_OWNER_ONLY_info"},
    {value: "WORKSPACE_MEMBERS", labelKey: "materialAccessType_WORKSPACE_MEMBERS_info"},
    {value: "GRAINED", labelKey: "materialAccessType_GRAINED_info"},
    {value: "ANYONE_WITH_LINK", labelKey: "materialAccessType_ANYONE_WITH_LINK_info"}
];

const logger = Logger("MaterialAccessUpdate");

const MaterialAccessUpdate: React.FC<MaterialAccessUpdateProps> = ({material}) => {
    const {t} = useTranslation();
    const materialId = material.materialId;
    const [updateMaterialAccessType, {
        isLoading: isUpdateMaterialAccessTypeLoading,
        isSuccess: isUpdateMaterialAccessTypeSuccess
    }] = useUpdateMaterialAccessTypeMutation();
    const [materialAccessType, setMaterialAccessType] = useState(material.materialAccessType);
    const resetList = useTypedSelector(selectMaterialAccessModalResetList);

    const FileIcon = material.materialType == "FOLDER" ? LuFolder : getMaterialIcon(material.media?.contentType ?? "default");

    const handleAccessTypeChange = (value: MaterialAccessType) => {
        setMaterialAccessType(value);
        updateMaterialAccessType({materialId, accessType: value});
        resetList?.();
    };

    useEffect(() => {
        isUpdateMaterialAccessTypeSuccess && toast(t("materialAccessUpdated"));
    }, [isUpdateMaterialAccessTypeSuccess]);

    return (
        <div className={styles.container}>
            <div className={styles.titleContainer}>
                <FileIcon size={32} style={{minWidth: 32}}/>
                <h1 className={cn("line-clamp", styles.title)}>{material?.name}</h1>
            </div>

            <div className={styles.accessTypeSelectContainer}>
                <h3>{t("materialAccessListMembersTitle")}</h3>
                <div className={styles.accessTypeButtons}>
                    {ACCESS_TYPE_OPTIONS.map((option) => {
                        const Icon = ACCESS_TYPE_ICON_MAP[option.value];
                        const isSelected = materialAccessType === option.value;
                        return material.materialType === "FOLDER" && option.value == "ANYONE_WITH_LINK" ? null : (
                            <Button
                                key={option.value}
                                variant={isSelected ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
                                className={styles.accessTypeButton}
                                onClick={() => handleAccessTypeChange(option.value)}
                            >
                                <Icon size={20}/>
                                {/*@ts-ignore*/}
                                <span>{t(option.labelKey)}</span>
                            </Button>
                        );
                    })}
                </div>
            </div>

            {isUpdateMaterialAccessTypeLoading &&
                <div className={styles.loadingContainer}>
                    <CircularLoading size={15}/>
                    <span>{t("materialAccessSaving")}</span>
                </div>
            }

            {!isUpdateMaterialAccessTypeLoading && material.materialAccessType == "GRAINED" && materialAccessType == "GRAINED" &&
                <MaterialAccessList materialId={materialId} workspaceId={material.workspaceId}/>}

        </div>
    );
};

export default MaterialAccessUpdate;