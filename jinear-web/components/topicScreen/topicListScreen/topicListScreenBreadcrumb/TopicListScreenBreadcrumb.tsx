import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TopicListScreenBreadcrumbProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TopicListScreenBreadcrumb: React.FC<TopicListScreenBreadcrumbProps> = ({ workspace, team }) => {
  const { t } = useTranslation();

  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspace?.title || ""} url={`/${workspace?.username}`} />
      <BreadcrumbLink label={team?.name || ""} url={`/${workspace?.username}/${team?.name}`} />
      <BreadcrumbLink label={t("topicListScreenTitle")} url={`/${workspace?.username}/${team?.name}/topic/list`} />
    </Breadcrumb>
  );
};

export default TopicListScreenBreadcrumb;
