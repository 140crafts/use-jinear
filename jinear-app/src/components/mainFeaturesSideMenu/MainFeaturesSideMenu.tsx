import type {AccountsWorkspacePerspectiveDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {LuCalendarDays, LuFolder, LuNotebook, LuSquareCheckBig} from "react-icons/lu";
import Button, {ButtonVariants} from "../button";
import styles from "./MainFeaturesSideMenu.module.scss";
import Logger from "@/util/logger";
import {useLocation} from "react-router-dom";
import {useFeatureFlag} from "@/hooks/useFeatureFlag.ts";

interface MainFeaturesSideMenuProps {
    workspace: AccountsWorkspacePerspectiveDto;
}

const logger = Logger("MainFeaturesSideMenu");

const MainFeaturesSideMenu: React.FC<MainFeaturesSideMenuProps> = ({workspace}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();
    const calendarPath = `/${workspace?.username}/calendar`;
    const tasksPath = `/${workspace?.username}/tasks`;
    const filesPath = `/${workspace?.username}/files`;
    const notesPath = `/${workspace?.username}/notebook`;
    const notesEnabled = useFeatureFlag("NOTES");

    return (
        <div className={styles.container}>

            <Button
                className={styles.iconButton}
                href={calendarPath}
                variant={pathname?.indexOf(calendarPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuCalendarDays className={styles.icon}/>
                {t("mainFeaturesMenuLabelCalendar")}
            </Button>

            <Button
                className={styles.iconButton}
                href={tasksPath}
                variant={pathname?.indexOf(tasksPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuSquareCheckBig className={styles.icon}/>
                {t("mainFeaturesMenuLabelTasks")}
            </Button>

            {notesEnabled &&
                <Button
                className={styles.iconButton}
                href={notesPath}
                variant={pathname?.indexOf(notesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuNotebook className={styles.icon}/>
                {t("mainFeaturesMenuLabelNotes")}
            </Button>}

            <Button
                className={styles.iconButton}
                href={filesPath}
                variant={pathname?.indexOf(filesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuFolder className={styles.icon}/>
                {t("mainFeaturesMenuLabelFiles")}
            </Button>
        </div>
    )
        ;
};

export default MainFeaturesSideMenu;
