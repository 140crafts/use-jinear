import React from 'react';
import styles from './NoteActionBar.module.css';
import cn from "classnames";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {useOnlineStatus} from "@/hooks/useOnlineStatus.ts";

const NoteActionBar: React.FC = () => {
    const {t} = useTranslation();
    const {status, isPendingCreate} = useNoteEditorContext();
    const online = useOnlineStatus();

    // Display-only projection of the sync state — first match wins.
    const statusLabel = !online ? t('noteSyncStatusOffline')
        : isPendingCreate || status === "saved_locally" ? t('noteSyncStatusSavedLocally')
            : status === "syncing" ? t('noteSyncStatusSyncing')
                : status === "synced" ? t('noteSyncStatusSynced')
                    : status === "error" ? t('noteSyncStatusError')
                        : null;

    const isError = online && status === "error";

    return (
        <div className={styles.container}>
            {/*{t('noteEditorActionBarPublish' : 'noteEditorActionBarSave')}*/}
            {statusLabel &&
                <span className={cn(styles.syncStatus, isError && styles.syncStatusError)}>
                    {statusLabel}
                </span>}
        </div>
    );
}

export default NoteActionBar;
