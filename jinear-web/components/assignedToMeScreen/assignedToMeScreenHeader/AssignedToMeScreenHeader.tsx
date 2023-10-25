import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoClose } from "react-icons/io5";
import styles from "./AssignedToMeScreenHeader.module.css";

interface AssignedToMeScreenHeaderProps {
  workspace: WorkspaceDto;
  filterBy?: TeamDto;
  setFilterBy?: React.Dispatch<React.SetStateAction<TeamDto | undefined>>;
}

const AssignedToMeScreenHeader: React.FC<AssignedToMeScreenHeaderProps> = ({ workspace, filterBy, setFilterBy }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popFilterTeamModal = () => {
    dispatch(popTeamPickerModal({ workspaceId: workspace.workspaceId, visible: true, onPick: setFilterBy }));
  };

  const clearFilter = () => {
    setFilterBy?.(undefined);
  };

  return (
    <div className={styles.container}>
      {/* <AssignedToMeScreenBreadcrumb /> */}
      {/* <div className="spacer-h-2" /> */}
      <h2>{t("assignedToMeTaskListName")}</h2>
      <div className="spacer-h-1" />
      <div
        dangerouslySetInnerHTML={{
          __html: filterBy
            ? t("assignedToMeScreenSubtitleTeamFiltered").replace("${teamName}", filterBy.name)
            : t("assignedToMeScreenSubtitleAllTeams"),
        }}
      />
      <div className={styles.actionBar}>
        <Button
          onClick={filterBy ? clearFilter : popFilterTeamModal}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled}
          className={filterBy ? styles.filterButtonWithActiveFilter : undefined}
        >
          {filterBy ? (
            <>
              <IoClose />
              <div className="spacer-w-1" />
              {t("calendarClearFilterButton")}
            </>
          ) : (
            t("calendarFilterButton")
          )}
        </Button>
      </div>
    </div>
  );
};

export default AssignedToMeScreenHeader;
