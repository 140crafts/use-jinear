import React from 'react';
import styles from './NotesActionButtons.module.css';
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle.tsx";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuFilePen} from "react-icons/lu";
import {UUID} from "@/util/UUID.ts";
import {useNavigate} from "react-router-dom";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {useAppDispatch} from "@/store";
import {addPendingDraft} from "@/slice/noteDraftsSlice.ts";

interface NotesActionButtonsProps {
    workspace: WorkspaceDto;
}

const NotesActionButtons: React.FC<NotesActionButtonsProps> = ({workspace}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const onNewNote = () => {
        const draftId = UUID();
        dispatch(addPendingDraft({draftId, workspaceId: workspace.workspaceId}));
        navigate(`/${workspace.username}/notebook/${DRAFTS_NOTEBOOK_ID}/note/${draftId}`);
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