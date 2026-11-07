import type {AccountsWorkspacePerspectiveDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {LuCalendarDays, LuFolder, LuNotebook, LuSearch, LuShieldCheck, LuSquareCheckBig} from "react-icons/lu";
import Button, {ButtonVariants} from "../button";
import styles from "./MainFeaturesSideMenu.module.scss";
import Logger from "@/util/logger";
import {useLocation} from "react-router-dom";
import useIsInstanceAdmin from "@/hooks/useIsInstanceAdmin";
import {useAppDispatch} from "@/store";
import {popGlobalSearchModal} from "@/slice/modalSlice";

interface MainFeaturesSideMenuProps {
    workspace: AccountsWorkspacePerspectiveDto;
}

const logger = Logger("MainFeaturesSideMenu");

const MainFeaturesSideMenu: React.FC<MainFeaturesSideMenuProps> = ({workspace}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();
    const dispatch = useAppDispatch();
    const calendarPath = `/${workspace?.username}/calendar`;
    const tasksPath = `/${workspace?.username}/tasks`;
    const filesPath = `/${workspace?.username}/files`;
    const notesPath = `/${workspace?.username}/notebook`;
    const instanceSettingsPath = `/admin/instance-settings`;

    const isInstanceAdmin = useIsInstanceAdmin();

    const popSearch = () => {
        dispatch(popGlobalSearchModal({visible: true, workspaceId: workspace.workspaceId, workspaceUsername: workspace.username}));
    };

    return (
        <div className={styles.container}>

            <Button
                className={styles.iconButton}
                href={calendarPath}
                shortcut="navCalendar"
                variant={pathname?.indexOf(calendarPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuCalendarDays className={styles.icon}/>
                {t("mainFeaturesMenuLabelCalendar")}
            </Button>

            <Button
                className={styles.iconButton}
                href={tasksPath}
                shortcut="navTasks"
                variant={pathname?.indexOf(tasksPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuSquareCheckBig className={styles.icon}/>
                {t("mainFeaturesMenuLabelTasks")}
            </Button>

            <Button
                className={styles.iconButton}
                href={notesPath}
                shortcut="navNotes"
                variant={pathname?.indexOf(notesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuNotebook className={styles.icon}/>
                {t("mainFeaturesMenuLabelNotes")}
            </Button>

            <Button
                className={styles.iconButton}
                href={filesPath}
                shortcut="navFiles"
                variant={pathname?.indexOf(filesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuFolder className={styles.icon}/>
                {t("mainFeaturesMenuLabelFiles")}
            </Button>

            <Button
                className={styles.iconButton}
                onClick={popSearch}
                shortcut="search"
                variant={ButtonVariants.hoverFilled2}
            >
                <LuSearch className={styles.icon}/>
                {t("mainFeaturesMenuLabelSearch")}
            </Button>

            {isInstanceAdmin && (
                <Button
                    className={styles.iconButton}
                    href={instanceSettingsPath}
                    shortcut="navInstanceSettings"
                    variant={pathname?.indexOf(instanceSettingsPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuShieldCheck className={styles.icon}/>
                    {t("mainFeaturesMenuLabelInstanceSettings")}
                </Button>
            )}
        </div>
    )
        ;
};

export default MainFeaturesSideMenu;
