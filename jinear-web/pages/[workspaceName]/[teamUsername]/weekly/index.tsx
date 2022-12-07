import TeamWeeklyScreenBreadcrumb from "@/components/teamWeeklyScreen/teamWeeklyScreenBreadcrumb/TeamWeeklyScreenBreadcrumb";
import TeamWeekView from "@/components/teamWeeklyScreen/teamWeekView/TeamWeekView";
import YearWeekNo from "@/components/teamWeeklyScreen/yearWeekNo/YearWeekNo";
import TeamWeeklyScreenContext from "@/store/context/screen/team/weekly/teamWeeklyScreenContext";
import {
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { startOfToday, startOfWeek } from "date-fns";
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
    <TeamWeeklyScreenContext.Provider value={{ viewingWeekStart }}>
      <div className={styles.container}>
        <TeamWeeklyScreenBreadcrumb
          workspaceName={workspaceName}
          teamUsername={teamUsername}
        />
        <YearWeekNo />
        <span className={styles.weekViewContainer}>
          {preferredTeamId && currentWorkspace && (
            <TeamWeekView
              teamId={preferredTeamId}
              workspaceId={currentWorkspace?.workspaceId}
              viewingWeekStart={viewingWeekStart}
            />
          )}
        </span>
      </div>
    </TeamWeeklyScreenContext.Provider>
  );
};

export default TeamWeeklyScreen;
