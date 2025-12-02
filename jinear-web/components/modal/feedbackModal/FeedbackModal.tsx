import React, { useEffect, useState } from "react";
import styles from "./FeedbackModal.module.css";
import Modal from "@/components/modal/modal/Modal";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { closeFeedbackModal, selectFeedbackModalVisible } from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import useWindowSize from "@/hooks/useWindowSize";
import { useLazyGenerateCaptchaQuery } from "@/api/captchaApi";
import Button, { ButtonVariants } from "@/components/button";
import Logger from "@/utils/logger";
import { useSendToThreadUsingRobotsMutation } from "@/api/robotMessageOperationApi";
import { __DEV__ } from "@/utils/constants";
import { env } from "next-runtime-env";
import { SubmitHandler, useForm } from "react-hook-form";
import { selectCurrentAccount } from "@/slice/accountSlice";
import toast from "react-hot-toast";
import translations from "@/locals/strings";
import { solveCaptchaChallenge } from "@/utils/captcha-challenge-solver";

interface FeedbackModalProps {

}

interface IFeedbackForm {
  currentAccountId?: string;
  message: string;
  contact?: string;
}

const logger = Logger("FeedbackModal");

const suggestToken = !__DEV__ ? env("NEXT_PUBLIC_SUGGEST_TOKEN") : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMWtiYjY0YTlzZjExN3E2YTEwMDFocWM4ZiIsImlzX3JvYm90Ijp0cnVlLCJleHAiOjQ5MTgxMzMzNDgsImlhdCI6MTc2NDUzMzM0OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ST0JPVCJdfQ.Y69WXHMoSJxqoM7G2LkmKFunrd1RumERdoBmBR5_Du_iLEdLTWRbgiWdB-S2siEuGtLGnGqnTNQdNpVGzemK6w";
const suggestThreadId = !__DEV__ ? env("NEXT_PUBLIC_SUGGEST_THREAD_ID") : "01k8jrynssn1nawjg5xy2ejyjc";

const FeedbackModal: React.FC<FeedbackModalProps> = ({}) => {
  const { t } = useTranslation();
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const { isMobile } = useWindowSize();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectFeedbackModalVisible);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const textAreaRef = React.useRef<HTMLTextAreaElement>(null);
  const [generateCaptcha, { isError: isGenerateCaptchaError }] = useLazyGenerateCaptchaQuery();
  const [sendToThreadUsingRobots, { isError: isSendToThreadError }] = useSendToThreadUsingRobotsMutation();
  const { register, handleSubmit, setFocus, setValue, watch, reset } = useForm<IFeedbackForm>();

  const close = () => {
    setIsLoading(false);
    reset();
    dispatch(closeFeedbackModal());
  };

  useEffect(() => {
    if (currentAccount && visible) {
      setValue("currentAccountId", currentAccount.accountId);
      setValue("contact", currentAccount.email);
      setFocus("message");
    }
  }, [currentAccount, visible, setValue, setFocus]);

  const submitFeedbackForm: SubmitHandler<IFeedbackForm> = (data) => {
    logger.log({ data });
    if (!data.message || data.message.length === 0) {
      toast(t("feedbackModalPleaseProvideDetail"));
      return;
    }
    sendFeedback(data);
  };

  const sendFeedback = async (formData: IFeedbackForm) => {
    setIsLoading(true);
    if (suggestThreadId && suggestToken) {
      const response = await generateCaptcha({}).unwrap();
      const data = response.data;
      const result = await solveCaptchaChallenge(data);
      sendToThreadUsingRobots({
        body: {
          body: `accId: ${formData.currentAccountId},\ncontact: ${formData.contact},\nmessage: ${formData.message}`,
          captchaResolveVos: result
        },
        threadId: suggestThreadId,
        robotToken: suggestToken
      });
    }
    setIsLoading(false);
    close();
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("feedbackModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      onTitleCloeButtonClick={close}
    >
      <form
        id={"feedback-form"}
        autoComplete="off"
        className={styles.contentContainer}
        onSubmit={handleSubmit(submitFeedbackForm)}
        action="#">

        <input type="hidden" {...register("currentAccountId")} />

        <label className={styles.label} htmlFor={"message"}>
          {t("feedbackModalSubTitle")}
          <textarea
            id={"message"}
            rows={3}
            className={styles.textArea}
            {...register("message")}
          />
        </label>

        <label className={styles.label} htmlFor={"contact-mail"}>
          {t("feedbackModalContactMail")}
          <input
            type={"email"}
            {...register("contact")}
          />
        </label>

        <div className={"spacer-h-1"} />

        <Button
          disabled={isLoading}
          loading={isLoading}
          variant={ButtonVariants.contrast}
          type={"submit"}
        >
          {t("feedbackModalSubmit")}
        </Button>
      </form>
    </Modal>
  );
};

export default FeedbackModal;