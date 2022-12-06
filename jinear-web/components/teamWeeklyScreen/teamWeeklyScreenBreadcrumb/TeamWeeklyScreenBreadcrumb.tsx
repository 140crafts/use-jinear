import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TeamWeeklyScreenBreadcrumbProps {
  workspaceName: string;
  teamUsername: string;
}

const TeamWeeklyScreenBreadcrumb: React.FC<TeamWeeklyScreenBreadcrumbProps> = ({
  workspaceName,
  teamUsername,
}) => {
  const { t } = useTranslation();
  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspaceName} url={`/${workspaceName}`} />
      <BreadcrumbLink
        label={teamUsername}
        url={`/${workspaceName}/${teamUsername}`}
      />
      <BreadcrumbLink
        label={t("teamWeeklyScreenBreadcrumbLabel")}
        url={`/${workspaceName}/${teamUsername}/weekly`}
      />
    </Breadcrumb>
  );
};

export default TeamWeeklyScreenBreadcrumb;
