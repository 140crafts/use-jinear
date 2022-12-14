import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TeamSettingsScreenBreadcrumbProps {
  workspaceName: string;
  teamUsername: string;
}

const TeamSettingsScreenBreadcrumb: React.FC<
  TeamSettingsScreenBreadcrumbProps
> = ({ workspaceName, teamUsername }) => {
  const { t } = useTranslation();
  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspaceName} url={`/${workspaceName}`} />
      <BreadcrumbLink
        label={teamUsername}
        url={`/${workspaceName}/${teamUsername}`}
      />
      <BreadcrumbLink
        label={t("teamSettingsScreenBreadcrumbLabel")}
        url={`/${workspaceName}/${teamUsername}/settings`}
      />
    </Breadcrumb>
  );
};

export default TeamSettingsScreenBreadcrumb;
