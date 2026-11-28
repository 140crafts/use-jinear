import React, {useEffect, useState} from "react";
import cn from "classnames";
import styles from "./MaterialAccessUpdate.module.css";
import type {MaterialAccessType, MaterialDto} from "@/be/jinear-core";
import useTranslation, {type StringKeys} from "@/locals/useTranslation";
import {
    ACCESS_TYPE_ICON_MAP,
    getMaterialIcon
} from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import {LuCheck, LuFolder} from "react-icons/lu";
import {useUpdateMaterialAccessTypeMutation} from "@/api/materialOperationApi";
import MaterialAccessList from "@/components/modal/materialAccessModal/materialAccessList/MaterialAccessList";
import Button, {ButtonVariants} from "@/components/button";
import {useTypedSelector} from "@/store";
import {selectMaterialAccessModalResetList} from "@/slice/modalSlice";
import {selectCurrentAccountId} from "@/slice/accountSlice";
import {toast} from "react-hot-toast";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import GradientLine from "@/components/gradientLine/GradientLine";
import {humanReadibleFileSize} from "@/util/FileSizeFormatter";
import {calculateDateDiff} from "@/util/DateHelper";
import {format} from "date-fns";

interface MaterialAccessUpdateProps {
    material: MaterialDto;
}

interface AccessTypeOption {
    value: MaterialAccessType;
    labelKey: StringKeys;
    detailKey: StringKeys;
}

const ACCESS_TYPE_OPTIONS: AccessTypeOption[] = [
    {value: "OWNER_ONLY", labelKey: "materialListRowAccessOnlyYouTooltip", detailKey: "materialAccessType_OWNER_ONLY_info"},
    {value: "WORKSPACE_MEMBERS", labelKey: "materialListRowAccessWorkspaceMembersTooltip", detailKey: "materialAccessType_WORKSPACE_MEMBERS_info"},
    {value: "GRAINED", labelKey: "materialListRowAccessGrainedTooltip", detailKey: "materialAccessType_GRAINED_info"},
    {value: "ANYONE_WITH_LINK", labelKey: "materialListRowAccessAnyoneWithTheLinkTooltip", detailKey: "materialAccessType_ANYONE_WITH_LINK_info"}
];

const MaterialAccessUpdate: React.FC<MaterialAccessUpdateProps> = ({material}) => {
    const {t} = useTranslation();
    const materialId = material.materialId;
    const currentAccountId = useTypedSelector(selectCurrentAccountId);
    const [updateMaterialAccessType, {
        isLoading: isUpdateMaterialAccessTypeLoading,
        isSuccess: isUpdateMaterialAccessTypeSuccess
    }] = useUpdateMaterialAccessTypeMutation();
    const [materialAccessType, setMaterialAccessType] = useState(material.materialAccessType);
    const resetList = useTypedSelector(selectMaterialAccessModalResetList);
    const accessUpdatedMessage = t("materialAccessUpdated");
    const viewerIsOwner = material.ownerId == currentAccountId;

    const fileIcon = material.materialType == "FOLDER" ? LuFolder : getMaterialIcon(material.media?.contentType ?? "default");
    const fileMeta = [
        humanReadibleFileSize(material.media?.size),
        calculateDateDiff(material.createdDate),
        material.owner?.username
    ].filter(Boolean).join(" · ");
    const createdFullDate = format(new Date(material.createdDate), t("dateTimeFormat"));
    const visibleOptions = ACCESS_TYPE_OPTIONS.filter(option => material.materialType != "FOLDER" || option.value != "ANYONE_WITH_LINK");
    const showMemberList = material.materialAccessType == "GRAINED" && materialAccessType == "GRAINED";

    const handleAccessTypeChange = (value: MaterialAccessType) => {
        if (value == materialAccessType) {
            return;
        }
        setMaterialAccessType(value);
        updateMaterialAccessType({materialId, accessType: value});
        resetList?.();
    };

    const getLabel = (option: AccessTypeOption) =>
        option.value == "OWNER_ONLY" && !viewerIsOwner ? t("materialAccessTypeOwnerOnlyLabel") : t(option.labelKey);

    const getDetail = (option: AccessTypeOption) =>
        option.value == "OWNER_ONLY" && !viewerIsOwner ?
            t("materialAccessType_OWNER_ONLY_info_SpesificUser").replace("{user}", material.owner?.username ?? "") :
            t(option.detailKey);

    useEffect(() => {
        if (isUpdateMaterialAccessTypeSuccess) {
            toast(accessUpdatedMessage);
        }
    }, [isUpdateMaterialAccessTypeSuccess, accessUpdatedMessage]);

    return (
        <div className={styles.container}>
            <div className={styles.fileCard}>
                <div className={styles.fileIconTile}>
                    {React.createElement(fileIcon, {size: 14})}
                </div>
                <div className={styles.fileInfo}>
                    <b className="single-line">{material.name}</b>
                    <span className={cn(styles.fileMeta, "single-line")} data-tooltip-right={createdFullDate}>{fileMeta}</span>
                </div>
            </div>

            <div className={styles.section}>
                <div className={styles.sectionTitle}>
                    <h3>{t("materialAccessTypeSectionTitle")}</h3>
                    <GradientLine/>
                </div>
                <div className={styles.options}>
                    {visibleOptions.map((option) => {
                        const isSelected = materialAccessType == option.value;
                        return (
                            <Button
                                key={option.value}
                                variant={isSelected ? ButtonVariants.filled : ButtonVariants.default}
                                className={styles.option}
                                onClick={() => handleAccessTypeChange(option.value)}
                            >
                                {React.createElement(ACCESS_TYPE_ICON_MAP[option.value], {size: 16, className: styles.optionIcon})}
                                <div className={styles.optionText}>
                                    <b>{getLabel(option)}</b>
                                    <span className={styles.optionDetail}>{getDetail(option)}</span>
                                </div>
                                <div className={styles.optionState}>
                                    {isSelected && (isUpdateMaterialAccessTypeLoading ? <CircularLoading/> : <LuCheck/>)}
                                </div>
                            </Button>
                        );
                    })}
                </div>
            </div>

            {showMemberList &&
                <div className={styles.section}>
                    <div className={styles.sectionTitle}>
                        <h3>{t("materialAccessListMembersTitle")}</h3>
                        <GradientLine/>
                    </div>
                    <MaterialAccessList materialId={materialId} workspaceId={material.workspaceId} ownerId={material.ownerId}/>
                </div>}
        </div>
    );
};

export default MaterialAccessUpdate;
