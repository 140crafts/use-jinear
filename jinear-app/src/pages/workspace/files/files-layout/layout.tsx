import React from "react";
import styles from "./layout.module.scss";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import cn from "classnames";
import FilesSectionSideMenu from "@/components/filesSectionSideMenu/FilesSectionSideMenu";
import {Outlet} from "react-router-dom";

interface FilesLayoutProps {

}

const FilesLayout: React.FC<FilesLayoutProps> = ({}) => {

    return (
        <div id="files-layout-container" className={styles.container}>
            <SecondLevelSideMenuV2>
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