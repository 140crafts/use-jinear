import { useUpdatePreferredWorkspaceWithUsernameMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { selectReroute } from "@/store/slice/displayPreferenceSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

interface WorkspaceAndTeamChangeListenerProps {}

const logger = Logger("WorkspaceAndTeamChangeListener");

const WorkspaceAndTeamChangeListener: React.FC<WorkspaceAndTeamChangeListenerProps> = ({}) => {
  const router = useRouter();
  const workspaceNameFromUrl: string = router.query?.workspaceName as string;
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const activeReroute = useTypedSelector(selectReroute);

  const currentWorkspaceDifferentFromUrl =
    currentWorkspace && workspaceNameFromUrl && currentWorkspace.username != workspaceNameFromUrl;

  const [updatePreferredWorkspaceWithUsername, { isError: isWorkspaceUpdateError, status: workspaceUpdateStatus }] =
    useUpdatePreferredWorkspaceWithUsernameMutation();

  useEffect(() => {
    if (isWorkspaceUpdateError) {
      logger.log({ workspaceUpdateStatus });
      router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [isWorkspaceUpdateError, workspaceUpdateStatus]);

  useEffect(() => {
    if (activeReroute) {
      return;
    } else if (currentWorkspaceDifferentFromUrl) {
      updatePreferredWorkspaceWithUsername({
        workspaceUsername: workspaceNameFromUrl,
        dontReroute: true,
      });
    }
  }, [activeReroute, currentWorkspaceDifferentFromUrl]);

  logger.log({
    workspaceNameFromUrl,
    currentWorkspace,
    currentWorkspaceDifferentFromUrl,
    activeReroute,
  });

  return null;
};

export default WorkspaceAndTeamChangeListener;
