import { TextInput } from "@/components/textInput/TextInput";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { RefObject, useEffect } from "react";

interface EmailStageProps {
  onPrimaryButtonClick: () => void;
  className: string;
  infoClassName: string;
  inputRef: RefObject<HTMLInputElement>;
}

const logger = Logger("LoginModal");

const EmailStage: React.FC<EmailStageProps> = ({ onPrimaryButtonClick, className, infoClassName, inputRef }) => {
  const { t } = useTranslation();

  useEffect(() => {
    setTimeout(() => {
      inputRef?.current?.focus();
    }, 500);
  }, []);

  const onEnterCallBack = () => {
    onPrimaryButtonClick();
  };

  return (
    <div className={className}>
      <h3>{t("loginScreenText")}</h3>
      <TextInput ref={inputRef} type={"email"} onEnterCallback={onEnterCallBack} placeholder={t("loginScreenEmailPlaceholder")} />
      <span className={infoClassName} dangerouslySetInnerHTML={{ __html: t("loginScreenEmailSubText") }} />
    </div>
  );
};

export default EmailStage;
