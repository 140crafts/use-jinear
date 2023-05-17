import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoClose } from "react-icons/io5";
import InboxScreenBreadcrumbs from "../inboxScreenBreadcrumbs/InboxScreenBreadcrumbs";
import styles from "./InboxScreenHeader.module.css";

interface InboxScreenHeaderProps {
  workspace?: WorkspaceDto | null;
  filterBy?: TeamDto;
  setFilterBy?: React.Dispatch<React.SetStateAction<TeamDto | undefined>>;
}

const InboxScreenHeader: React.FC<InboxScreenHeaderProps> = ({ workspace, filterBy, setFilterBy }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popFilterTeamModal = () => {
    dispatch(popTeamPickerModal({ workspaceId: workspace?.workspaceId, visible: true, onPick: setFilterBy }));
  };

  const clearFilter = () => {
    setFilterBy?.(undefined);
  };

  return (
    <div className={styles.container}>
      <InboxScreenBreadcrumbs />
      <div className="spacer-h-2" />
      <h2>{t("inboxScreenHeader")}</h2>
      <div className={styles.actionBar}>
        <div
          className={styles.subTitle}
          dangerouslySetInnerHTML={{
            __html: workspace?.isPersonal
              ? t("inboxHeaderPersonalWorkspaceSubtitle")
              : filterBy
              ? t("inboxHeaderFilteredTeamSubtitle").replace("${teamName}", filterBy.name)
              : t("inboxHeaderAllTeamsSubtitle"),
          }}
        />
        {!workspace?.isPersonal && (
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
                {t("inboxScreenClearFilterButton")}
              </>
            ) : (
              t("inboxScreenFilterButton")
            )}
          </Button>
        )}
      </div>
    </div>
  );
};

export default InboxScreenHeader;
