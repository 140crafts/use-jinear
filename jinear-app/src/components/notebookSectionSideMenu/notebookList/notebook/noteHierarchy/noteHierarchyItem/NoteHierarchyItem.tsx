import React from 'react';
import styles from './NoteHierarchyItem.module.css';
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {useToggle} from "@/hooks/useToggle.ts";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuChevronDown, LuChevronRight, LuFileText} from "react-icons/lu";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import NoteHierarchyList
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";
import cn from "classnames";
import Logger from "@/util/logger.ts";
import {useMoveNoteMutation} from "@/api/noteOperationApi.ts";
import toast from "react-hot-toast";

interface NoteHierarchyItemProps {
    workspace: WorkspaceDto,
    note: NoteDto;
    notebookId?: string;
    forDrafts: boolean;
    pathname: string;
}

const logger = Logger('NoteHierarchyItem');

export const NOTE_DRAG_TYPE = "application/x-jinear-note";

const NoteHierarchyItem: React.FC<NoteHierarchyItemProps> = ({
                                                                 workspace,
                                                                 note,
                                                                 notebookId,
                                                                 forDrafts,
                                                                 pathname
                                                             }) => {
    const {t} = useTranslation();
    const [open, toggle, setToggle] = useToggle(false);

    const [moveNote] = useMoveNoteMutation();
    const isNoteDraft = note?.notebookId == null;

    const path = `/${workspace.username}/notebook/${note?.notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${note?.noteId}`;
    const atPath = pathname?.indexOf(path) != -1;

    const _onDragStart = (event: React.DragEvent) => {
        logger.log({_onDragStart: note?.noteId, event});
        event.dataTransfer.setData(NOTE_DRAG_TYPE, JSON.stringify(note));
        event.dataTransfer.setData("text/plain", note?.title ?? "");
        event.dataTransfer.effectAllowed = "move";
        setToggle(false);
    };

    const _onDragOver = (event: React.DragEvent) => {
        if (!event.dataTransfer.types.includes(NOTE_DRAG_TYPE)) return;
        event.preventDefault();
        event.dataTransfer.dropEffect = "move";

    }

    const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        if (isNoteDraft) {
            toast(t("youCanNotMoveNotesUnderDrafts"))
            return;
        }
        try {
            const raw = e.dataTransfer.getData(NOTE_DRAG_TYPE);
            const dragged = JSON.parse(raw);

            const draggedNoteId = dragged?.noteId;
            const currentNoteId = note?.noteId;
            logger.log({draggedNoteId, currentNoteId})

            if (!draggedNoteId || !currentNoteId || !workspace) return;
            if (draggedNoteId === currentNoteId) return;

            moveNote({
                workspaceId: workspace.workspaceId,
                noteId: draggedNoteId,
                parentNoteId: currentNoteId
            })
        } catch {
            return;
        }
    }

    return (
        <div className={styles.noteButtonGroup}>
            <div className={cn(styles.noteRow, atPath && styles.noteAtPath)}
                 draggable={!isNoteDraft}
                 onDragStart={_onDragStart}
                 onDragOver={_onDragOver}
                 onDrop={handleDrop}
            >
                <Button
                    heightVariant={ButtonHeight.mid}
                    variant={ButtonVariants.hoverFilled2}
                    onClick={toggle}
                    className={cn(styles.iconButton, forDrafts && styles.hoverBgNone)}
                >
                    <LuFileText className={cn('icon', styles.fileIcon, forDrafts && styles.shown)}/>
                    {(open ? <LuChevronDown className={cn('icon', styles.dropButton, forDrafts && styles.hidden)}/> :
                        <LuChevronRight className={cn('icon', styles.dropButton, forDrafts && styles.hidden)}/>)}
                </Button>

                <Button
                    heightVariant={ButtonHeight.mid}
                    href={path}
                    className={cn(styles.noteButton)}
                    draggable={false}
                >
                <span className={'line-clamp'}>
                    {shortenStringIfMoreThanMaxLength({
                        text: (note.title == null || note.title == '') ? t('untitledNote') : note.title,
                        maxLength: 29
                    })}
                </span>
                </Button>

            </div>

            {!forDrafts && open &&
                <div className={styles.childContainer}>
                    <NoteHierarchyList
                        workspace={workspace}
                        notebookId={notebookId}
                        parentNote={note}/>
                </div>
            }
        </div>
    );
}

export default NoteHierarchyItem;