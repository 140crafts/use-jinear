"use client";
import { useUpdatePreferredWorkspaceWithUsernameMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useParams } from "next/navigation";
import React, { useEffect } from "react";

interface WorkspaceAndTeamChangeListenerProps {}

const logger = Logger("WorkspaceAndTeamChangeListener");

const WorkspaceAndTeamChangeListener: React.FC<WorkspaceAndTeamChangeListenerProps> = ({}) => {
  const params = useParams();
  const workspaceNameFromUrl: string = params?.workspaceName as string;
  const preferedWorkspace = useTypedSelector((state) => state.account.current?.workspaceDisplayPreference?.workspace);

  const currentWorkspaceDifferentFromUrl =
    preferedWorkspace && workspaceNameFromUrl && preferedWorkspace.username != workspaceNameFromUrl;

  const [updatePreferredWorkspaceWithUsername] = useUpdatePreferredWorkspaceWithUsernameMutation();

  useEffect(() => {
    if (currentWorkspaceDifferentFromUrl) {
      updatePreferredWorkspaceWithUsername({
        workspaceUsername: workspaceNameFromUrl,
        dontReroute: true,
      });
    }
  }, [currentWorkspaceDifferentFromUrl]);
  return null;
};

export default WorkspaceAndTeamChangeListener;
