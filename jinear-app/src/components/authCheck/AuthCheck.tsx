import {useMeQuery} from "@/store/api/accountApi";
import {selectAuthState} from "@/store/slice/accountSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import {askAppTrackingPermission} from "@/util/webviewUtils";
import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";

interface AuthCheckProps {
}

const logger = Logger("AuthCheck");

const PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS = [
    ["/engage/confirm-email"],
    ["/engage/reset-password-complete"],
    ["/engage/forgot-password"],
    ["/engage/workspace-invitation"],
    ["/pricing"],
    ["/terms"],
    ["/debug"],
    ["/project-feed/"],
    ["/shared/", "/feed/"]
];

const ONLY_NOT_LOGGED_IN_PATHS = ["/forgot-password", "/register", "/login"];

const checkPathIsIn = (pathname: string, allowed: string[][]) => {
    const result = allowed?.find(allowedPathKeys => {
        const matchesAllKeys =
            allowedPathKeys
                .map(allowedPath => pathname.indexOf(allowedPath) != -1)
                .reduce((prev, next) => prev && next);
        logger.log({allowedPathKeys, pathname, matchesAllKeys});
        return matchesAllKeys;
    }) != null;
    logger.log({pathname, allowed, checkPathIsIn: result});
    return result;
};

const AuthCheck: React.FC<AuthCheckProps> = ({}) => {
    const dispatch = useAppDispatch();
    const {data, error, isLoading} = useMeQuery();
    const navigate = useNavigate();
    const {pathname} = useLocation();
    const authState = useTypedSelector(selectAuthState);
    logger.log({
        authState,
        pathname,
        activeAccount: {data, error, isLoading}
    });

    useEffect(() => {
        if (authState == "LOGGED_IN" && ONLY_NOT_LOGGED_IN_PATHS.indexOf(pathname) != -1) {
            logger.log("This path only for NOT LOGGED IN users.");
            navigate("/", {replace: true});
        } else if (
            authState == "NOT_LOGGED_IN" &&
            ONLY_NOT_LOGGED_IN_PATHS.indexOf(pathname) == -1 &&
            !checkPathIsIn(pathname, PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS)
        ) {
            logger.log("This path only for LOGGED IN users.");
            navigate("/", {replace: true});
        }
        if (authState == "LOGGED_IN") {
            askAppTrackingPermission();
        }
        if (authState === "NOT_LOGGED_IN") {
            // resetAllStates(dispatch);
        }
    }, [navigate, dispatch, authState, pathname]);

    return null;
};

export default AuthCheck;
