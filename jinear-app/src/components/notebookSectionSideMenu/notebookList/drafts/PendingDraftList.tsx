import React, {useMemo} from 'react';
import styles from './PendingDraftList.module.css';
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuFileClock, LuFileWarning} from "react-icons/lu";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {useTypedSelector} from "@/store";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {pendingDraftsForNotebook, selectPendingDraftsMap} from "@/slice/noteDraftsSlice.ts";
import {useLocation} from "react-router-dom";
import cn from "classnames";

interface PendingDraftListProps {
    workspace: WorkspaceDto;
    /** Notebook whose drafts to list. Omitted ⇒ the workspace-level ones in the drafts section. */
    notebookId?: string;
}

/**
 * Local drafts whose create hasn't been acked yet (offline / lie-fi creations). Listing them here
 * is the recovery path: without it a draft whose tab closed before ack has no reachable URL.
 */
const PendingDraftList: React.FC<PendingDraftListProps> = ({workspace, notebookId}) => {
    const {pathname} = useLocation()
    const {t} = useTranslation();
    // The map reference only changes when a draft does, so notebooks don't all re-render per dispatch.
    const pending = useTypedSelector(selectPendingDraftsMap);
    const entries = useMemo(
        () => pendingDraftsForNotebook(pending, workspace.workspaceId, notebookId),
        [pending, workspace.workspaceId, notebookId]
    );

    return (
        <div className={styles.container}>
            {entries.map(entry => {
                    const path = `/${workspace.username}/notebook/${notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${entry.draftId}`;
                    const atPath = pathname?.indexOf(path) != -1;
                    const failed = !!entry.failedAt;
                    return (
                        <div key={`sidebar-pending-draft-${entry.draftId}`} className={styles.noteButtonGroup}>
                            <Button>
                                {failed
                                    ? <LuFileWarning className={`icon ${styles.failedIcon}`}
                                                     data-tooltip={t('draftCouldNotBeSaved')}/>
                                    : <LuFileClock className={'icon'}/>}
                            </Button>
                            <Button
                                heightVariant={ButtonHeight.short}
                                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                                href={path}
                                className={cn(styles.noteButton, failed && styles.failed, 'line-clamp')}
                            >
                                {shortenStringIfMoreThanMaxLength({text: entry.title || t('untitledNote'), maxLength: 29})}
                            </Button>
                        </div>);
                }
            )}
        </div>
    );
}

export default PendingDraftList;