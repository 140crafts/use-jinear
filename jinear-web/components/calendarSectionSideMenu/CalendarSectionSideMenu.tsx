import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import OrLine from "../orLine/OrLine";
import styles from "./CalendarSectionSideMenu.module.css";
import CalendarTeamsList from "./calendarTeamsList/CalendarTeamsList";
import ExternalCalendarsList from "./externalCalendarsList/ExternalCalendarsList";

interface CalendarSectionSideMenuProps {}

const CalendarSectionSideMenu: React.FC<CalendarSectionSideMenuProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const params = useParams();
  const workspaceName = (params?.workspaceName as string) || "";
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data?.find((team) => team);

  return (
    <div className={styles.container}>
      {workspace && (
        <>
          <ExternalCalendarsList workspace={workspace} />
          <OrLine omitText={true} />
          <CalendarTeamsList workspace={workspace} />
        </>
      )}
    </div>
  );
};

export default CalendarSectionSideMenu;
