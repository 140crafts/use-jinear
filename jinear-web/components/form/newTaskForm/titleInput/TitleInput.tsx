import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister } from "react-hook-form";

interface TitleInputProps {
  register: UseFormRegister<TaskInitializeRequest>;
  labelClass: string;
}

const TitleInput: React.FC<TitleInputProps> = ({ register, labelClass }) => {
  const { t } = useTranslation();
  useEffect(() => {
    setTimeout(() => {
      document.getElementById("new-task-title")?.click();
    }, 250);
  }, []);
  return (
    <label className={labelClass} htmlFor={"new-task-title"}>
      {`${t("newTaskModalTaskTitle")} *`}
      <input id={"new-task-title"} type={"text"} {...register("title", { required: t("formRequiredField") })} />
    </label>
  );
};

export default TitleInput;
