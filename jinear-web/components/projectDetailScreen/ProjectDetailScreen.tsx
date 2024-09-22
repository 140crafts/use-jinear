"use client";
import React from "react";
import styles from "./ProjectDetailScreen.module.css";
import { ProjectDto } from "@/be/jinear-core";
import ProjectTitle from "@/components/projectDetailScreen/projectTitle/ProjectTitle";
import Line from "@/components/line/Line";
import ProjectActionButtons from "@/components/projectDetailScreen/projectActionButtons/ProjectActionButtons";
import ProjectDescription from "@/components/projectDetailScreen/projectDescription/ProjectDescription";
import ProjectDetailMilestones from "@/components/projectDetailScreen/projectDetailMilestones/ProjectDetailMilestones";
import ProjectDetailTab from "@/components/projectDetailScreen/projectDetailTab/ProjectDetailTab";
import TabView from "@/components/tabbedPanel/tabView/TabView";
import WorkspaceInfoTab from "@/components/workspaceSettingsScreen/workspaceInfoTab/WorkspaceInfoTab";
import TabbedPanel from "@/components/tabbedPanel/TabbedPanel";
import useTranslation from "@/locals/useTranslation";
import { useHash } from "@/utils/useHash";
import Logger from "@/utils/logger";

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
      <TabbedPanel initialTabName={current}>
        <TabView name="#detail" label={t("projectDetailTabLabel")}>
          <ProjectDetailTab project={project} isFetching={isFetching} />
        </TabView>
        {/*<TabView name="#posts" label={t("projectPosts")}>*/}
        {/*  <ProjectDetailTab project={project} isFetching={isFetching} />*/}
        {/*</TabView>*/}
      </TabbedPanel>
    </div>
  );
};

export default ProjectDetailScreen;