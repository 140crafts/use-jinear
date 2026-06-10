import React, { useEffect, useState } from "react";
import styles from "./ProjectPostInitializeAccessTypeSelect.module.scss";
import { ProjectFeedSettingsDto, ProjectPostInitializeAccessType } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import { useUpdateProjectFeedSettingsMutation } from "@/api/projectFeedSettingsApi";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";

interface ProjectPostInitializeAccessTypeSelectProps {
  projectFeedSettings: ProjectFeedSettingsDto;
  isFetching?: boolean;
  editable?: boolean;
}

const ACCESS_TYPES = ["WORKSPACE_ADMINS", "PROJECT_LEAD", "PROJECT_TEAM_ADMINS", "PROJECT_TEAM_MEMBERS", "WORKSPACE_MEMBERS"];

const ProjectPostInitializeAccessTypeSelect: React.FC<ProjectPostInitializeAccessTypeSelectProps> = ({
                                                                                                       projectFeedSettings,
                                                                                                       isFetching,
                                                                                                       editable = true
                                                                                                     }) => {
  const { t } = useTranslation();
  const [current, setCurrent] = useState<ProjectPostInitializeAccessType>(projectFeedSettings.projectPostInitializeAccessType);

  const [updateProjectFeedSettings, {
    isSuccess: isUpdateSuccess,
    isLoading: isUpdateLoading
  }] = useUpdateProjectFeedSettingsMutation();

  useEffect(() => {
    setCurrent(projectFeedSettings.projectPostInitializeAccessType);
  }, [projectFeedSettings.projectPostInitializeAccessType]);

  const changeViewType = (value: string, index: number) => {
    if (value && (ACCESS_TYPES.includes(value))) {
      const req = {
        projectId: projectFeedSettings.projectId,
        projectPostInitializeAccessType: value as ProjectPostInitializeAccessType
      };
      updateProjectFeedSettings(req);
      setCurrent(value as ProjectPostInitializeAccessType);
    }
  };

  return (
    <div className={styles.container}>

      <div className={styles.titleContainer}>
        <h3>{t("projectPostInitializeAccessTypeSectionTitle")}</h3>
        <span>{t("projectPostInitializeAccessTypeSectionText")}</span>
      </div>

      <div className={styles.segmentedControlContainer}>
        <SegmentedControl
          id="project-post-initialize-access-type-segment-control"
          name="project-post-initialize-access-type-segment-control"
          defaultIndex={ACCESS_TYPES.indexOf(current)}
          segments={[
            {
              label: t("projectPostInitializeAccessType_WORKSPACE_ADMINS"),
              value: "WORKSPACE_ADMINS",
              inputProps: { disabled: !editable }
            },
            {
              label: t("projectPostInitializeAccessType_PROJECT_LEAD"),
              value: "PROJECT_LEAD",
              inputProps: { disabled: !editable }
            },
            {
              label: t("projectPostInitializeAccessType_PROJECT_TEAM_ADMINS"),
              value: "PROJECT_TEAM_ADMINS",
              inputProps: { disabled: !editable }
            },
            {
              label: t("projectPostInitializeAccessType_PROJECT_TEAM_MEMBERS"),
              value: "PROJECT_TEAM_MEMBERS",
              inputProps: { disabled: !editable }
            },
            {
              label: t("projectPostInitializeAccessType_WORKSPACE_MEMBERS"),
              value: "WORKSPACE_MEMBERS",
              inputProps: { disabled: !editable }
            }
          ]}
          segmentLabelClassName={styles.viewTypeSegmentLabel}
          callback={changeViewType}
        />
      </div>
      <span className={styles.infoLabel}>
        {t(`projectPostInitializeAccessType_${current}_info`)}
      </span>
    </div>
  );
};

export default ProjectPostInitializeAccessTypeSelect;