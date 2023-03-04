import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import TextEditorBasic from "@/components/TextEditorBasic/TextEditorBasic";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import dynamic from "next/dynamic";
import React from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";

const TextEditor = dynamic(import("@/components/textEditor/TextEditor"), {
  ssr: false,
});

interface DescriptionInputProps {
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
  labelClass: string;
  inputClass: string;
}

const DescriptionInput: React.FC<DescriptionInputProps> = ({ register, setValue, labelClass }) => {
  const { t } = useTranslation();
  return (
    <label className={labelClass} htmlFor={"new-task-description"}>
      {t("newTaskModalTaskDescription")}
      <PureClientOnly>
        <TextEditorBasic htmlInputId={"description"} register={register} formSetValue={setValue} />
      </PureClientOnly>
    </label>
  );
};

export default DescriptionInput;
