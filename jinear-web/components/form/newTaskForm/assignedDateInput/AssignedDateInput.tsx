import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister } from "react-hook-form";

interface AssignedDateInputProps {
  register: UseFormRegister<TaskInitializeRequest>;
  labelClass: string;
}

const AssignedDateInput: React.FC<AssignedDateInputProps> = ({
  register,
  labelClass,
}) => {
  const { t } = useTranslation();
  return (
    <label className={labelClass} htmlFor={"new-task-assigned-date"}>
      {t("newTaskModalTaskAssignedDate")}
      <input
        type={"date"}
        id={"new-task-assigned-date"}
        {...register("assignedDate")}
      />
    </label>
  );
};

export default AssignedDateInput;
