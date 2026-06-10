import { InitializeMilestoneRequest, ProjectInitializeRequest } from "@/model/be/jinear-core";
import { focusAndOpenKeyboard } from "@/utils/htmlUtils";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister } from "react-hook-form";
import styles from "./TitleInput.module.css";
import cn from "classnames";

interface TitleInputProps {
  register: UseFormRegister<InitializeMilestoneRequest>;
  labelClass?: string;
}

const TitleInput: React.FC<TitleInputProps> = ({ register, labelClass }) => {
  const { t } = useTranslation();
  const inputId = "new-milestone-title";

  useEffect(() => {
    setTimeout(() => {
      focusAndOpenKeyboard(inputId);
    }, 250);
  }, []);

  const isEnter = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.key === "Enter") {
      event.preventDefault?.();
    }
  };

  return (
    <label className={styles.label} htmlFor={inputId}>
      {t("newMilestoneFormTitle")}
      <input
        id={inputId}
        type={"text"}
        onKeyDown={isEnter}
        {...register("title", { required: t("formRequiredField") })}
      />
    </label>
  );
};

export default TitleInput;
