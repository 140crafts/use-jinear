import {selectAuthState} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import {
    consentRoute,
    forgetPendingConsentRequestId,
    readPendingConsentRequestId,
} from "@/util/oauthConsent.ts";
import Logger from "@/util/logger";
import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";

interface OauthConsentReturnListenerProps {
}

const logger = Logger("OauthConsentReturnListener");

const OauthConsentReturnListener: React.FC<OauthConsentReturnListenerProps> = ({}) => {
    const navigate = useNavigate();
    const {pathname} = useLocation();
    const authState = useTypedSelector(selectAuthState);

    useEffect(() => {
        if (authState != "LOGGED_IN" || pathname.indexOf("/oauth/consent") != -1) {
            return;
        }
        const requestId = readPendingConsentRequestId();
        if (!requestId) {
            return;
        }
        logger.log({requestId, pathname, resumingConsent: true});
        forgetPendingConsentRequestId();
        navigate(consentRoute(requestId), {replace: true});
    }, [authState, pathname, navigate]);

    return null;
};

export default OauthConsentReturnListener;
