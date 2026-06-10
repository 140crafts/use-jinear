import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

import CalendarSectionSideMenu from "@/components/calendarSectionSideMenu/CalendarSectionSideMenu";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import {Outlet} from "react-router-dom";

interface TasksLayoutProps {
}

const CalendarLayout: React.FC<TasksLayoutProps> = ({}) => {

    return (
        <div id="calendar-layout-container" className={styles.container}>
            <SecondLevelSideMenuV2>
                <CalendarSectionSideMenu/>
            </SecondLevelSideMenuV2>
            <div
                id="calendar-layout-content"
                className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
            >
                <Outlet/>
            </div>
        </div>
    );
};

export default CalendarLayout;
