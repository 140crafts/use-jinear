import {selectAuthState, selectCurrentAccountsWorkspaces} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import React, {useEffect} from "react";
import {useNavigate} from "react-router-dom";

interface OnboardListenerProps {
}

const logger = Logger("OnboardListener");

const OnboardListener: React.FC<OnboardListenerProps> = ({}) => {
    const navigate = useNavigate();
    const authState = useTypedSelector(selectAuthState);
    const currentAccountWorkspaces = useTypedSelector(selectCurrentAccountsWorkspaces);

    useEffect(() => {
        logger.log({authState, currentAccountWorkspaces});
        if (authState == "LOGGED_IN" && currentAccountWorkspaces?.length == 0) {
            navigate("/new-workspace");
        }
    }, [authState, currentAccountWorkspaces]);

    return null;
};

export default OnboardListener;
