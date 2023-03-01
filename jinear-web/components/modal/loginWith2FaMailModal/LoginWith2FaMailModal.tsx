import Button, { ButtonVariants } from "@/components/button";
import { LocaleType } from "@/model/be/jinear-core";
import {
  useEmailLoginTokenRequestMutation,
  useEmailOtpLoginCompleteMutation,
} from "@/store/api/authApi";

import {
  changeLoginWith2FaMailModalVisibility,
  selectLoginWith2FaMailModalVisible,
} from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { validateEmail } from "@/utils/validator";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef, useState } from "react";
import { toast } from "react-hot-toast";
import { useDispatch } from "react-redux";
import Modal from "../modal/Modal";
import CodeStage from "./codeStage/CodeStage";
import EmailStage from "./emailStage/EmailStage";
import styles from "./LoginWith2FaMailModal.module.css";

interface LoginWith2FaMailModalProps {}

const INITIAL_COUNTER = 300;

const logger = Logger("LoginWith2FaMailModal");

const LoginWith2FaMailModal: React.FC<LoginWith2FaMailModalProps> = ({}) => {
  const [
    requestToken,
    {
      data: requestTokenResponse,
      error: requestTokenError,
      isLoading: isRequestTokenLoading,
      reset: resetTokenCache,
    },
  ] = useEmailLoginTokenRequestMutation();
  const [
    login,
    {
      isSuccess: isLoginRequestSuccess,
      isLoading: isLoginRequestLoading,
      reset: resetLoginCache,
    },
  ] = useEmailOtpLoginCompleteMutation();

  const dispatch = useDispatch();
  const { t } = useTranslation();
  const loginModalVisible = useTypedSelector(
    selectLoginWith2FaMailModalVisible
  );
  const step = requestTokenResponse?.csrf == null ? 0 : 1;

  const emailInputRef = useRef<HTMLInputElement>(null);
  const codeInputRef = useRef<HTMLInputElement>(null);

  const [counter, setCounter] = useState<number>(INITIAL_COUNTER);

  useEffect(() => {
    let id: number;
    if (step == 1) {
      id = window.setInterval(() => {
        setCounter((prevCount) => {
          let next = prevCount - 1;
          if (next <= 0) {
            clearInterval(id);
            return 0;
          }
          return next;
        });
      }, 1000);
    }
    return () => {
      clearInterval(id);
    };
  }, [step]);

  useEffect(() => {
    if (isLoginRequestSuccess) {
      close();
    }
  }, [isLoginRequestSuccess]);

  const close = () => {
    resetTokenCache();
    resetLoginCache();
    dispatch(changeLoginWith2FaMailModalVisibility({ visible: false }));
  };

  const requestTokenCall = async () => {
    const email = emailInputRef?.current?.value || "";
    if (!validateEmail(email)) {
      toast(t("loginScreenInvalidMailToast"));
      return;
    }
    const locale = (t("localeType") || "EN") as LocaleType;
    await requestToken({ email, locale });
  };

  const loginCall = () => {
    const code = codeInputRef?.current?.value || "";
    if (code?.length < 4 || !requestTokenResponse) {
      return;
    }
    login({
      email: requestTokenResponse?.email,
      csrf: requestTokenResponse?.csrf,
      code,
      provider: "OTP_MAIL",
      locale: t("localeType") as LocaleType,
    });
  };

  const onPrimaryButtonClick = () => {
    step == 0 ? requestTokenCall() : loginCall();
  };

  const onSecondaryButtonClick = () => {
    if (step == 0) {
      close();
      return;
    }
    if (counter > 0) {
      toast(
        t("loginScreenResendWait")
          ?.split("${timeout}")
          .join(counter + "")
      );
      return;
    }
    resetTokenCache();
    resetLoginCache();
  };

  return (
    <Modal
      visible={loginModalVisible}
      requestClose={close}
      title={t("loginScreenTitle")}
    >
      {step === 0 && (
        <EmailStage
          onPrimaryButtonClick={onPrimaryButtonClick}
          className={styles.stage}
          infoClassName={styles.infoText}
          inputRef={emailInputRef}
        />
      )}
      {step === 1 && (
        <CodeStage
          onPrimaryButtonClick={onPrimaryButtonClick}
          className={styles.stage}
          infoClassName={styles.infoText}
          inputRef={codeInputRef}
        />
      )}

      <div className="spacer-h-4" />

      <Button
        loading={isRequestTokenLoading || isLoginRequestLoading}
        disabled={isRequestTokenLoading || isLoginRequestLoading}
        variant={ButtonVariants.contrast}
        onClick={onPrimaryButtonClick}
      >
        {t(step === 0 ? "loginScreenEmailSendCode" : "continue")}
      </Button>
      <div className="spacer-h-2" />
      <Button variant={ButtonVariants.default} onClick={onSecondaryButtonClick}>
        {step === 0
          ? t("cancel")
          : t("loginScreenCodeCancel") + `        ${counter}`}
      </Button>
    </Modal>
  );
};

export default LoginWith2FaMailModal;
