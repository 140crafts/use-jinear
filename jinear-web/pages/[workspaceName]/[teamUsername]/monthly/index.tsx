import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import MonthlyPlanTab from "@/components/teamMonthlyScreen/monthlyPlanTab/MonthlyPlanTab";
import MonthYearNo from "@/components/teamMonthlyScreen/monthYearNo/MonthYearNo";
import TeamMonthlyScreenBreadcrumb from "@/components/teamMonthlyScreen/teamMonthlyScreenBreadcrumb/TeamMonthlyScreenBreadcrumb";
import WorkflowStatusTab from "@/components/workflowStatusTab/WorkflowStatusTab";
import TeamMonthlyScreenContext from "@/store/context/screen/team/monthly/teamMonthlyScreenContext";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { endOfMonth, endOfWeek, startOfMonth, startOfToday } from "date-fns";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect, useRef, useState } from "react";
import styles from "./index.module.scss";

interface TeamMonthlyScreenProps {}

const logger = Logger("TeamMonthlyScreen");

const TeamMonthlyScreen: React.FC<TeamMonthlyScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const monthlyPlanTabContainerRef = useRef<HTMLDivElement>(null);

  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const today = startOfToday();
  const [viewingPeriodOf, setViewingPeriodOf] = useState<Date>(today);
  const [viewingPeriodStart, setViewingPeriodStart] = useState<Date>(startOfMonth(viewingPeriodOf));
  const [viewingPeriodEnd, setViewingPeriodEnd] = useState<Date>(endOfMonth(viewingPeriodOf));

  useEffect(() => {
    if (viewingPeriodOf) {
      setViewingPeriodStart(startOfMonth(viewingPeriodOf));
      setViewingPeriodEnd(endOfMonth(viewingPeriodOf));
    }
  }, [viewingPeriodOf]);

  return (
    <TeamMonthlyScreenContext.Provider
      value={{
        viewingPeriodOf,
        viewingPeriodStart,
        viewingPeriodEnd,
        setViewingPeriodOf,
      }}
    >
      <div className={styles.container}>
        {!currentWorkspace?.isPersonal && (
          <TeamMonthlyScreenBreadcrumb
            workspaceName={currentWorkspace?.title || ""}
            workspaceUsername={currentWorkspace?.username || ""}
            teamName={preferredTeam?.name}
            teamUsername={preferredTeam?.username}
          />
        )}
        <MonthYearNo />

        <div className="spacer-h-2" />

        <TabbedPanel initialTabName="workflow" containerClassName={styles.weekViewContainer}>
          <TabView
            name="workflow"
            label={t("teamWeeklyScreenTaskWorkflowStatusSectionTitle")}
            containerClassName={styles.tabViewContainer}
          >
            <WorkflowStatusTab
              teamId={preferredTeam?.teamId}
              workspaceId={currentWorkspace?.workspaceId}
              startDate={viewingPeriodStart}
              endDate={endOfWeek(viewingPeriodEnd, { weekStartsOn: 1 })}
            />
          </TabView>

          <TabView
            name="plan"
            label={t("teamMonthlyScreenPeriodPlanSectionTitle")}
            containerClassName={styles.tabViewContainer}
            containerRef={monthlyPlanTabContainerRef}
          >
            <MonthlyPlanTab
              containerRef={monthlyPlanTabContainerRef}
              teamId={preferredTeam?.teamId}
              workspaceId={currentWorkspace?.workspaceId}
              viewingPeriodStart={viewingPeriodStart}
              viewingPeriodEnd={viewingPeriodEnd}
            />
          </TabView>
        </TabbedPanel>
      </div>
    </TeamMonthlyScreenContext.Provider>
  );
};

export default TeamMonthlyScreen;
