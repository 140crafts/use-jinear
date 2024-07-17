import { CalendarEventInitializeRequest, TaskInitializeRequest } from "@/model/be/jinear-core";
import { focusAndOpenKeyboard } from "@/utils/htmlUtils";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister } from "react-hook-form";
import styles from "./TitleInput.module.css";

interface TitleInputProps {
  register: UseFormRegister<CalendarEventInitializeRequest>;
  labelClass?: string;
}

const TitleInput: React.FC<TitleInputProps> = ({ register, labelClass }) => {
  const { t } = useTranslation();
  const inputId = "new-event-title";

  useEffect(() => {
    setTimeout(() => {
      focusAndOpenKeyboard(inputId);
    }, 250);
  }, []);

  return (
    <input
      id={inputId}
      type={"text"}
      placeholder={t("newEventModalEventSummary")}
      className={cn(styles.input, labelClass)}
      {...register("summary", { required: t("formRequiredField") })}
    />
  );
};

export default TitleInput;
