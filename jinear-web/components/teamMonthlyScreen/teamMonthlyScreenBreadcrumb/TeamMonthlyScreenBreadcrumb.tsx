import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TeamMonthlyScreenBreadcrumbProps {
  workspaceName: string;
  workspaceUsername: string;
  teamName?: string;
  teamUsername?: string;
}

const TeamMonthlyScreenBreadcrumb: React.FC<TeamMonthlyScreenBreadcrumbProps> = ({
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
      <BreadcrumbLink label={t("teamMonthlyScreenBreadcrumbLabel")} url={`/${workspaceUsername}/${teamUsername}/monthly`} />
    </Breadcrumb>
  );
};

export default TeamMonthlyScreenBreadcrumb;
