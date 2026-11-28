import type {BaseResponse, WorkspaceDisplayPreferenceResponse} from "@/model/be/jinear-core";
import {useUpdatePreferredWorkspaceWithUsernameMutation} from "@/store/api/workspaceDisplayPreferenceApi";
import {useTypedSelector} from "@/store";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants";
import Logger from "@/util/logger";
import {getTranslatedMessage} from "@/locales/useTranslation";
import React, {useEffect} from "react";
import {useNavigate, useParams} from "react-router-dom";
import toast from "react-hot-toast";

interface WorkspaceAndTeamChangeListenerProps {
}

const logger = Logger("WorkspaceAndTeamChangeListener");
const WORKSPACE_SWITCHED_TOAST_ID = "workspace-switched";

const showWorkspaceSwitchedToast = (response: WorkspaceDisplayPreferenceResponse) => {
    const title = response.data.workspace?.title ?? "";
    const message = getTranslatedMessage("workspaceSwitchedToast").replace("${title}", title);
    toast(message, {id: WORKSPACE_SWITCHED_TOAST_ID});
};

const WorkspaceAndTeamChangeListener: React.FC<WorkspaceAndTeamChangeListenerProps> = ({}) => {
    const {workspaceName} = useParams();
    const navigate = useNavigate();

    const preferedWorkspace = useTypedSelector((state) => state.account.current?.workspaceDisplayPreference?.workspace);

    const currentWorkspaceDifferentFromUrl =
        preferedWorkspace && workspaceName && preferedWorkspace.username != workspaceName;

    const [updatePreferredWorkspaceWithUsername, {error}] = useUpdatePreferredWorkspaceWithUsernameMutation();

    useEffect(() => {
        if (currentWorkspaceDifferentFromUrl) {
            updatePreferredWorkspaceWithUsername({
                workspaceUsername: workspaceName,
                dontReroute: true
            })
                .unwrap()
                .then(showWorkspaceSwitchedToast)
                .catch(logger.log);
        }
    }, [currentWorkspaceDifferentFromUrl, workspaceName, updatePreferredWorkspaceWithUsername]);

    useEffect(() => {
        const data = error && "data" in error ? error.data as BaseResponse : undefined;
        logger.log({data});
        const errorCode = data?.errorCode;
        if (errorCode && ["14001", "001"].includes(errorCode)) {
            navigate(ROUTE_IF_LOGGED_IN);
        }
    }, [error, navigate]);

    return null;
};

export default WorkspaceAndTeamChangeListener;
