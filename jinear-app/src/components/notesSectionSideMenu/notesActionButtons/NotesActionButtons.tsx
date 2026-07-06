import React from 'react';
import styles from './NotesActionButtons.module.css';
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle.tsx";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuFilePen, LuSquarePen} from "react-icons/lu";

interface NotesActionButtonsProps {
    workspace: WorkspaceDto;
}

const NotesActionButtons: React.FC<NotesActionButtonsProps> = ({workspace}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <MenuGroupTitle label={t("sideMenuYourWorkspaceNotes")}/>

            <div className={styles.buttonsContainer}>
                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.brandColor}
                    className={styles.newTaskButton}
                    href={`/${workspace.username}/notes/new`}
                >
                    <LuFilePen className={'icon'}/>
                    <b>{t("sideMenuNewNote")}</b>
                </Button>
            </div>

        </div>
    );
}

export default NotesActionButtons;