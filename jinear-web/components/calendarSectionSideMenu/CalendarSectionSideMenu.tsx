import {
  queryStateDateToShortDateConverter,
  queryStateShortDateParser,
  useQueryState,
  useSetQueryState,
} from "@/hooks/useQueryState";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import MiniMonthCalendar from "../miniMonthCalendar/MiniMonthCalendar";
import OrLine from "../orLine/OrLine";
import styles from "./CalendarSectionSideMenu.module.css";
import CalendarTeamsList from "./calendarTeamsList/CalendarTeamsList";
import ExternalCalendarsList from "./externalCalendarsList/ExternalCalendarsList";

interface CalendarSectionSideMenuProps {}

const CalendarSectionSideMenu: React.FC<CalendarSectionSideMenuProps> = ({}) => {
  const setQueryState = useSetQueryState();
  const params = useParams();
  const workspaceName = (params?.workspaceName as string) || "";
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser);

  const changeViewingDate = (day?: Date) => {
    if (day) {
      setQueryState("viewingDate", queryStateDateToShortDateConverter(day));
    }
  };

  return (
    <div className={styles.container}>
      {workspace && (
        <>
          <MiniMonthCalendar
            value={viewingDate || undefined}
            setValue={changeViewingDate}
            dayButtonClassName={styles.dayButtonClassName}
            showYearPicker={true}
            showLabel={true}
            headerContainerClassName={styles.miniMonthCalendarHeader}
          />
          <ExternalCalendarsList workspace={workspace} />
          <OrLine omitText={true} />
          <CalendarTeamsList workspace={workspace} />
        </>
      )}
    </div>
  );
};

export default CalendarSectionSideMenu;
