import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import Tiptap, {type ITiptapRef} from "@/components/tiptap/Tiptap";
import {useToggle} from "@/hooks/useToggle";
import {type RichTextDto} from "@/model/be/jinear-core";
import {useUpdateTaskDescriptionMutation} from "@/store/api/taskUpdateApi";
import Logger from "@/util/logger";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useRef, useState} from "react";
import {LuPencil} from "react-icons/lu";
import styles from "./TaskDescription.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TaskDescriptionProps {
    taskId: string;
    description?: RichTextDto | null;
}

const logger = Logger("TaskDescription");

const TaskDescription: React.FC<TaskDescriptionProps> = ({taskId, description}) => {
    const {t} = useTranslation();
    const [readOnly, toggleReadOnly] = useToggle(true);
    const [initialValue, setInitialValue] = useState(description?.value);
    const [updateTaskDescription, {
        isSuccess: isUpdateSuccess,
        isLoading: isUpdateLoading
    }] = useUpdateTaskDescriptionMutation();
    const tiptapRef = useRef<ITiptapRef>(null);

    useEffect(() => {
        if (description?.value) {
            setInitialValue(description?.value);
        }
    }, [description?.value]);

    const save = () => {
        const input: HTMLInputElement | null = document.getElementById(`${description?.richTextId}`) as HTMLInputElement;
        if (input) {
            const value = input?.value || "";
            const req = {
                taskId,
                body: {
                    description: value
                }
            };
            updateTaskDescription(req);
        }
    };

    const toggle = () => {
        toggleReadOnly();
        if (!readOnly) {
            save();
        } else {
            tiptapRef?.current?.focus();
        }
    };

    const cancel = () => {
        toggleReadOnly();
        setInitialValue("");
        setTimeout(() => {
            setInitialValue(description?.value);
        }, 100);
    };

    return (
        <div className={cn(styles.container)}>
            <Tiptap
                ref={tiptapRef}
                content={initialValue}
                editable={!readOnly}
                placeholder={t("taskDetalPageTaskDescription")}
                htmlInputId={`${description?.richTextId}`}
            />
            {readOnly && (
                <Button
                    onClick={toggle}
                    className={styles.editButton}
                    variant={ButtonVariants.contrast}
                    data-tooltip-right={t("taskDescriptionEdit")}
                >
                    <LuPencil/>
                </Button>
            )}

            {!readOnly && <div className={'spacer-h-2'}/>}

            <div className={styles.actionContainer}>
                {isUpdateLoading && (
                    <div className={styles.loadingContainer}>
                        <CircularLoading size={14}/>
                        <div>{t("taskDescriptionSaving")}</div>
                    </div>
                )}

                {!readOnly && (
                    <Button
                        disabled={isUpdateLoading}
                        loading={isUpdateLoading}
                        heightVariant={ButtonHeight.mid}
                        variant={readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast}
                        onClick={toggle}
                    >
                        {t("taskDescriptionSave")}
                    </Button>
                )}
                {!readOnly && (
                    <Button disabled={isUpdateLoading} heightVariant={ButtonHeight.mid} variant={ButtonVariants.filled2}
                            onClick={cancel}>
                        {t("taskDescriptionCancel")}
                    </Button>
                )}
            </div>
        </div>
    );
};

export default TaskDescription;
