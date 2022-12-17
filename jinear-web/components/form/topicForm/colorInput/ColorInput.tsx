import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister, UseFormWatch } from "react-hook-form";
import { ITopicForm } from "../TopicForm";
import styles from "./ColorInput.module.css";

interface ColorInputProps {
  register: UseFormRegister<ITopicForm>;
  watch: UseFormWatch<ITopicForm>;
  labelClass: string;
}

const ColorInput: React.FC<ColorInputProps> = ({
  register,
  watch,
  labelClass,
}) => {
  const { t } = useTranslation();
  return (
    <label className={cn(labelClass, styles.container)} htmlFor={"topic-color"}>
      {`${t("topicFormColor")}`}
      <input
        id={"topic-color"}
        type={"color"}
        {...register("color", { required: t("formRequiredField") })}
      />
    </label>
  );
};

export default ColorInput;
