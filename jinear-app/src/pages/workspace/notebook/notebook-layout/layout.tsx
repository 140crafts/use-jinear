import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import {Outlet} from "react-router-dom";
import {LuFileText} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";
import NotebookSectionSideMenu from "@/components/notebookSectionSideMenu/NotebookSectionSideMenu.tsx";

interface NotesLayoutProps {
}

const NotesLayout: React.FC<NotesLayoutProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div id="notes-layout-container" className={styles.container}>
            <SecondLevelSideMenuV2
                mobileFabButtonIcon={<LuFileText className={"icon"} size={18}/>}
                mobileFabButtonText={t('mobileFabButtonNotes')}
            >
                <NotebookSectionSideMenu/>
            </SecondLevelSideMenuV2>
            <div
                id="notes-layout-content"
                className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
            >
                <Outlet/>
            </div>
        </div>
    );
};

export default NotesLayout;
