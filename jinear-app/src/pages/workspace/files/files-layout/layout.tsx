import React from "react";
import styles from "./layout.module.scss";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import cn from "classnames";
import FilesSectionSideMenu from "@/components/filesSectionSideMenu/FilesSectionSideMenu";
import {Outlet} from "react-router-dom";
import {LuCalendarFold, LuFolders} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";

interface FilesLayoutProps {

}

const FilesLayout: React.FC<FilesLayoutProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div id="files-layout-container" className={styles.container}>
            <SecondLevelSideMenuV2
                mobileFabButtonIcon={<LuFolders className={"icon"} size={18}/>}
                mobileFabButtonText={t('mobileFabButtonFiles')}
            >
                <FilesSectionSideMenu/>
            </SecondLevelSideMenuV2>
            <div
                id="files-layout-content"
                className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
            >
                <Outlet/>
            </div>
        </div>
    );
};

export default FilesLayout;