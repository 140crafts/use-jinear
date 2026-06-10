import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister } from "react-hook-form";
import { ITopicForm } from "../TopicForm";
import styles from "./TagInput.module.css";

interface TagInputProps {
  register: UseFormRegister<ITopicForm>;
  labelClass: string;
}

const TagInput: React.FC<TagInputProps> = ({ register, labelClass }) => {
  const { t } = useTranslation();

  return (
    <label className={labelClass} htmlFor={"topic-tag"}>
      {`${t("topicFormTag")} *`}
      <input id={"topic-tag"} type={"text"} maxLength={16} {...register("tag", { required: t("formRequiredField") })} />
      <span className={styles.info}>{t("topicFormTagExplaination")}</span>
    </label>
  );
};

export default TagInput;
