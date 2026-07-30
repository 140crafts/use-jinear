import type { TaskInitializeRequest } from "@/model/be/jinear-core";
import { focusAndOpenKeyboard } from "@/util/htmlUtils";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, { useEffect } from "react";
import type { UseFormRegister } from "react-hook-form";
import styles from "./TitleInput.module.css";

interface TitleInputProps {
  register: UseFormRegister<TaskInitializeRequest>;
  labelClass?: string;
}

const TitleInput: React.FC<TitleInputProps> = ({ register, labelClass }) => {
  const { t } = useTranslation();
  const inputId = "new-task-title";

  useEffect(() => {
    setTimeout(() => {
      focusAndOpenKeyboard(inputId);
    }, 250);
  }, []);

  const isEnter = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (event.key === "Enter") {
      event.preventDefault?.();
    }
  };

  return (
    <textarea
      id={inputId}
      placeholder={t("newTaskModalTaskTitle")}
      className={cn(styles.input, labelClass)}
      onKeyDown={isEnter}
      {...register("title", { required: t("newTaskFormTitleRequiredField") })}
    />
  );
};

export default TitleInput;
