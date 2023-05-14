import {
  useUpdatePreferredTeamWithUsernameMutation,
  useUpdatePreferredWorkspaceWithUsernameMutation,
} from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { selectReroute } from "@/store/slice/displayPreferenceSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

interface WorkspaceAndTeamChangeListenerProps {}

const logger = Logger("WorkspaceAndTeamChangeListener");

const WorkspaceAndTeamChangeListener: React.FC<WorkspaceAndTeamChangeListenerProps> = ({}) => {
  const router = useRouter();
  const workspaceNameFromUrl: string = router.query?.workspaceName as string;
  const teamUsernameFromUrl: string = router.query?.teamUsername as string;
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const activeReroute = useTypedSelector(selectReroute);

  const currentWorkspaceDifferentFromUrl =
    currentWorkspace && workspaceNameFromUrl && currentWorkspace.username != workspaceNameFromUrl;
  const currentTeamDifferentFromUrl =
    workspaceNameFromUrl && teamUsernameFromUrl && preferredTeam && preferredTeam.username != teamUsernameFromUrl;

  const [updatePreferredWorkspaceWithUsername, { isError: isTeamUpdateError, status: teamUpdateStatus }] =
    useUpdatePreferredWorkspaceWithUsernameMutation();
  const [updatePreferredTeamWithUsername, { isError: isWorkspaceUpdateError, status: workspaceUpdateStatus }] =
    useUpdatePreferredTeamWithUsernameMutation();

  useEffect(() => {
    if (isWorkspaceUpdateError || isTeamUpdateError) {
      logger.log({ teamUpdateStatus, workspaceUpdateStatus });
      router.replace("/");
    }
  }, [isTeamUpdateError, isWorkspaceUpdateError, teamUpdateStatus, workspaceUpdateStatus]);

  useEffect(() => {
    if (activeReroute) {
      return;
    } else if (currentWorkspaceDifferentFromUrl) {
      updatePreferredWorkspaceWithUsername({
        workspaceUsername: workspaceNameFromUrl,
        dontReroute: true,
      });
    } else if (currentTeamDifferentFromUrl) {
      updatePreferredTeamWithUsername({
        workspaceUsername: workspaceNameFromUrl,
        teamUsername: teamUsernameFromUrl,
        dontReroute: true,
      });
    }
  }, [activeReroute, currentWorkspaceDifferentFromUrl, currentTeamDifferentFromUrl]);

  logger.log({
    workspaceNameFromUrl,
    teamUsernameFromUrl,
    currentWorkspace,
    preferredTeam,
    currentWorkspaceDifferentFromUrl,
    currentTeamDifferentFromUrl,
    activeReroute,
  });

  return null;
};

export default WorkspaceAndTeamChangeListener;
