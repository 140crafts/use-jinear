import Button from "@/components/button";
import {useFirstRender} from "@/hooks/useFirstRender";
import type {LocaleType} from "@/model/be/jinear-core";
import {useCompleteResetPasswordMutation} from "@/store/api/accountPasswordApi";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
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
    const firstRender = useFirstRender();
    const [completeResetPassword, {data, isSuccess, isError, isLoading}] = useCompleteResetPasswordMutation();

    const token = params.get("token");

    useEffect(() => {
        if (token && !isLoading && !data) {
            completeResetPassword({
                uniqueToken: token,
                locale: t("localeType") as LocaleType,
            });
        }
    }, [token, firstRender]);

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
