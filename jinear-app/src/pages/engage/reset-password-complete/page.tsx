import Button from "@/components/button";
import type {LocaleType} from "@/model/be/jinear-core";
import {useCompleteResetPasswordMutation} from "@/store/api/accountPasswordApi";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useRef} from "react";
import styles from "./index.module.css";
import {useSearchParams} from "react-router-dom";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import {useInstanceFlag} from "@/hooks/useInstanceFlag";

interface ResetPasswordCompletePageProps {
}

const ResetPasswordCompletePage: React.FC<ResetPasswordCompletePageProps> = ({}) => {
    const {t} = useTranslation();
    const [params] = useSearchParams();
    const forgotPasswordEnabled = useInstanceFlag("FORGOT_PASSWORD");
    const requestedTokenRef = useRef<string | null>(null);
    const [completeResetPassword, {isSuccess, isError, isLoading}] = useCompleteResetPasswordMutation();

    const token = params.get("token");
    const locale = t("localeType") as LocaleType;

    useEffect(() => {
        if (!token || requestedTokenRef.current == token) {
            return;
        }
        requestedTokenRef.current = token;
        completeResetPassword({uniqueToken: token, locale});
    }, [token, locale, completeResetPassword]);

    return (
        <div className={styles.container}>
            {isLoading && <CircularLoading size={28}/>}
            <div className={styles.infoContainer}>
                {isSuccess && !isError && (
                    <>
                        <div className={styles.title}>{t("engageCompletePasswordResetSuccessTitle")}</div>
                        <div>{t("engageCompletePasswordResetSuccessText")}</div>
                        <div className="spacer-h-4"/>
                        <div className={styles.actionButtonContainer}>
                            <Button href={"/login"}>{t("engageCompletePasswordResetLoginPage")}</Button>
                        </div>
                    </>
                )}
                {!isSuccess && isError && (
                    <>
                        <div className={styles.title}>{t("engageCompletePasswordResetErrorTitle")}</div>
                        <div>{t("engageCompletePasswordResetErrorText")}</div>
                        <div className="spacer-h-2"/>
                        <div className={styles.actionButtonContainer}>
                            {forgotPasswordEnabled ?
                                <Button
                                    href={"/forgot-password"}>{t("engageCompletePasswordResetForgotPasswordPage")}</Button> :
                                <Button href={"/login"}>{t("engageCompletePasswordResetLoginPage")}</Button>}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default ResetPasswordCompletePage;
