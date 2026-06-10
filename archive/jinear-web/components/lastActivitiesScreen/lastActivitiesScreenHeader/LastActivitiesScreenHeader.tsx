import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoClose } from "react-icons/io5";
import styles from "./LastActivitiesScreenHeader.module.css";

interface LastActivitiesScreenHeaderProps {
  workspace: WorkspaceDto;
  filterBy?: TeamDto;
  setFilterBy?: React.Dispatch<React.SetStateAction<TeamDto | undefined>>;
}

const LastActivitiesScreenHeader: React.FC<LastActivitiesScreenHeaderProps> = ({ workspace, filterBy, setFilterBy }) => {
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
      <h2>{filterBy ? t("lastActivitiesScreenFilteredTitle") : t("lastActivitiesScreenAllTeamsTitle")}</h2>
      <div className={styles.actionBar}>
        <div
          className={styles.subtext}
          dangerouslySetInnerHTML={{
            __html: filterBy
              ? t("lastActivitiesScreenFilteredText").replace("${teamName}", filterBy.name)
              : t("lastActivitiesScreenAllTeamsText"),
          }}
        />
        <Button
          onClick={filterBy ? clearFilter : popFilterTeamModal}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          className={filterBy ? styles.filterButtonWithActiveFilter : undefined}
        >
          {filterBy ? (
            <>
              <IoClose />
              <div className="spacer-w-1" />
              {t("lastActivitiesScreenClearFilterButton")}
            </>
          ) : (
            t("lastActivitiesScreenFilterButton")
          )}
        </Button>
      </div>
    </div>
  );
};

export default LastActivitiesScreenHeader;
