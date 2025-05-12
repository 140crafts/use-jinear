import React, { RefObject } from "react";
import styles from "./DescriptionInput.module.css";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import useTranslation from "@/locals/useTranslation";
import { UseFormRegister } from "react-hook-form";
import { InitializeMilestoneRequest } from "@/be/jinear-core";

interface DescriptionInputProps {
  register: UseFormRegister<InitializeMilestoneRequest>;
  tiptapRef?: RefObject<ITiptapRef> | null;
}

const DescriptionInput: React.FC<DescriptionInputProps> = ({ tiptapRef, register }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <label className={styles.label}>
        {t("newMilestoneFormDescriptionPlaceholder")}
      </label>
      <Tiptap
        ref={tiptapRef}
        htmlInputId={"new-milestone-form-description"}
        registerKey={"description"}
        editable={true}
        hideActionBarWhenEmpty={false}
        register={register}
      />
    </div>
  );
};

export default DescriptionInput;