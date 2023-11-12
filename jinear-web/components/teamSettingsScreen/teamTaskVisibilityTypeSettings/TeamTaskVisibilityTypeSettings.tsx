import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import Transition from "@/components/transition/Transition";
import { TeamDto, TeamTaskVisibilityType } from "@/model/be/jinear-core";
import { useUpdateTeamTaskVisibilityTypeMutation } from "@/store/api/teamApi";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import styles from "./TeamTaskVisibilityTypeSettings.module.css";

interface TeamTaskVisibilityTypeSettingsProps {
  team: TeamDto;
}

const TeamTaskVisibilityTypeSettings: React.FC<TeamTaskVisibilityTypeSettingsProps> = ({ team }) => {
  const { t } = useTranslation();
  const [nextViewType, setNextViewType] = useState<TeamTaskVisibilityType>(team.taskVisibility);
  const [updateTeamTaskVisibilityType, { isLoading }] = useUpdateTeamTaskVisibilityTypeMutation();

  useEffect(() => {
    setNextViewType(team.taskVisibility);
  }, [team]);

  const changeViewType = (value: string, index: number) => {
    if (value && (value == "VISIBLE_TO_ALL_TEAM_MEMBERS" || value == "OWNER_ASSIGNEE_AND_ADMINS")) {
      setNextViewType?.(value);
    }
  };

  const saveChanges = () => {
    updateTeamTaskVisibilityType({ teamId: team.teamId, taskVisibilityType: nextViewType });
  };

  return (
    <div className={styles.container}>
      <SectionTitle
        title={t("teamSettingsScreenTaskVisibilitySectionTitle")}
        description={t("teamSettingsScreenTaskVisibilitySectionDescription")}
      />
      <Transition initial={true} className={styles.content}>
        <SegmentedControl
          name="calendar-view-type-segment-control"
          defaultIndex={["VISIBLE_TO_ALL_TEAM_MEMBERS", "OWNER_ASSIGNEE_AND_ADMINS"].indexOf(team.taskVisibility)}
          segments={[
            { label: t("teamTaskVisibility_VISIBLE_TO_ALL_TEAM_MEMBERS"), value: "VISIBLE_TO_ALL_TEAM_MEMBERS" },
            { label: t("teamTaskVisibility_OWNER_ASSIGNEE_AND_ADMINS"), value: "OWNER_ASSIGNEE_AND_ADMINS" },
          ]}
          segmentLabelClassName={styles.viewTypeSegmentLabel}
          callback={changeViewType}
        />

        <div className={styles.detail}>
          <label>{t(`teamTaskVisibilityDetail_${nextViewType}`)}</label>
          <div className="spacer-h-2" />
          {nextViewType != team.taskVisibility && (
            <Button
              loading={isLoading}
              disabled={isLoading}
              heightVariant={ButtonHeight.short}
              variant={ButtonVariants.contrast}
              onClick={saveChanges}
            >
              {t("teamSettingsScreenTaskVisibilitySectionSave")}
            </Button>
          )}
        </div>
      </Transition>
    </div>
  );
};

export default TeamTaskVisibilityTypeSettings;
