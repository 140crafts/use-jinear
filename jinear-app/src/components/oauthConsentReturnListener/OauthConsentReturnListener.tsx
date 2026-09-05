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

/**
 * Sends a user back to the connection request they were answering when they had to sign
 * in first. Every sign in method lands on the app root, so this watches for that landing
 * rather than for a particular form's success.
 */
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
