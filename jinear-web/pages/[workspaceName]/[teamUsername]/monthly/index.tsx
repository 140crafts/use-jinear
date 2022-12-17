import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import MonthlyPlanTab from "@/components/teamMonthlyScreen/monthlyPlanTab/MonthlyPlanTab";
import MonthYearNo from "@/components/teamMonthlyScreen/monthYearNo/MonthYearNo";
import TeamMonthlyScreenBreadcrumb from "@/components/teamMonthlyScreen/teamMonthlyScreenBreadcrumb/TeamMonthlyScreenBreadcrumb";
import WorkflowStatusTab from "@/components/workflowStatusTab/WorkflowStatusTab";
import TeamMonthlyScreenContext from "@/store/context/screen/team/monthly/teamMonthlyScreenContext";
import {
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import {
  endOfMonth,
  endOfWeek,
  startOfMonth,
  startOfToday,
  startOfWeek,
} from "date-fns";
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
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  const currentWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const preferredTeamId = useTypedSelector(
    selectCurrentAccountsPreferredTeamId
  );

  const today = startOfToday();
  const [viewingPeriodOf, setViewingPeriodOf] = useState<Date>(today);
  const [viewingPeriodStart, setViewingPeriodStart] = useState<Date>(
    startOfWeek(startOfMonth(viewingPeriodOf), { weekStartsOn: 1 })
  );
  const [viewingPeriodEnd, setViewingPeriodEnd] = useState<Date>(
    endOfWeek(endOfMonth(viewingPeriodOf), { weekStartsOn: 1 })
  );

  useEffect(() => {
    if (viewingPeriodOf) {
      setViewingPeriodStart(
        startOfWeek(startOfMonth(viewingPeriodOf), { weekStartsOn: 1 })
      );
      setViewingPeriodEnd(
        endOfWeek(endOfMonth(viewingPeriodOf), { weekStartsOn: 1 })
      );
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
        <TeamMonthlyScreenBreadcrumb
          workspaceName={workspaceName}
          teamUsername={teamUsername}
        />
        <MonthYearNo />

        <div className="spacer-h-2" />

        <TabbedPanel
          initialTabName="workflow"
          containerClassName={styles.weekViewContainer}
        >
          <TabView
            name="workflow"
            label={t("teamWeeklyScreenTaskWorkflowStatusSectionTitle")}
            containerClassName={styles.tabViewContainer}
          >
            <WorkflowStatusTab
              teamId={preferredTeamId}
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
              teamId={preferredTeamId}
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
