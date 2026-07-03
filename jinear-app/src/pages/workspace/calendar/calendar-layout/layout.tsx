import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

import CalendarSectionSideMenu from "@/components/calendarSectionSideMenu/CalendarSectionSideMenu";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import {Outlet} from "react-router-dom";
import {LuCalendarFold} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";

interface TasksLayoutProps {
}

const CalendarLayout: React.FC<TasksLayoutProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div id="calendar-layout-container" className={styles.container}>
            <SecondLevelSideMenuV2
                mobileFabButtonIcon={<LuCalendarFold className={"icon"} size={18}/>}
                mobileFabButtonText={t('mobileFabButtonCalendar')}
            >
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
