"use client";
import { BaseResponse } from "@/model/be/jinear-core";
import { useUpdatePreferredWorkspaceWithUsernameMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import Logger from "@/utils/logger";
import { useParams, useRouter } from "next/navigation";
import React, { useEffect } from "react";

interface WorkspaceAndTeamChangeListenerProps {}

const logger = Logger("WorkspaceAndTeamChangeListener");

const WorkspaceAndTeamChangeListener: React.FC<WorkspaceAndTeamChangeListenerProps> = ({}) => {
  const params = useParams();
  const router = useRouter();
  const workspaceNameFromUrl: string = params?.workspaceName as string;
  const preferedWorkspace = useTypedSelector((state) => state.account.current?.workspaceDisplayPreference?.workspace);

  const currentWorkspaceDifferentFromUrl =
    preferedWorkspace && workspaceNameFromUrl && preferedWorkspace.username != workspaceNameFromUrl;

  const [updatePreferredWorkspaceWithUsername, { error }] = useUpdatePreferredWorkspaceWithUsernameMutation();

  useEffect(() => {
    if (currentWorkspaceDifferentFromUrl) {
      updatePreferredWorkspaceWithUsername({
        workspaceUsername: workspaceNameFromUrl,
        dontReroute: true,
      });
    }
  }, [currentWorkspaceDifferentFromUrl]);

  useEffect(() => {
    //@ts-ignore
    const data = error?.data as BaseResponse;
    logger.log({ data });
    if (data?.errorCode == "14001") {
      router.push(ROUTE_IF_LOGGED_IN);
    }
  }, [error]);

  return null;
};

export default WorkspaceAndTeamChangeListener;
