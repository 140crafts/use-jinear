import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister } from "react-hook-form";

interface DescriptionInputProps {
  register: UseFormRegister<TaskInitializeRequest>;
  labelClass: string;
  inputClass: string;
}

const DescriptionInput: React.FC<DescriptionInputProps> = ({
  register,
  labelClass,
  inputClass,
}) => {
  const { t } = useTranslation();
  return (
    <label className={labelClass} htmlFor={"new-task-description"}>
      {t("newTaskModalTaskDescription")}
      <textarea
        id={"new-task-description"}
        className={inputClass}
        {...register("description")}
      />
    </label>
  );
};

export default DescriptionInput;
