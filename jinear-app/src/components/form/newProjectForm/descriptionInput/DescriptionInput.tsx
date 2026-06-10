import React, {type RefObject} from "react";
import styles from "./DescriptionInput.module.css";
import Tiptap, {type ITiptapRef} from "@/components/tiptap/Tiptap";
import useTranslation from "@/locals/useTranslation";
import type {UseFormRegister} from "react-hook-form";
import type {ProjectInitializeRequest} from "@/be/jinear-core";

interface DescriptionInputProps {
    register: UseFormRegister<ProjectInitializeRequest>;
    tiptapRef?: RefObject<ITiptapRef | null> | null;
}

const DescriptionInput: React.FC<DescriptionInputProps> = ({tiptapRef, register}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <Tiptap
                ref={tiptapRef}
                htmlInputId={"new-project-form-description"}
                // htmlInputId={"description"}
                registerKey={"description"}
                className={styles.input}
                editorClassName={styles.input}
                editorWrapperClassName={styles.editorWrapper}
                placeholder={t("newProjectFormDescriptionPlaceholder")}
                editable={true}
                hideActionBarWhenEmpty={true}
                register={register}
            />
        </div>
    );
};

export default DescriptionInput;