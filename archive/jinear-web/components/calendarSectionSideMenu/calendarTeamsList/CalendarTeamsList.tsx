import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { useToggle } from "@/hooks/useToggle";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveMembershipsQuery } from "@/store/api/teamMemberApi";
import { popCalendarShareEventsModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import { IoShareOutline } from "react-icons/io5";
import { LuChevronDown, LuChevronRight } from "react-icons/lu";
import CalendarSourceButton from "../calendarSourceButton/CalendarSourceButton";
import styles from "./CalendarTeamsList.module.css";

interface CalendarTeamsListProps {
  workspace: WorkspaceDto;
}

const CalendarTeamsList: React.FC<CalendarTeamsListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [archivedVisible, toggleArchivedVisible] = useToggle(false);

  const {
    data: membershipsResponse,
    isSuccess,
    isFetching,
  } = useRetrieveMembershipsQuery({ workspaceId: workspace.workspaceId });

  const activeTeamMembershipList = useMemo(
    () => membershipsResponse?.data?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ACTIVE"),
    [membershipsResponse]
  );

  const archivedTeamMembershipList = useMemo(
    () => membershipsResponse?.data?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ARCHIVED"),
    [membershipsResponse]
  );

  const popShareWorkspaceEventsModal = () => {
    dispatch(popCalendarShareEventsModal({ workspaceId: workspace.workspaceId, visible: true }));
  };

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <div className={styles.titleContainer}>
        <MenuGroupTitle label={t("sideMenuTeamCalendarsTitle")} hasAddButton={false} />
        <Button onClick={popShareWorkspaceEventsModal} variant={ButtonVariants.hoverFilled2} heightVariant={ButtonHeight.short}>
          <IoShareOutline />
        </Button>
      </div>

      {isFetching && <CircularLoading />}
      <div className="spacer-h-1" />
      <div className={styles.calendarTeamListContainer}>
        {!isFetching && isSuccess && (
          <>
            {activeTeamMembershipList?.map((teamMemberDto) => (
              <CalendarSourceButton
                key={`team-cal-button-${teamMemberDto.teamId}`}
                label={teamMemberDto.team.name}
                teamId={teamMemberDto.teamId}
                type={"TEAM"}
              />
            ))}
            {archivedTeamMembershipList && archivedTeamMembershipList.length != 0 && (
              <div className={styles.archivedListContainer}>
                <Button heightVariant={ButtonHeight.short} className={styles.menuActionButton} onClick={toggleArchivedVisible}>
                  {archivedVisible ? <LuChevronDown /> : <LuChevronRight />}
                  {t("sideMenuArchivedTeamsTitle").replace(
                    "${n}",
                    archivedTeamMembershipList.length > 99 ? "99+" : archivedTeamMembershipList.length + ""
                  )}
                </Button>
                {archivedVisible && (
                  <div className={styles.archivedList}>
                    {archivedTeamMembershipList.map((teamMemberDto) => (
                      <CalendarSourceButton
                        key={`team-cal-button-${teamMemberDto.teamId}`}
                        label={teamMemberDto.team.name}
                        teamId={teamMemberDto.teamId}
                        type={"TEAM"}
                      />
                    ))}
                  </div>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default CalendarTeamsList;
