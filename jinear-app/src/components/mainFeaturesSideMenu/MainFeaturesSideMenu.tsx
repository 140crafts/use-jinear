import type {AccountsWorkspacePerspectiveDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {LuCalendarDays, LuFolder, LuSquareCheckBig} from "react-icons/lu";
import Button, {ButtonVariants} from "../button";
import styles from "./MainFeaturesSideMenu.module.scss";
import InboxButton from "./inboxButton/InboxButton";
import Logger from "@/util/logger";
import {useLocation} from "react-router-dom";

interface MainFeaturesSideMenuProps {
    workspace: AccountsWorkspacePerspectiveDto;
}

const logger = Logger("MainFeaturesSideMenu");

const MainFeaturesSideMenu: React.FC<MainFeaturesSideMenuProps> = ({workspace}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();
    const calendarPath = `/${workspace?.username}/calendar`;
    const tasksButtonOpensPath = `/${workspace?.username}/tasks/last-activities`;
    const tasksPath = `/${workspace?.username}/tasks`;
    const inboxPath = `/${workspace?.username}/inbox`;
    const filesPath = `/${workspace?.username}/files`;

    return (
        <div className={styles.container}>
            <InboxButton
                isActive={inboxPath == pathname}
                workspace={workspace}
                buttonStyle={styles.iconButton}
                iconStyle={styles.icon}
            />

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
                href={tasksButtonOpensPath}
                variant={pathname?.indexOf(tasksPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <LuSquareCheckBig className={styles.icon}/>
                {t("mainFeaturesMenuLabelTasks")}
            </Button>

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
