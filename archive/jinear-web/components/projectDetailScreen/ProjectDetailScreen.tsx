"use client";
import React from "react";
import styles from "./ProjectDetailScreen.module.css";
import { ProjectDto } from "@/be/jinear-core";
import ProjectDetailTab from "@/components/projectDetailScreen/projectDetailTab/ProjectDetailTab";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import useTranslation from "@/locals/useTranslation";
import { useHash } from "@/utils/useHash";
import Logger from "@/utils/logger";
import ProjectArchivedInfo from "@/components/projectDetailScreen/projectArchivedInfo/ProjectArchivedInfo";
import ProjectFeedSettingsTab from "@/components/projectDetailScreen/projectFeedSettingsTab/ProjectFeedSettingsTab";

interface ProjectDetailScreenProps {
  project: ProjectDto;
  isFetching: boolean;
}

const logger = Logger("ProjectDetailScreen");

const ProjectDetailScreen: React.FC<ProjectDetailScreenProps> = ({ project, isFetching }) => {
  const { t } = useTranslation();
  const hash = useHash();
  const current = hash ?? "#detail";

  logger.log({ current });

  return (
    <div className={styles.container}>

      <ProjectArchivedInfo project={project} />

      <TabbedPanel initialTabName={current}>
        <TabView name="#detail" label={t("projectDetailTabLabel")}>
          <ProjectDetailTab project={project} isFetching={isFetching} />
        </TabView>
        <TabView name="#feed-settings" label={t("projectFeedSettings")}>
          <ProjectFeedSettingsTab project={project} isFetching={isFetching} />
        </TabView>
      </TabbedPanel>
    </div>
  );
};

export default ProjectDetailScreen;