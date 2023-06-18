import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import React from "react";
import Breadcrumb from "../breadcrumb/Breadcrumb";
import BreadcrumbLink from "../breadcrumb/BreadcrumbLink";

interface GenericBreadcrumbProps {
  workspace?: WorkspaceDto | null;
  team?: TeamDto | null;
  label: string;
  pathAfterWorkspaceAndTeam: string;
}

const GenericBreadcrumb: React.FC<GenericBreadcrumbProps> = ({ workspace, team, label, pathAfterWorkspaceAndTeam }) => {
  const workspaceUsername = workspace?.username;
  const teamUsername = team?.username;
  return workspace && team && workspace.isPersonal ? null : (
    <Breadcrumb>
      <BreadcrumbLink label={workspace?.title || ""} url={`/${workspaceUsername}`} />
      <BreadcrumbLink label={team?.name || ""} url={`/${workspaceUsername}/${teamUsername}`} />
      <BreadcrumbLink label={label} url={`/${workspaceUsername}/${teamUsername}/${pathAfterWorkspaceAndTeam}`} />
    </Breadcrumb>
  );
};

export default GenericBreadcrumb;
