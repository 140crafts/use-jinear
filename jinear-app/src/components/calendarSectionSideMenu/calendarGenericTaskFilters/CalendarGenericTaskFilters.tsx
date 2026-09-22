import React from "react";
import styles from "./CalendarGenericTaskFilters.module.css";
import useTranslation from "@/locales/useTranslation";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import CalendarBoardsList from "@/components/calendarSectionSideMenu/calendarBoardsList/CalendarBoardsList";
import CalendarAssigneesList from "@/components/calendarSectionSideMenu/calendarAssigneesList/CalendarAssigneesList";
import CalendarCollaboratorsList
    from "@/components/calendarSectionSideMenu/calendarCollaboratorsList/CalendarCollaboratorsList";

interface CalendarGenericTaskFiltersProps {
    workspace: WorkspaceDto;
}

const CalendarGenericTaskFilters: React.FC<CalendarGenericTaskFiltersProps> = ({workspace}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <div className="spacer-h-1"/>
            <div className={styles.titleContainer}>
                <MenuGroupTitle label={t("calendarFilterByPropertyLabel")} hasAddButton={false}/>
            </div>

            <div className={styles.filterListContainer}>
                <CalendarBoardsList workspace={workspace}/>
                <CalendarAssigneesList workspace={workspace}/>
                <CalendarCollaboratorsList workspace={workspace}/>
            </div>
        </div>
    );
};

export default CalendarGenericTaskFilters;
