import type {BaseResponse} from "@/model/be/jinear-core";
import {useUpdatePreferredWorkspaceWithUsernameMutation} from "@/store/api/workspaceDisplayPreferenceApi";
import {useTypedSelector} from "@/store";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants";
import Logger from "@/util/logger";
import React, {useEffect} from "react";
import {useLocation, useNavigate, useParams} from "react-router-dom";

interface WorkspaceAndTeamChangeListenerProps {
}

const logger = Logger("WorkspaceAndTeamChangeListener");

const WorkspaceAndTeamChangeListener: React.FC<WorkspaceAndTeamChangeListenerProps> = ({}) => {
    const {workspaceName} = useParams();
    const navigate = useNavigate();
    const {pathname} = useLocation();

    const preferedWorkspace = useTypedSelector((state) => state.account.current?.workspaceDisplayPreference?.workspace);

    const currentWorkspaceDifferentFromUrl =
        preferedWorkspace && workspaceName && preferedWorkspace.username != workspaceName;

    const [updatePreferredWorkspaceWithUsername, {error}] = useUpdatePreferredWorkspaceWithUsernameMutation();

    useEffect(() => {
        if (currentWorkspaceDifferentFromUrl && pathname.indexOf("shared") == -1) {
            updatePreferredWorkspaceWithUsername({
                workspaceUsername: workspaceName,
                dontReroute: true
            });
        }
    }, [currentWorkspaceDifferentFromUrl, pathname]);

    useEffect(() => {
        //@ts-ignore
        const data = error?.data as BaseResponse;
        logger.log({data});
        if (["14001", "001"].indexOf(data?.errorCode) != -1) {
            navigate(ROUTE_IF_LOGGED_IN);
        }
    }, [error]);

    return null;
};

export default WorkspaceAndTeamChangeListener;
