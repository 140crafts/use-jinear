import Button, {ButtonVariants} from "@/components/button";
import type {LocaleType} from "@/model/be/jinear-core";
import {useConfirmEmailMutation, useResendConfirmEmailMutation} from "@/store/api/accountApi";
import {
    closeConfirmEmailCodeModal,
    selectConfirmEmailCodeModalEmail,
    selectConfirmEmailCodeModalOnConfirmed,
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

const logger = Logger("ConfirmEmailCodeModal");

const ConfirmEmailCodeModal: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectConfirmEmailCodeModalVisible);
    const email = useTypedSelector(selectConfirmEmailCodeModalEmail);
    const onConfirmed = useTypedSelector(selectConfirmEmailCodeModalOnConfirmed);
    const [confirmEmail, {isLoading: isConfirmLoading}] = useConfirmEmailMutation();
    const [resendConfirmEmail, {isLoading: isResendLoading}] = useResendConfirmEmailMutation();
    const inputRef = useRef<HTMLInputElement>(null);
    const [code, setCode] = useState<string>("");
    const infoText = t("confirmEmailCodeModalText").split("${email}").join(email || "");

    useEffect(() => {
        if (!visible) {
            return;
        }
        const focusTimeout = setTimeout(() => inputRef.current?.focus(), 250);
        return () => clearTimeout(focusTimeout);
    }, [visible]);

    const onCodeChange = (e: ChangeEvent<HTMLInputElement>) => {
        setCode(e.target.value.trim());
    };

    const close = () => {
        setCode("");
        dispatch(closeConfirmEmailCodeModal());
    };

    const confirm = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!email || code.length != CODE_LENGTH || isConfirmLoading) {
            return;
        }
        try {
            await confirmEmail({email, code}).unwrap();
            close();
            toast(t("confirmEmailCodeModalSuccess"));
            onConfirmed?.();
        } catch (error) {
            logger.log({confirmEmailError: error});
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
        <Modal visible={visible} title={t("confirmEmailCodeModalTitle")} requestClose={close} width={"default"}>
            <form className={styles.form} onSubmit={confirm}>
                <div>{infoText}</div>
                <input
                    ref={inputRef}
                    type={"text"}
                    inputMode={"numeric"}
                    autoComplete={"one-time-code"}
                    maxLength={CODE_LENGTH}
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
                        disabled={isConfirmLoading}
                    >
                        {t("continue")}
                    </Button>
                </div>
            </form>
        </Modal>
    );
};

export default ConfirmEmailCodeModal;
