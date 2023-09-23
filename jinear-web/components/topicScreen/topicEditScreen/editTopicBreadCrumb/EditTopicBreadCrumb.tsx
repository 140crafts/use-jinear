import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface EditTopicBreadCrumbProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  topicName: string;
  topicId: string;
}

const EditTopicBreadCrumb: React.FC<EditTopicBreadCrumbProps> = ({ workspace, team, topicName, topicId }) => {
  const { t } = useTranslation();

  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspace?.title || ""} url={`/${workspace?.username}`} />
      <BreadcrumbLink label={team?.name || ""} url={`/${workspace?.username}/${team?.name}/topic/list`} />
      <BreadcrumbLink label={t("topicListScreenTitle")} url={`/${workspace?.username}/${team?.name}/topic/list`} />
      <BreadcrumbLink label={topicName} url={`/${workspace?.username}/${team?.name}/topic/edit/${topicId}`} />
    </Breadcrumb>
  );
};

export default EditTopicBreadCrumb;
