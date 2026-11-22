import Button, {ButtonVariants} from "@/components/button";
import type {LocaleType} from "@/model/be/jinear-core";
import {useRegisterViaMailMutation} from "@/store/api/accountRegisterApi";
import Logger from "@/util/logger";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";

import React from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import toast from "react-hot-toast";
import FormTitle from "../form-title/FormTitle";
import styles from "./RegisterWithMailForm.module.css";
import OrLine from "@/components/or-line/or-line";
import {IoLogoGoogle} from "react-icons/io5";
import {isWebView} from "@/util/webviewUtils";
import {useRetrieveLoginRedirectInfoQuery} from "@/api/googleOAuthApi";
import SignInWithAppleButton from "@/components/sign-in-with-apple-button/SignInWithAppleButton";
import {useNavigate} from "react-router-dom";
import {usePasswordLogin} from "@/hooks/usePasswordLogin.ts";
import {TERMS} from "@/util/constants.ts";
import {useInstanceFlag} from "@/hooks/useInstanceFlag";

interface RegisterWithMailFormProps {
    className?: string;
}

export interface IRegisterWithMailForm {
    email: string;
    password: string;
    passwordControl: string;
    preferedLocale: string;
    termsAgree: boolean;
}

const logger = Logger("RegisterWithMailForm");

const RegisterWithMailForm: React.FC<RegisterWithMailFormProps> = ({className}) => {
    const {t} = useTranslation();
    const [registerViaMail, {isLoading: isRegisterLoading}] = useRegisterViaMailMutation();
    const navigate = useNavigate();
    const moveToLogin = (email: string) => navigate(`/login?email=${encodeURIComponent(email)}`, {replace: true});
    const {login, isLoading: isLoginLoading} = usePasswordLogin(moveToLogin);
    const isLoading = isRegisterLoading || isLoginLoading;
    const _isWebView = isWebView();
    const googleEnabled = useInstanceFlag("SIGN_IN_WITH_GOOGLE") && !_isWebView;
    const appleEnabled = useInstanceFlag("SIGN_IN_WITH_APPLE") && !_isWebView;
    const hasOtherMethods = googleEnabled || appleEnabled;
    const {
        data: authRedirectInfoResponse,
        isLoading: isAuthRedirectRetrieveLoading
    } = useRetrieveLoginRedirectInfoQuery(undefined, {skip: !googleEnabled});

    const {
        register,
        handleSubmit,
        formState: {errors}
    } = useForm<IRegisterWithMailForm>();

    const submit: SubmitHandler<IRegisterWithMailForm> = async (data) => {
        if (data.password != data.passwordControl) {
            toast(t("registerWithEmailPasswordsNotMatch"));
            return;
        }
        if (isLoading) {
            return;
        }
        try {
            await registerViaMail({
                email: data.email,
                password: data.password,
                locale: t("localeType") as LocaleType
            }).unwrap();
        } catch (error) {
            logger.log({registerViaMailError: error});
            return;
        }
        toast(t("registerWithEmailIsSuccessfull"));
        login(data.email, data.password);
    };

    return (
        <div className={cn(styles.container, className)}>
            <FormTitle
                title={t("registerWithMailFormTitle")}
                subTitle={t("registerWithMailFormSubTitle")}
                linkLabel={t("registerWithMailFormSubTitleLinkLabel")}
                link={"/login"}
            />

            <div className="spacer-h-5"/>

            <form autoComplete="off" id={"register-with-email-form"} className={styles.form}
                  onSubmit={handleSubmit(submit)}
                  action="#">
                <label className={styles.label} htmlFor={"register-with-email-email"}>
                    {t("registerWithEmailEmailLabel")}
                    <input id={"register-with-email-email"} type={"email"} autoFocus autoComplete="on"
                           required {...register("email")} />
                </label>

                <label className={styles.label} htmlFor={"register-with-email-password"}>
                    {t("registerWithEmailPasswordLabel")}
                    <input id={"register-with-email-password"} type={"password"} required {...register("password")} />
                </label>

                <label className={styles.label} htmlFor={"register-with-email-password-control"}>
                    {t("registerWithEmailPasswordConfirmLabel")}
                    <input
                        id={"register-with-email-password-control"}
                        type={"password"}
                        required
                        {...register("passwordControl", {required: true})}
                    />
                </label>

                <label className={styles.checkboxLabel} htmlFor={"register-with-email-agree-terms"}>
                    <input
                        id={"register-with-email-agree-terms"}
                        type={"checkbox"}
                        required
                        {...register("termsAgree")}
                        aria-invalid={errors.termsAgree ? "true" : "false"}
                    />
                    <div>
                        {t("registerWithEmailAgreeTerms")}
                        <a target="_blank" href={TERMS} className={styles.link}>
                            {` ${t("terms")}`}
                        </a>
                    </div>
                </label>

                <Button
                    disabled={isLoading}
                    loading={isLoading}
                    type="submit"
                    className={styles.submitButton}
                    variant={ButtonVariants.contrast}
                >
                    <div>{t("registerWithEmailPasswordRegisterLabel")}</div>
                </Button>

            </form>

            {hasOtherMethods && (
                <>
                    <div className="spacer-h-3"/>
                    <OrLine/>
                    <div className="spacer-h-3"/>

                    <div className={styles.otherMethodsContainer}>
                        {googleEnabled && (
                            <Button
                                disabled={isLoading}
                                href={authRedirectInfoResponse?.redirectUrl}
                                variant={ButtonVariants.outline}
                                className={styles.iconButton}
                            >
                                <IoLogoGoogle className={styles.icon}/>
                                <div>{t("loginScreenLoginWithGoogle")}</div>
                            </Button>
                        )}
                        {appleEnabled && (
                            <SignInWithAppleButton
                                disabled={isLoading}
                                className={styles.iconButton}
                                iconClassName={styles.icon}
                            />
                        )}
                    </div>
                </>
            )}

            <div className="spacer-h-3"/>

            <div className={styles.privacyPolicyContainer}>
                {t("registerPrivacyPolicyText")}
                <a className={styles.link} href={TERMS} target="_blank">
                    {` ${t("privacyPolicy")}`}
                </a>
            </div>
        </div>
    );
};

export default RegisterWithMailForm;
