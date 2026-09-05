import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import LoginWithMailForm from "@/components/form/login-with-mail-form/LoginWithMailForm.tsx";
import useTranslation, {type StringKeys} from "@/locales/useTranslation";
import {useRetrieveOauthConsentInfoQuery, useSubmitOauthConsentMutation} from "@/store/api/oauthApi.ts";
import {selectAuthState} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import {forgetPendingConsentRequestId, rememberPendingConsentRequestId} from "@/util/oauthConsent.ts";
import React, {useEffect} from "react";
import {LuCheck, LuLaptop, LuShieldCheck} from "react-icons/lu";
import {useSearchParams} from "react-router-dom";
import styles from "./index.module.css";

interface OauthConsentPageProps {
}

const logger = Logger("OauthConsentPage");

const SCOPE_LABEL_KEYS: Record<string, StringKeys> = {
    "workspace:read": "oauthConsentScopeWorkspaceRead",
    "tasks:read": "oauthConsentScopeTasksRead",
    "tasks:write": "oauthConsentScopeTasksWrite",
    "calendar:read": "oauthConsentScopeCalendarRead",
    "notes:read": "oauthConsentScopeNotesRead",
    "files:read": "oauthConsentScopeFilesRead",
    "offline_access": "oauthConsentScopeOfflineAccess",
};

const OauthConsentPage: React.FC<OauthConsentPageProps> = ({}) => {
    const {t} = useTranslation();
    const [params] = useSearchParams();
    const requestId = params?.get("request_id");
    const authState = useTypedSelector(selectAuthState);
    const isLoggedIn = authState == "LOGGED_IN";

    const {
        currentData: consentInfoResponse,
        isError,
    } = useRetrieveOauthConsentInfoQuery({requestId: requestId ?? ""}, {skip: !requestId});

    const [submitConsent, {isLoading: isSubmitting}] = useSubmitOauthConsentMutation();

    const info = consentInfoResponse?.data;

    useEffect(() => {
        if (requestId && !isLoggedIn) {
            rememberPendingConsentRequestId(requestId);
        }
    }, [requestId, isLoggedIn]);

    const respond = async (approved: boolean) => {
        if (!requestId || isSubmitting) {
            return;
        }
        try {
            const response = await submitConsent({requestId, approved}).unwrap();
            forgetPendingConsentRequestId();
            logger.log({approved, redirecting: true});
            window.location.href = response.data;
        } catch (error) {
            logger.log({consentSubmitFailed: error});
        }
    };

    if (!requestId) {
        return (
            <div className={styles.container}>
                <div className={styles.card}>
                    <div className={styles.title}>{t("oauthConsentUnavailableTitle")}</div>
                    <div className={styles.text}>{t("oauthConsentUnavailableText")}</div>
                </div>
            </div>
        );
    }

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                {!info && !isError && <CircularLoading size={28}/>}

                {isError && (
                    <>
                        <div className={styles.title}>{t("oauthConsentUnavailableTitle")}</div>
                        <div className={styles.text}>{t("oauthConsentUnavailableText")}</div>
                    </>
                )}

                {!isError && info && (
                    <>
                        <div className={styles.title}>{t("oauthConsentTitle")}</div>
                        <div className={styles.clientRow}>
                            <LuShieldCheck className={styles.clientIcon}/>
                            <div className={styles.clientText}>
                                <div className={styles.clientHost}>{info.clientDisplayHost}</div>
                                {info.clientName && <div className={styles.clientName}>{info.clientName}</div>}
                            </div>
                        </div>
                        <div className={styles.text}>{t("oauthConsentSubtitle")}</div>

                        <div className={styles.scopeTitle}>{t("oauthConsentScopesTitle")}</div>
                        <ul className={styles.scopeList}>
                            {info.requestedScopes?.map((scope) => (
                                <li key={scope} className={styles.scopeItem}>
                                    <LuCheck className={styles.scopeIcon}/>
                                    <span>{SCOPE_LABEL_KEYS[scope] ? t(SCOPE_LABEL_KEYS[scope]) : scope}</span>
                                </li>
                            ))}
                        </ul>

                        {info.loopbackOnly && (
                            <div className={styles.noteRow}>
                                <LuLaptop className={styles.noteIcon}/>
                                <span>{t("oauthConsentLoopbackNote")}</span>
                            </div>
                        )}
                        {!info.loopbackOnly && info.redirectHost && (
                            <div className={styles.noteRow}>
                                <LuLaptop className={styles.noteIcon}/>
                                <span>{`${t("oauthConsentRedirectNote")} ${info.redirectHost}`}</span>
                            </div>
                        )}

                        {(info.policyUri || info.tosUri || info.clientUri) && (
                            <div className={styles.linkRow}>
                                {info.clientUri &&
                                    <a href={info.clientUri} target="_blank"
                                       rel="noreferrer noopener">{t("oauthConsentClientSiteLink")}</a>}
                                {info.policyUri &&
                                    <a href={info.policyUri} target="_blank"
                                       rel="noreferrer noopener">{t("oauthConsentPrivacyLink")}</a>}
                                {info.tosUri &&
                                    <a href={info.tosUri} target="_blank"
                                       rel="noreferrer noopener">{t("oauthConsentTermsLink")}</a>}
                            </div>
                        )}

                        {isLoggedIn ? (
                            <div className={styles.actionRow}>
                                <Button
                                    disabled={isSubmitting}
                                    onClick={() => respond(false)}
                                    variant={ButtonVariants.outline}
                                >
                                    {t("oauthConsentDenyButton")}
                                </Button>
                                <Button
                                    disabled={isSubmitting}
                                    loading={isSubmitting}
                                    onClick={() => respond(true)}
                                    variant={ButtonVariants.contrast}
                                >
                                    {t("oauthConsentAllowButton")}
                                </Button>
                            </div>
                        ) : (
                            <>
                                <div className={styles.signInNote}>{t("oauthConsentSignInFirst")}</div>
                                <LoginWithMailForm/>
                            </>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};

export default OauthConsentPage;
