import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveCalendarMembershipsQuery } from "@/store/api/calendarMemberApi";
import { popNewCalendarIntegrationModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuCalendarPlus } from "react-icons/lu";
import styles from "./ExternalCalendarsList.module.css";
import ExternalCalendar from "./externalCalendar/ExternalCalendar";

interface ExternalCalendarsListProps {
  workspace: WorkspaceDto;
}

const ExternalCalendarsList: React.FC<ExternalCalendarsListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const {
    data: membershipsResponse,
    isSuccess,
    isFetching,
  } = useRetrieveCalendarMembershipsQuery({ workspaceId: workspace.workspaceId });

  const popCalendarIntegrationModal = () => {
    dispatch(popNewCalendarIntegrationModal({ workspaceId: workspace.workspaceId, visible: true }));
  };

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <MenuGroupTitle label={t("sideMenuExternalCalendarsTitle")} hasAddButton={false} />
      {isFetching && <CircularLoading />}
      <div className="spacer-h-1" />
      <div className={styles.externalCalendarListContainer}>
        {!isFetching &&
          isSuccess &&
          membershipsResponse.data
            .map((calendarMembership) => calendarMembership.calendar)
            .map((calendar) => (
              <ExternalCalendar
                key={`external-cal-button-${calendar.provider}-${calendar.calendarId}`}
                workspace={workspace}
                calendar={calendar}
              />
            ))}
        {!isFetching && isSuccess && membershipsResponse.data?.length == 0 && <div></div>}
      </div>
      <div className="spacer-h-1" />
      <Button
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.hoverFilled2}
        className={styles.addCalendarIntegrationButton}
        onClick={popCalendarIntegrationModal}
      >
        <LuCalendarPlus />
        {t("sideMenuAddCalendarIntegrationLabel")}
      </Button>
    </div>
  );
};

export default ExternalCalendarsList;
