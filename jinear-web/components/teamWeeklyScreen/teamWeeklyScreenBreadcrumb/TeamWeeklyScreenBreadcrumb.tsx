import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TeamWeeklyScreenBreadcrumbProps {
  workspaceName: string;
  workspaceUsername: string;
  teamName?: string;
  teamUsername?: string;
}

const TeamWeeklyScreenBreadcrumb: React.FC<TeamWeeklyScreenBreadcrumbProps> = ({
  workspaceName,
  workspaceUsername,
  teamName = "",
  teamUsername = "",
}) => {
  const { t } = useTranslation();
  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspaceName} url={`/${workspaceUsername}`} />
      <BreadcrumbLink label={teamName} url={`/${workspaceUsername}/${teamUsername}`} />
      <BreadcrumbLink label={t("teamWeeklyScreenBreadcrumbLabel")} url={`/${workspaceUsername}/${teamUsername}/weekly`} />
    </Breadcrumb>
  );
};

export default TeamWeeklyScreenBreadcrumb;
