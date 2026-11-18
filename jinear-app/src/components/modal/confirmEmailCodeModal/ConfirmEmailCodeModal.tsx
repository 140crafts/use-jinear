import Button, {ButtonVariants} from "@/components/button";
import type {LocaleType} from "@/model/be/jinear-core";
import {useResendConfirmEmailMutation} from "@/store/api/accountApi";
import {
    closeConfirmEmailCodeModal,
    selectConfirmEmailCodeModalEmail,
    selectConfirmEmailCodeModalOnCancel,
    selectConfirmEmailCodeModalOnSubmitCode,
    selectConfirmEmailCodeModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import useTranslation from "@/locales/useTranslation";
import React, {type ChangeEvent, type FormEvent, useEffect, useRef, useState} from "react";
import {toast} from "react-hot-toast";
import Modal from "../modal/Modal";
import styles from "./ConfirmEmailCodeModal.module.css";

const CODE_LENGTH = 6;
const NON_DIGITS = /\D/g;

const logger = Logger("ConfirmEmailCodeModal");

const ConfirmEmailCodeModal: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectConfirmEmailCodeModalVisible);
    const email = useTypedSelector(selectConfirmEmailCodeModalEmail);
    const onSubmitCode = useTypedSelector(selectConfirmEmailCodeModalOnSubmitCode);
    const onCancel = useTypedSelector(selectConfirmEmailCodeModalOnCancel);
    const [resendConfirmEmail, {isLoading: isResendLoading}] = useResendConfirmEmailMutation();
    const inputRef = useRef<HTMLInputElement>(null);
    const [code, setCode] = useState<string>("");
    const [isConfirmLoading, setIsConfirmLoading] = useState<boolean>(false);
    const isCodeComplete = code.length == CODE_LENGTH;
    const infoText = t("confirmEmailCodeModalText").split("${email}").join(email || "");

    useEffect(() => {
        if (!visible) {
            return;
        }
        const focusTimeout = setTimeout(() => inputRef.current?.focus(), 250);
        return () => clearTimeout(focusTimeout);
    }, [visible]);

    const onCodeChange = (e: ChangeEvent<HTMLInputElement>) => {
        const digits = e.target.value.replace(NON_DIGITS, "");
        setCode(digits.slice(0, CODE_LENGTH));
    };

    const close = () => {
        setCode("");
        dispatch(closeConfirmEmailCodeModal());
    };

    const cancel = () => {
        close();
        onCancel?.();
    };

    const confirm = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!onSubmitCode || !isCodeComplete || isConfirmLoading) {
            return;
        }
        setIsConfirmLoading(true);
        try {
            await onSubmitCode(code);
            close();
        } catch (error) {
            logger.log({confirmEmailError: error});
        } finally {
            setIsConfirmLoading(false);
        }
    };

    const resend = async () => {
        if (!email || isResendLoading) {
            return;
        }
        try {
            await resendConfirmEmail({email, locale: t("localeType") as LocaleType}).unwrap();
            toast(t("confirmEmailCodeModalResendSuccess"));
        } catch (error) {
            logger.log({resendConfirmEmailError: error});
        }
    };

    return (
        <Modal visible={visible} title={t("confirmEmailCodeModalTitle")} requestClose={cancel} width={"default"}>
            <form className={styles.form} onSubmit={confirm}>
                <div className={styles.infoText}>{infoText}</div>
                <input
                    ref={inputRef}
                    type={"text"}
                    inputMode={"numeric"}
                    autoComplete={"one-time-code"}
                    placeholder={t("loginScreenCodePlaceholder")}
                    onChange={onCodeChange}
                    value={code}
                />
                <div className={styles.actionBarContainer}>
                    <Button loading={isResendLoading} disabled={isResendLoading} onClick={resend}>
                        {t("confirmEmailCodeModalResendButton")}
                    </Button>
                    <Button
                        type={"submit"}
                        variant={ButtonVariants.contrast}
                        loading={isConfirmLoading}
                        disabled={isConfirmLoading || !isCodeComplete}
                    >
                        {t("continue")}
                    </Button>
                </div>
            </form>
        </Modal>
    );
};

export default ConfirmEmailCodeModal;
