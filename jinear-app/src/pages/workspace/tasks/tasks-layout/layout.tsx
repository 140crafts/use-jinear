import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";
import TasksSectionSideMenu from "@/components/tasksSectionSideMenu/TasksSectionSideMenu";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import {Outlet} from "react-router-dom";
import {LuCopyCheck, LuFolders} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";

interface TasksLayoutProps {
}

const TasksLayout: React.FC<TasksLayoutProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div id="tasks-layout-container" className={styles.container}>
            <SecondLevelSideMenuV2
                mobileFabButtonIcon={<LuCopyCheck className={"icon"} size={18}/>}
                mobileFabButtonText={t('mobileFabButtonTasks')}
            >
                <TasksSectionSideMenu/>
            </SecondLevelSideMenuV2>
            <div
                id="tasks-layout-content"
                className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
            >
                <Outlet/>
            </div>
        </div>
    );
};

export default TasksLayout;
