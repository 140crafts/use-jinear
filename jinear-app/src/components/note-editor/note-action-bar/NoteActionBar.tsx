import React from 'react';
import styles from './NoteActionBar.module.scss';
import cn from "classnames";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import useTranslation, {type StringKeys} from "@/locals/useTranslation.ts";
import {useOnlineStatus} from "@/hooks/useOnlineStatus.ts";
import NotePath from "@/components/note-editor/note-action-bar/note-path/NotePath.tsx";
import {LuCircleCheck, LuCloudAlert, LuCloudOff, LuCloudUpload, LuLoaderCircle, LuTrash2,} from "react-icons/lu";
import type {IconType} from "react-icons/lib";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useAppDispatch} from "@/store/hooks.ts";
import {closeDialogModal, popDialogModal} from "@/slice/modalSlice.ts";
import {useDeleteNoteMutation} from "@/api/noteOperationApi.ts";
import {useNavigate} from "react-router-dom";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {removePendingDraft} from "@/slice/noteDraftsSlice.ts";
import {deleteLocalDoc} from "@/components/tiptap/crdt/deleteLocalDoc.ts";
import {store} from "@/store";

type SyncState = "offline" | "saved_locally" | "syncing" | "synced" | "error";
type SyncUi = { Icon: IconType; key: StringKeys; spin?: boolean };

const SYNC_UI: Record<SyncState, SyncUi> = {
    offline: {Icon: LuCloudOff, key: "noteSyncStatusOffline"},
    saved_locally: {Icon: LuCloudUpload, key: "noteSyncStatusSavedLocally"},
    syncing: {Icon: LuLoaderCircle, key: "noteSyncStatusSyncing", spin: true},
    synced: {Icon: LuCircleCheck, key: "noteSyncStatusSynced"},
    error: {Icon: LuCloudAlert, key: "noteSyncStatusError"},
};

const NoteActionBar: React.FC = () => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {workspace, note, noteId, docKey, status, isPendingCreate} = useNoteEditorContext();
    const online = useOnlineStatus();
    const dispatch = useAppDispatch();
    const [deleteNote] = useDeleteNoteMutation();

    const syncState = !online
        ? "offline"
        : isPendingCreate || status === "saved_locally"
            ? "saved_locally"
            : status;

    const ui = SYNC_UI[syncState as keyof typeof SYNC_UI];

    const isError = online && status === "error";

    const deleteCurrentNote = () => {
        if (!workspace || !noteId) return;
        const draftsState = store.getState().noteDrafts;
        if (draftsState.pending[noteId]) {
            dispatch(removePendingDraft({draftId: noteId}));
            void deleteLocalDoc(noteId);
            dispatch(closeDialogModal());
            navigate(`/${workspace.username}/notebook`);
            return;
        }
        // Real note. If it was acked while the modal was open, note is still undefined here and
        // the created id is only recoverable through the alias map (submittedNoteIdByDraftId is
        // cleared right after ack, docKeyAliases is permanent).
        const realNoteId = note?.noteId
            ?? Object.keys(draftsState.docKeyAliases).find(id => draftsState.docKeyAliases[id] === noteId);
        if (!realNoteId) {
            dispatch(closeDialogModal());
            return;
        }
        deleteNote({workspaceId: workspace.workspaceId, noteId: realNoteId});
        void deleteLocalDoc(docKey ?? noteId);
        dispatch(closeDialogModal());
        const notebookIdToNavigate = note?.notebookId ? note.notebookId : '';
        navigate(`/${workspace.username}/notebook/${notebookIdToNavigate}`);
    }

    const popAreYouSureToDeleteModal = () => {
        dispatch(popDialogModal({
            visible: true,
            title: t(isPendingCreate ? "discardDraftNoteAreYouSureTitle" : "deleteNoteAreYouSureTitle"),
            content: t(isPendingCreate ? "discardDraftNoteAreYouSureText" : "deleteNoteAreYouSureText"),
            confirmButtonLabel: t(isPendingCreate ? "discardDraftNoteAreYouSureConfirmLabel" : "deleteNoteAreYouSureConfirmLabel"),
            onConfirm: deleteCurrentNote
        }));
    }

    return (
        <div className={styles.container}>
            <NotePath/>

            <div className={'flex-1'}/>

            <div className={styles.actionButtonContainer}>
                {ui && (
                    <span
                        className={cn(styles.syncStatus, isError && styles.syncStatusError)}
                        role="status"
                        aria-live="polite"
                    >
                        <ui.Icon className={cn(styles.syncStatusIcon, ui.spin && styles.spin)}/>
                        {t(ui.key)}
                    </span>
                )}

                <Button
                    variant={ButtonVariants.outline}
                    heightVariant={ButtonHeight.mid}
                    className={styles.deleteButton}
                    onClick={popAreYouSureToDeleteModal}
                >
                    <LuTrash2 className={'icon'}/>
                </Button>
            </div>
        </div>
    );
}

export default NoteActionBar;
