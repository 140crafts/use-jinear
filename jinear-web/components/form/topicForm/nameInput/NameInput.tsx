import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister } from "react-hook-form";
import { ITopicForm } from "../TopicForm";

interface NameInputProps {
  register: UseFormRegister<ITopicForm>;
  labelClass: string;
}

const NameInput: React.FC<NameInputProps> = ({ register, labelClass }) => {
  const { t } = useTranslation();
  return (
    <label className={labelClass} htmlFor={"topic-name"}>
      {`${t("topicFormName")} *`}
      <input
        id={"topic-name"}
        type={"text"}
        {...register("name", { required: t("formRequiredField") })}
      />
    </label>
  );
};

export default NameInput;
