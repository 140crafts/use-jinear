import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface LastActivitiesBreadCrumbProps {
  workspace: WorkspaceDto;
}

const LastActivitiesBreadCrumb: React.FC<LastActivitiesBreadCrumbProps> = ({ workspace }) => {
  const { t } = useTranslation();

  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspace.title} url={`/${workspace.username}`} />
      <BreadcrumbLink label={t("lastActivitiesScreenBreadcrumbLabel")} url={`/${workspace.username}/last-activities`} />
    </Breadcrumb>
  );
};

export default LastActivitiesBreadCrumb;
