import {
  useUpdatePreferredTeamWithUsernameMutation,
  useUpdatePreferredWorkspaceWithUsernameMutation,
} from "@/store/api/workspaceDisplayPreferenceApi";
import {
  selectCurrentAccountsPreferredTeam,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

interface WorkspaceAndTeamChangeListenerProps {}

const logger = Logger("WorkspaceAndTeamChangeListener");

const WorkspaceAndTeamChangeListener: React.FC<
  WorkspaceAndTeamChangeListenerProps
> = ({}) => {
  const router = useRouter();
  const workspaceNameFromUrl: string = router.query?.workspaceName as string;
  const teamUsernameFromUrl: string = router.query?.teamUsername as string;
  logger.log({ query: router });
  const currentWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const [
    updatePreferredWorkspaceWithUsername,
    { isError: isTeamUpdateError, status: teamUpdateStatus },
  ] = useUpdatePreferredWorkspaceWithUsernameMutation();
  const [
    updatePreferredTeamWithUsername,
    { isError: isWorkspaceUpdateError, status: workspaceUpdateStatus },
  ] = useUpdatePreferredTeamWithUsernameMutation();

  useEffect(() => {
    if (isWorkspaceUpdateError || isTeamUpdateError) {
      logger.log({ teamUpdateStatus, workspaceUpdateStatus });
      router.replace("/");
    }
  }, [
    isTeamUpdateError,
    isWorkspaceUpdateError,
    teamUpdateStatus,
    workspaceUpdateStatus,
  ]);

  useEffect(() => {
    if (
      currentWorkspace &&
      workspaceNameFromUrl &&
      currentWorkspace.username != workspaceNameFromUrl
    ) {
      const log = `Current workspace is different from url. fromUrl: ${workspaceNameFromUrl}, currentName: ${currentWorkspace.username}`;
      logger.log(log);
      updatePreferredWorkspaceWithUsername({
        workspaceUsername: workspaceNameFromUrl,
      });
    }
  }, [router.asPath, workspaceNameFromUrl, currentWorkspace]);

  useEffect(() => {
    if (
      workspaceNameFromUrl &&
      teamUsernameFromUrl &&
      preferredTeam &&
      preferredTeam.name != teamUsernameFromUrl
    ) {
      const log = `Current team is different from url. fromUrl: ${teamUsernameFromUrl}, preferredTeamName: ${preferredTeam.name}`;
      logger.log(log);
      updatePreferredTeamWithUsername({
        workspaceUsername: workspaceNameFromUrl,
        teamName: teamUsernameFromUrl,
      });
    }
  }, [router.asPath, workspaceNameFromUrl, teamUsernameFromUrl, preferredTeam]);

  return null;
};

export default WorkspaceAndTeamChangeListener;
