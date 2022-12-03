import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister } from "react-hook-form";

interface DueDateInputProps {
  register: UseFormRegister<TaskInitializeRequest>;
  labelClass: string;
}

const DueDateInput: React.FC<DueDateInputProps> = ({
  register,
  labelClass,
}) => {
  const { t } = useTranslation();

  return (
    <label className={labelClass} htmlFor={"new-task-due-date"}>
      {t("newTaskModalTaskDueDate")}
      <input type={"date"} id={"new-task-due-date"} {...register("dueDate")} />
    </label>
  );
};

export default DueDateInput;
