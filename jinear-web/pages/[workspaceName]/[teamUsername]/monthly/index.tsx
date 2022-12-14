import PeriodSpanTaskView from "@/components/periodSpanTaskView/PeriodSpanTaskView";
import MonthYearNo from "@/components/teamMonthlyScreen/monthYearNo/MonthYearNo";
import TeamMonthlyScreenBreadcrumb from "@/components/teamMonthlyScreen/teamMonthlyScreenBreadcrumb/TeamMonthlyScreenBreadcrumb";
import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import TeamMonthlyScreenContext from "@/store/context/screen/team/monthly/teamMonthlyScreenContext";
import {
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import {
  endOfMonth,
  endOfWeek,
  startOfMonth,
  startOfToday,
  startOfWeek,
} from "date-fns";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { IoCalendarOutline, IoCheckmark } from "react-icons/io5";
import styles from "./index.module.scss";

interface TeamMonthlyScreenProps {}

const TeamMonthlyScreen: React.FC<TeamMonthlyScreenProps> = ({}) => {
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
        <div className={styles.sectionTitleContainer}>
          <IoCheckmark size={21} />
          <div className={styles.sectionTitle}>
            {t("teamWeeklyScreenTaskWorkflowStatusSectionTitle")}
          </div>
        </div>

        <span className={styles.statusBoardContainer}>
          {preferredTeamId && currentWorkspace && (
            <TeamWorkflowStatusBoard
              teamId={preferredTeamId}
              workspaceId={currentWorkspace?.workspaceId}
              startDate={viewingPeriodStart}
              endDate={viewingPeriodEnd}
            />
          )}
        </span>

        <div className="spacer-h-2" />
        <div className={styles.sectionTitleContainer}>
          <IoCalendarOutline size={21} />
          <div className={styles.sectionTitle}>
            {t("teamMonthlyScreenPeriodPlanSectionTitle")}
          </div>
        </div>

        <span className={styles.monthViewContainer}>
          {preferredTeamId && currentWorkspace && (
            // <>
            //   <TeamWeekView
            //     teamId={preferredTeamId}
            //     workspaceId={currentWorkspace?.workspaceId}
            //     viewingWeekStart={viewingPeriodStart}
            //     showDayOfWeek={true}
            //   />
            //   <TeamWeekView
            //     teamId={preferredTeamId}
            //     workspaceId={currentWorkspace?.workspaceId}
            //     viewingWeekStart={addWeeks(viewingPeriodStart, 1)}
            //     showDayOfWeek={false}
            //   />
            //   <TeamWeekView
            //     teamId={preferredTeamId}
            //     workspaceId={currentWorkspace?.workspaceId}
            //     viewingWeekStart={addWeeks(viewingPeriodStart, 2)}
            //     showDayOfWeek={false}
            //   />
            //   <TeamWeekView
            //     teamId={preferredTeamId}
            //     workspaceId={currentWorkspace?.workspaceId}
            //     viewingWeekStart={addWeeks(viewingPeriodStart, 3)}
            //     showDayOfWeek={false}
            //   />
            // </>

            <PeriodSpanTaskView
              teamId={preferredTeamId}
              workspaceId={currentWorkspace?.workspaceId}
              viewingPeriodStart={viewingPeriodStart}
              viewingPeriodEnd={viewingPeriodEnd}
              showDayOfWeek={true}
            />
          )}
        </span>
      </div>
    </TeamMonthlyScreenContext.Provider>
  );
};

export default TeamMonthlyScreen;
