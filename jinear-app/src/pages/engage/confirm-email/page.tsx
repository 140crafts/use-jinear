import Button, {ButtonVariants} from "@/components/button";
import type {LocaleType} from "@/model/be/jinear-core";
import {useConfirmEmailMutation, useResendConfirmEmailMutation} from "@/store/api/accountApi";
import Logger from "@/util/logger";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import styles from "./index.module.css";
import {useNavigate, useSearchParams} from "react-router-dom";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface ConfirmEmailPageProps {
}

const logger = Logger("ConfirmEmailPage");
const ConfirmEmailPage: React.FC<ConfirmEmailPageProps> = ({}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [params] = useSearchParams();
    const [confirmEmail, {data, isSuccess, isError, isLoading}] = useConfirmEmailMutation();
    const [resendConfirmEmail, {error: resendErrorResponse, isLoading: resendLoading, isError: isResendError}] =
        useResendConfirmEmailMutation();
    const token = params.get("token");

    useEffect(() => {
        if (token && !isLoading && !data) {
            logger.log({token, isLoading});
            confirmEmail({
                uniqueToken: token,
            });
        }
    }, [token, data]);

    useEffect(() => {
        logger.log({resendErrorResponse});
        if (
            resendErrorResponse &&
            // @ts-ignore
            resendErrorResponse?.data?.errorCode == "16002"
        ) {
            navigate(ROUTE_IF_LOGGED_IN, {replace: true});
        }
    }, [resendErrorResponse, isResendError]);

    const resend = () => {
        token && resendConfirmEmail({
            token,
            locale: t("localeType") as LocaleType,
        });
    };

    return (
        <div className={styles.container}>
            {isLoading && <CircularLoading size={28}/>}
            <div className={styles.infoContainer}>
                {isSuccess && !isError && (
                    <>
                        <div className={styles.title}>{t("engageConfirmEmailTitleSuccess")}</div>
                        <div className="spacer-h-4"/>
                        <div className={styles.actionButtonContainer}>
                            <Button href={"/"} variant={ButtonVariants.filled}>
                                {t("engageConfirmEmailContinueHomeButton")}
                            </Button>
                        </div>
                    </>
                )}
                {!isSuccess && isError && (
                    <>
                        <div className={styles.title}>{t("engageConfirmEmailTitleError")}</div>
                        <div>{t("engageConfirmEmailTextError")}</div>
                        <div className="spacer-h-2"/>
                        <div className={styles.actionButtonContainer}>
                            <Button loading={resendLoading} disabled={resendLoading} onClick={resend}
                                    variant={ButtonVariants.filled}>
                                {t("engageConfirmEmailRequestNewMail")}
                            </Button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default ConfirmEmailPage;
