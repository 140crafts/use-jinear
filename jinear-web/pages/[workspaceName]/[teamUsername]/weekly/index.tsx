import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import TeamWeeklyScreenBreadcrumb from "@/components/teamWeeklyScreen/teamWeeklyScreenBreadcrumb/TeamWeeklyScreenBreadcrumb";
import WeeklyPlanTab from "@/components/teamWeeklyScreen/weeklyPlanTab/WeeklyPlanTab";
import WorkflowStatusTab from "@/components/teamWeeklyScreen/workflowStatusTab/WorkflowStatusTab";
import YearWeekNo from "@/components/teamWeeklyScreen/yearWeekNo/YearWeekNo";
import TeamWeeklyScreenContext from "@/store/context/screen/team/weekly/teamWeeklyScreenContext";
import {
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { endOfWeek, startOfToday, startOfWeek } from "date-fns";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useState } from "react";
import styles from "./index.module.scss";

interface TeamWeeklyScreenProps {}

const TeamWeeklyScreen: React.FC<TeamWeeklyScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;
  const currentWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const preferredTeamId = useTypedSelector(
    selectCurrentAccountsPreferredTeamId
  );

  let today = startOfToday();

  const [viewingWeekStart, setViewingWeekStart] = useState<Date>(
    startOfWeek(today, { weekStartsOn: 1 })
  );

  return (
    <TeamWeeklyScreenContext.Provider
      value={{ viewingWeekStart, setViewingWeekStart }}
    >
      <div className={styles.container}>
        <TeamWeeklyScreenBreadcrumb
          workspaceName={workspaceName}
          teamUsername={teamUsername}
        />
        <YearWeekNo />

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
              startDate={viewingWeekStart}
              endDate={endOfWeek(viewingWeekStart, { weekStartsOn: 1 })}
            />
          </TabView>

          <TabView
            name="plan"
            label={t("teamWeeklyScreenWeeklyPlanSectionTitle")}
            containerClassName={styles.tabViewContainer}
          >
            <WeeklyPlanTab
              teamId={preferredTeamId}
              workspaceId={currentWorkspace?.workspaceId}
              viewingWeekStart={viewingWeekStart}
            />
          </TabView>
        </TabbedPanel>
      </div>
    </TeamWeeklyScreenContext.Provider>
  );
};

export default TeamWeeklyScreen;
