import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface NewTopicScreenBreadcrumbProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const NewTopicScreenBreadcrumb: React.FC<NewTopicScreenBreadcrumbProps> = ({ workspace, team }) => {
  const { t } = useTranslation();

  return !workspace.isPersonal ? (
    <Breadcrumb>
      <BreadcrumbLink label={workspace?.title || ""} url={`/${workspace?.username}`} />
      <BreadcrumbLink label={team?.name || ""} url={`/${workspace?.username}/${team?.name}/topic/list`} />
      <BreadcrumbLink label={t("newTopicScreenTitle")} url={`/${workspace?.username}/${team?.name}/topic/new`} />
    </Breadcrumb>
  ) : null;
};

export default NewTopicScreenBreadcrumb;
