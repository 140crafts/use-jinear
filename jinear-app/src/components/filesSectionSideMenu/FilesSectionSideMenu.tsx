import React from "react";
import styles from "./FilesSectionSideMenu.module.scss";
import {useTypedSelector} from "@/store";
import {selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "@/locals/useTranslation";
import Button, {ButtonVariants} from "@/components/button";
import {LuCamera, LuFileText, LuHaze,  LuHouse, LuSendToBack, LuTrash} from "react-icons/lu";
import TeamTaskFiles from "@/components/filesSectionSideMenu/teamTaskFiles/TeamTaskFiles";
import {useQueryState, useSetQueryStateMultiple} from "@/hooks/useQueryState";
import Logger from "@/util/logger";
import type {MaterialSearchContentFilterType, MaterialSearchSortType} from "@/be/jinear-core";
import MediaLimitUsage from "@/components/filesSectionSideMenu/mediaLimitUsage/MediaLimitUsage";
import {useParams} from "react-router-dom";

interface FilesSectionSideMenuProps {
    containerClassName?: string;
}

const logger = Logger("FilesSectionSideMenu");

const FilesSectionSideMenu: React.FC<FilesSectionSideMenuProps> = ({containerClassName}) => {
    const {t} = useTranslation();
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    const setQueryStateMultiple = useSetQueryStateMultiple();
    const parentMaterialId = useQueryState<string>("parentMaterialId");
    const materialSearchSortType = useQueryState<MaterialSearchSortType | null>("materialSearchSortType");
    const materialSearchContentFilterType = useQueryState<MaterialSearchContentFilterType | null>("materialSearchContentFilterType");

    const isInRoot = parentMaterialId == null && materialSearchSortType == null;
    const isInRecent = parentMaterialId == null && materialSearchSortType == "IDATE_DESC" && materialSearchContentFilterType == null;
    const isInImages = parentMaterialId == null && materialSearchContentFilterType == "IMAGE";
    const isInDocuments = parentMaterialId == null && materialSearchContentFilterType == "DOC";
    const isInShared = parentMaterialId == null && materialSearchContentFilterType == "SHARED";
    const isInDeleted = parentMaterialId == null && materialSearchContentFilterType == "RECENTLY_DELETED";

    logger.log({parentMaterialId, materialSearchSortType});

    const cd = (
        parentMaterialId?: string,
        materialSearchSortType?: MaterialSearchSortType,
        materialSearchContentFilterType?: MaterialSearchContentFilterType,
        taskFilesTeamId?: string) => {
        setQueryStateMultiple(
            new Map([
                ["parentMaterialId", parentMaterialId],
                ["materialSearchSortType", materialSearchSortType],
                ["materialSearchContentFilterType", materialSearchContentFilterType],
                ["taskFilesTeamId", taskFilesTeamId]
            ])
        );
    };

    const cdRoot = () => {
        cd(undefined, undefined, undefined, undefined);
    };

    const cdRecent = () => {
        cd(undefined, "IDATE_DESC", undefined, undefined);
    };

    const cdImages = () => {
        cd(undefined, "IDATE_DESC", "IMAGE", undefined);
    };

    const cdDocuments = () => {
        cd(undefined, "IDATE_DESC", "DOC", undefined);
    };

    const cdShared = () => {
        cd(undefined, "IDATE_DESC", "SHARED", undefined);
    };

    const cdDeleted = () => {
        cd(undefined, "IDATE_DESC", "RECENTLY_DELETED", undefined);
    };

    const cdTeamTaskFiles = (teamId: string) => {
        cd(undefined, undefined, undefined, teamId);
    };

    const clearFilters = () => {
        cd(undefined, undefined, undefined, undefined);
    };

    return (
        <div className={styles.container}>
            {workspace && (
                <>
                    <MenuGroupTitle label={t("sideMenuFilesTitle")}/>
                    <div className={styles.buttonsContainer}>

                        {/*<Button*/}
                        {/*  heightVariant={ButtonHeight.short}*/}
                        {/*  variant={ButtonVariants.brandColor}*/}
                        {/*  className={styles.newTaskButton}*/}
                        {/*>*/}
                        {/*  <LuPlus className={"icon"} />*/}
                        {/*  <b>{t("sideMenuFilesUpload")}</b>*/}
                        {/*</Button>*/}

                        <Button
                            className={styles.button}
                            onClick={cdRoot}
                            variant={isInRoot ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                        >
                            <LuHouse className={"icon"}/>
                            {t("sideMenuFilesAllFiles")}
                        </Button>

                        <Button
                            className={styles.button}
                            onClick={cdRecent}
                            variant={isInRecent ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                        >
                            <LuHaze className={"icon"}/>
                            {t("sideMenuFilesRecentFiles")}
                        </Button>

                        <Button
                            className={styles.button}
                            onClick={cdImages}
                            variant={isInImages ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                        >
                            <LuCamera className={"icon"}/>
                            {t("sideMenuFilesImageFiles")}
                        </Button>

                        <Button
                            className={styles.button}
                            onClick={cdDocuments}
                            variant={isInDocuments ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                        >
                            <LuFileText className={"icon"}/>
                            {t("sideMenuFilesDocumentFiles")}
                        </Button>

                        <Button
                            className={styles.button}
                            onClick={cdShared}
                            variant={isInShared ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                        >
                            <LuSendToBack className={"icon"}/>
                            {t("sideMenuSharedFiles")}
                        </Button>

                        {/*<Button*/}
                        {/*  className={styles.button}*/}
                        {/*  onClick={cdDeleted}*/}
                        {/*  variant={isInDeleted ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}*/}
                        {/*>*/}
                        {/*  <LuTrash className={"icon"} />*/}
                        {/*  {t("sideMenuDeletedFiles")}*/}
                        {/*</Button>*/}
                    </div>

                    <TeamTaskFiles workspaceId={workspace.workspaceId} onTeamClick={cdTeamTaskFiles}/>

                    <MediaLimitUsage workspace={workspace}/>
                </>
            )}
        </div>);
};

export default FilesSectionSideMenu;