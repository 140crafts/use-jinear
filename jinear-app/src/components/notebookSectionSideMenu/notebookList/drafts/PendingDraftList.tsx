import React, {useMemo} from 'react';
import styles from "../notebook/noteHierarchy/NotebookNoteList.module.css";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuFileClock} from "react-icons/lu";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {useTypedSelector} from "@/store";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {selectPendingDraftsOrdered} from "@/slice/noteDraftsSlice.ts";
import {useToggle} from "@/hooks/useToggle.ts";
import {useLocation} from "react-router-dom";

interface PendingDraftListProps {
    workspace: WorkspaceDto;
}

/**
 * Local drafts whose create hasn't been acked yet (offline / lie-fi creations). Listing them here
 * is the recovery path: without it a draft whose tab closed before ack has no reachable URL.
 */
const PendingDraftList: React.FC<PendingDraftListProps> = ({workspace}) => {
    const {pathname} = useLocation()
    const {t} = useTranslation();
    const entries = useTypedSelector(selectPendingDraftsOrdered(workspace.workspaceId));

    return (
        <div className={styles.container}>
            {entries.map(entry => {
                    const path = `/${workspace.username}/notebook/${DRAFTS_NOTEBOOK_ID}/note/${entry.draftId}`;
                    const atPath = pathname?.indexOf(path) != -1;
                    return (
                        <div key={`sidebar-pending-draft-${entry.draftId}`} className={styles.noteButtonGroup}>
                            <Button
                                heightVariant={ButtonHeight.short}
                                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                                href={path}
                                className={styles.noteButton}
                            >
                                <LuFileClock className={'icon'}/>
                                {shortenStringIfMoreThanMaxLength({text: entry.title || t('untitledNote'), maxLength: 29})}
                            </Button>
                        </div>);
                }
            )}
        </div>
    );
}

export default PendingDraftList;
