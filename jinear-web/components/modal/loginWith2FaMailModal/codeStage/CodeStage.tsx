import { TextInput } from "@/components/textInput/TextInput";
import useTranslation from "locales/useTranslation";
import React, { RefObject, useEffect } from "react";
import styles from "./CodeStage.module.css";

interface CodeStageProps {
  onPrimaryButtonClick: () => void;
  className: string;
  infoClassName: string;
  inputRef: RefObject<HTMLInputElement>;
}

const CodeStage: React.FC<CodeStageProps> = ({ onPrimaryButtonClick, className, infoClassName, inputRef }) => {
  const { t } = useTranslation();
  useEffect(() => {
    setTimeout(() => {
      inputRef?.current?.focus();
    }, 500);
  }, []);
  return (
    <div className={className}>
      <h3>{t("loginScreenCodeText")}</h3>
      <TextInput
        ref={inputRef}
        type={"number"}
        className={styles.codeInput}
        onEnterCallback={onPrimaryButtonClick}
        placeholder={t("loginScreenCodePlaceholder")}
      />
      <span className={infoClassName} dangerouslySetInnerHTML={{ __html: t("loginScreenCodeSubText") }} />
    </div>
  );
};

export default CodeStage;
