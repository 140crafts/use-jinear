import Tiptap from "@/components/tiptap/Tiptap";
import type {TaskInitializeRequest} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import type {UseFormRegister, UseFormSetValue} from "react-hook-form";

interface DescriptionInputProps {
    register: UseFormRegister<TaskInitializeRequest>;
    setValue: UseFormSetValue<TaskInitializeRequest>;
    labelClass: string;
    inputClass: string;
}

const DescriptionInput: React.FC<DescriptionInputProps> = ({register, setValue, labelClass}) => {
    const {t} = useTranslation();
    return (
        <label className={labelClass} htmlFor={"new-task-description"}>
            {t("newTaskModalTaskDescription")}
            <Tiptap
                placeholder={t("taskDetalPageTaskDescription")}
                register={register}
                formSetValue={setValue}
                htmlInputId={`description`}
            />
        </label>
    );
};

export default DescriptionInput;
