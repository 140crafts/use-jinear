import React, {useMemo, useState} from 'react';
import {format} from "date-fns";
import styles from './NoteHistoryContent.module.css';
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";
import {useOnlineStatus} from "@/hooks/useOnlineStatus.ts";
import {useAppDispatch, useTypedSelector} from "@/store";
import {
    closeDialogModal,
    popDialogModal,
    selectNoteHistoryModalNoteTitle,
    selectNoteHistoryModalOnRestore,
    selectNoteHistoryModalWorkspace
} from "@/slice/modalSlice.ts";
import {useRetrieveRichTextVersionQuery, useRetrieveRichTextVersionsInfiniteQuery} from "@/api/richTextVersionApi.ts";
import NoteHistoryVersionStrip, {versionDateOf} from "../noteHistoryVersionStrip/NoteHistoryVersionStrip.tsx";
import NoteHistoryPreview from "../noteHistoryPreview/NoteHistoryPreview.tsx";

interface NoteHistoryContentProps {
    richTextId: string;
    close: () => void;
}

const NoteHistoryContent: React.FC<NoteHistoryContentProps> = ({richTextId, close}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const online = useOnlineStatus();
    const workspace = useTypedSelector(selectNoteHistoryModalWorkspace);
    const noteTitle = useTypedSelector(selectNoteHistoryModalNoteTitle);
    const onRestore = useTypedSelector(selectNoteHistoryModalOnRestore);
    const [selectedVersionId, setSelectedVersionId] = useState<string>();

    const {
        currentData: versionPages,
        isLoading: isVersionsLoading,
        hasNextPage,
        isFetchingNextPage,
        fetchNextPage
    } = useRetrieveRichTextVersionsInfiniteQuery({richTextId});

    const versions = useMemo(() => versionPages?.pages.flatMap(page => page.content) ?? [], [versionPages]);
    const latestVersionId = versions[0]?.richTextVersionId;
    const activeVersionId = selectedVersionId ?? latestVersionId;

    const {currentData: activeVersion} = useRetrieveRichTextVersionQuery(
        {richTextId, richTextVersionId: activeVersionId ?? ""},
        {skip: !activeVersionId}
    );

    const isLatestActive = !!activeVersionId && activeVersionId === latestVersionId;
    const canRestore = online && !!onRestore && !!activeVersion && !isLatestActive;

    const restore = (html: string) => {
        onRestore?.(html);
        dispatch(closeDialogModal());
        close();
    };

    const confirmRestore = () => {
        if (!activeVersion) {
            return;
        }
        const html = activeVersion.html;
        dispatch(popDialogModal({
            visible: true,
            title: t("noteHistoryRestoreAreYouSureTitle"),
            content: t("noteHistoryRestoreAreYouSureText"),
            confirmButtonLabel: t("noteHistoryRestoreAreYouSureConfirmLabel"),
            onConfirm: () => restore(html)
        }));
    };

    const editedByLabel = activeVersion
        ? t("noteHistoryEditedBy")
            .replace("${username}", activeVersion.account?.username ?? "")
            .replace("${date}", format(versionDateOf(activeVersion), t("dateTimeFormat")))
        : "";

    return (
        <div className={styles.content}>
            <div className={styles.preview}>
                {activeVersionId && (
                    <NoteHistoryPreview
                        version={activeVersion}
                        noteTitle={noteTitle}
                        workspaceUsername={workspace?.username}
                    />
                )}
            </div>
            <div className={styles.strip}>
                <NoteHistoryVersionStrip
                    versions={versions}
                    activeVersionId={activeVersionId}
                    latestVersionId={latestVersionId}
                    hasNextPage={hasNextPage}
                    isFetchingNextPage={isFetchingNextPage}
                    isLoading={isVersionsLoading}
                    onSelect={setSelectedVersionId}
                    onLoadMore={() => void fetchNextPage()}
                />
            </div>
            {activeVersion && (
                <div className={styles.footer}>
                    <span className={styles.editedBy}>
                        {isLatestActive ? t("noteHistoryIsCurrentVersion") : editedByLabel}
                    </span>
                    {online && !!onRestore && (
                        <Button
                            variant={ButtonVariants.contrast}
                            heightVariant={ButtonHeight.mid}
                            disabled={!canRestore}
                            onClick={confirmRestore}
                        >
                            {t("noteHistoryRestore")}
                        </Button>
                    )}
                </div>
            )}
        </div>
    );
}

export default NoteHistoryContent;
