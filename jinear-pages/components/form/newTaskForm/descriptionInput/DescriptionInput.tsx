import { PureClientOnly } from "@/components/clientOnly/ClientOnly";
import Tiptap from "@/components/tiptap/Tiptap";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";

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
        <Tiptap
          placeholder={t("taskDetalPageTaskDescription")}
          register={register}
          formSetValue={setValue}
          htmlInputId={`description`}
        />
      </PureClientOnly>
    </label>
  );
};

export default DescriptionInput;
