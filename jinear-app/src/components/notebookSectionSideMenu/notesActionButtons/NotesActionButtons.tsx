import React from 'react';
import styles from './NotesActionButtons.module.css';
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle.tsx";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuFilePen} from "react-icons/lu";
import {UUID} from "@/util/UUID.ts";
import {useNavigate} from "react-router-dom";
import {DRAFT_ID_PREFIX, DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";

interface NotesActionButtonsProps {
    workspace: WorkspaceDto;
}

const NotesActionButtons: React.FC<NotesActionButtonsProps> = ({workspace}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();

    const onNewNote = () => {
        const draftId = UUID();
        const newNoteRoute = `/${workspace.username}/notebook/${DRAFTS_NOTEBOOK_ID}/note/${DRAFT_ID_PREFIX}-${draftId}`
        navigate(newNoteRoute);
    }

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