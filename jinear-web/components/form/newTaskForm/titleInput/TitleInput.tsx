import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { focusAndOpenKeyboard } from "@/utils/htmlUtils";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister } from "react-hook-form";
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

  const isEnter = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.key === "Enter") {
      event.preventDefault?.();
    }
  };

  return (
    <input
      id={inputId}
      type={"text"}
      placeholder={t("newTaskModalTaskTitle")}
      className={cn(styles.input, labelClass)}
      onKeyDown={isEnter}
      {...register("title", { required: t("formRequiredField") })}
    />
  );
};

export default TitleInput;
