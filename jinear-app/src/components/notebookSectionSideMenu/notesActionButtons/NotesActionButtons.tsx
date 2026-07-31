import React from 'react';
import styles from './NotesActionButtons.module.css';
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle.tsx";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuFilePen} from "react-icons/lu";
import {useCreateNoteDraft} from "@/hooks/useCreateNoteDraft.ts";

interface NotesActionButtonsProps {
    workspace: WorkspaceDto;
}

const NotesActionButtons: React.FC<NotesActionButtonsProps> = ({workspace}) => {
    const {t} = useTranslation();
    const onNewNote = useCreateNoteDraft(workspace);

    return (
        <div className={styles.container}>
            <MenuGroupTitle label={t("sideMenuYourWorkspaceNotes")}/>

            <div className={styles.buttonsContainer}>
                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.brandColor}
                    className={styles.newTaskButton}
                    onClick={onNewNote}
                >
                    <LuFilePen className={'icon'}/>
                    <b>{t("sideMenuNewNote")}</b>
                </Button>
            </div>

        </div>
    );
}

export default NotesActionButtons;