import Button, {ButtonVariants} from "@/components/button";
import OrLine from "@/components/or-line/or-line.tsx";
import type {LocaleType} from "@/model/be/jinear-core";
import Logger from "@/util/logger";
import cn from "classnames";
import {format} from "date-fns";
import useTranslation from "@/locales/useTranslation.ts";
import React, {useEffect} from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import toast from "react-hot-toast";

import {IoLogoGoogle, IoMail} from "react-icons/io5";
import {useDispatch} from "react-redux";
import styles from "./LoginWithMailForm.module.css";
import {setAuthStateAsNotDecided} from "@/slice/accountSlice";
import SignInWithAppleButton from "@/components/sign-in-with-apple-button/SignInWithAppleButton";
import FormTitle from "@/components/form/form-title/FormTitle.tsx";
import {Link, useNavigate} from "react-router-dom";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants.ts";
import {useLoginWithPasswordMutation} from "@/store/api/authApi.ts";
import {useRetrieveLoginRedirectInfoQuery} from "@/store/api/googleOAuthApi.ts";
import {changeLoginWith2FaMailModalVisibility} from "@/slice/modalSlice.ts";


interface LoginWithMailFormProps {
    initialEmail?: string;
    className?: string;
}

export interface ILoginWithMailForm {
    email: string;
    password: string;
}

const logger = Logger("LoginWithMailForm");

const LoginWithMailForm: React.FC<LoginWithMailFormProps> = ({className, initialEmail}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [loginWithPassword, {isSuccess, isError, isLoading}] = useLoginWithPasswordMutation();
    const {
        data: authRedirectInfoResponse,
    } = useRetrieveLoginRedirectInfoQuery();
    const {register, setValue, handleSubmit, setFocus} = useForm<ILoginWithMailForm>();

    useEffect(() => {
        if (isSuccess && !isError) {
            logger.log("Login with password success");
            toast(t("loginSuccessToast"));
            navigate(ROUTE_IF_LOGGED_IN, { replace: true })
        }
    }, [isSuccess, isError, t, navigate]);

    useEffect(() => {
        if (initialEmail) {
            setValue("email", initialEmail);
            setFocus("password");
        }
    }, [initialEmail, setFocus, setValue]);

    const submit: SubmitHandler<ILoginWithMailForm> = (data) => {
        dispatch(setAuthStateAsNotDecided());
        if (isLoading) {
            return;
        }
        loginWithPassword({
            ...data,
            locale: t("localeType") as LocaleType,
            timeZone: format(new Date(), "OOOO")
        });
    };

    const pop2FaMailModal = () => {
        dispatch(changeLoginWith2FaMailModalVisibility({visible: true}));
    };

    return (
        <div className={cn(styles.container, className)}>
            <FormTitle
                title={t("loginWithEmailFormTitle")}
                subTitle={t("loginWithEmailFormSubTitle")}
                linkLabel={t("loginWithEmailFormTitleLink")}
                link={"/register"}
            />

            <form autoComplete="off" id={"login-with-email-form"} className={styles.form}
                  onSubmit={handleSubmit(submit)}
                  action="#">
                <label className={styles.label} htmlFor={"login-with-email-email"}>
                    {t("loginWithEmailEmailLabel")}
                    <input id={"login-with-email-email"} type={"email"} {...register("email")} />
                </label>

                <label className={styles.label} htmlFor={"login-with-email-password"}>
                    {t("loginWithEmailPasswordLabel")}
                    <input id={"login-with-email-password"} type={"password"} {...register("password")} />
                </label>

                <Link className={styles.forgotPasswordLink} to={"/forgot-password"}>
                    {t("forgotPasswordLinkLabel")}
                </Link>

                <Button
                    disabled={isLoading}
                    loading={isLoading}
                    type="submit"
                    className={styles.submitButton}
                    variant={ButtonVariants.contrast}
                >
                    <div>{t("loginWithEmailLoginButtonLabel")}</div>
                </Button>
            </form>

            <div className="spacer-h-1"/>
            <OrLine/>
            <div className="spacer-h-1"/>

            <div className={styles.otherMethodsContainer}>
                <Button
                    disabled={isLoading}
                    href={authRedirectInfoResponse?.redirectUrl}
                    variant={ButtonVariants.outline}
                    className={styles.iconButton}
                >
                    <IoLogoGoogle className={styles.icon}/>
                    <div>{t("loginScreenLoginWithGoogle")}</div>
                </Button>
                <SignInWithAppleButton
                    disabled={isLoading}
                    className={styles.iconButton}
                    iconClassName={styles.icon}
                />
                <Button onClick={pop2FaMailModal} variant={ButtonVariants.outline} className={styles.iconButton}>
                    <IoMail className={styles.icon}/>
                    <div>{t("loginWith2FaMail")}</div>
                </Button>
            </div>
        </div>
    );
};

export default LoginWithMailForm;
