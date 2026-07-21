import React from 'react';
import styles from './NoteActionBar.module.scss';
import cn from "classnames";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {useOnlineStatus} from "@/hooks/useOnlineStatus.ts";
import NotePath from "@/components/note-editor/note-action-bar/note-path/NotePath.tsx";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";

const NoteActionBar: React.FC = () => {
    const {t} = useTranslation();
    const {workspace, note, status, isPendingCreate} = useNoteEditorContext();
    const online = useOnlineStatus();

    const statusLabel = !online ? t('noteSyncStatusOffline')
        : isPendingCreate || status === "saved_locally" ? t('noteSyncStatusSavedLocally')
            : status === "syncing" ? t('noteSyncStatusSyncing')
                : status === "synced" ? t('noteSyncStatusSynced')
                    : status === "error" ? t('noteSyncStatusError')
                        : null;

    const isError = online && status === "error";

    return (
        <div className={styles.container}>
            <NotePath/>

            <div className={'flex-1'}/>

            <div className={styles.actionButtonContainer}>
                {statusLabel &&
                    <Button
                        disabled={true}
                        variant={ButtonVariants.outline}
                        heightVariant={ButtonHeight.short}
                        className={cn(styles.syncStatus, isError && styles.syncStatusError)}
                    >
                        {statusLabel}
                    </Button>
                }
            </div>

        </div>
    );
}

export default NoteActionBar;
