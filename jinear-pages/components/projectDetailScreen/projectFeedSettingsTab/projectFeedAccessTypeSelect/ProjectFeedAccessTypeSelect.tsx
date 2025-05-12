import React, { useEffect, useState } from "react";
import styles from "./ProjectFeedAccessTypeSelect.module.scss";
import { ProjectFeedAccessType, ProjectFeedSettingsDto } from "@/be/jinear-core";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import useTranslation from "@/locals/useTranslation";
import { useUpdateProjectFeedSettingsMutation } from "@/api/projectFeedSettingsApi";

interface ProjectFeedAccessTypeSelectProps {
  projectFeedSettings: ProjectFeedSettingsDto;
  isFetching?: boolean;
  editable?: boolean;
}

const ProjectFeedAccessTypeSelect: React.FC<ProjectFeedAccessTypeSelectProps> = ({
                                                                                   projectFeedSettings,
                                                                                   isFetching,
                                                                                   editable
                                                                                 }) => {
  const { t } = useTranslation();
  const [current, setCurrent] = useState<ProjectFeedAccessType>(projectFeedSettings.projectFeedAccessType);

  const [updateProjectFeedSettings, {
    isSuccess: isUpdateSuccess,
    isLoading: isUpdateLoading
  }] = useUpdateProjectFeedSettingsMutation();

  useEffect(() => {
    setCurrent(projectFeedSettings.projectFeedAccessType);
  }, [projectFeedSettings.projectFeedAccessType]);

  const changeViewType = (value: string, index: number) => {
    if (value && (value == "PRIVATE" || value == "PUBLIC")) {
      const req = {
        projectId: projectFeedSettings.projectId,
        projectFeedAccessType: value as ProjectFeedAccessType
      };
      updateProjectFeedSettings(req);
      setCurrent(value);
    }
  };

  return (
    <div className={styles.container}>

      <div className={styles.titleContainer}>
        <h3>{t("projectFeedAccessTypeSectionTitle")}</h3>
        <span>{t("projectFeedAccessTypeSectionText")}</span>
      </div>

      <div className={styles.segmentedControlContainer}>
        <SegmentedControl
          id="project-feed-access-type-segment-control"
          name="project-feed-access-type-segment-control"
          defaultIndex={["PRIVATE", "PUBLIC"].indexOf(current)}
          segments={[
            { label: t("projectFeedAccessType_PRIVATE"), value: "PRIVATE", inputProps: { disabled: !editable } },
            { label: t("projectFeedAccessType_PUBLIC"), value: "PUBLIC", inputProps: { disabled: !editable } }
          ]}
          segmentLabelClassName={styles.viewTypeSegmentLabel}
          callback={changeViewType}
        />
      </div>
      <span className={styles.infoLabel}>
        {t(`projectFeedAccessType_${current}_info`)}
      </span>
    </div>
  );
};

export default ProjectFeedAccessTypeSelect;