import React, {useEffect, useState} from 'react';
import styles from './NotebookNoteList.module.css';
import {useFilterNotesQuery} from "@/api/noteFilterApi.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useToggle} from "@/hooks/useToggle.ts";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import {LuChevronDown, LuChevronRight} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading.tsx";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";

interface NoteHierarchyListProps {
    workspace: WorkspaceDto,
    notebookId: string;
    parentNote?: NoteDto;
}

const NoteHierarchyList: React.FC<NoteHierarchyListProps> = ({workspace, notebookId, parentNote}) => {
    const {t} = useTranslation();
    const [page, setPage] = useState<number>(0);
    const [notes, setNotes] = useState<NoteDto[]>([]);
    const [hasMore, setHasMore] = useState<boolean>(false);

    const [open, toggle] = useToggle(parentNote == null);
    const {data: filterNotesResponse, isLoading} = useFilterNotesQuery({
        workspaceId: workspace.workspaceId,
        notebookId,
        parentNoteId: parentNote?.noteId,
        page
    });

    useEffect(() => {
        if (filterNotesResponse) {
            const pageDto = filterNotesResponse?.data;
            const content = pageDto?.content;
            setNotes(prev => page == 0 ? [...content] : [...prev, ...content]);
            setHasMore(pageDto?.hasNext);
        }
    }, [filterNotesResponse, page]);

    const incPage = () => {
        setPage(curr => curr + 1);
    }

    const onNoteButtonClick = () => {
        toggle();
    }

    return (
        <div className={styles.container}>
            {isLoading && <InfiniteLineLoading/>}
            {notes?.length == 0 && <span className={styles.noChild}>{t('noteListNoChildNotes')}</span>}
            {notes?.map(note =>
                (<div key={`sidebar-note-${note.noteId}`} className={styles.noteButtonGroup}>
                    <Button
                        heightVariant={ButtonHeight.short2x}
                        variant={ButtonVariants.hoverFilled2}
                        href={`/${workspace.username}/notebook/${note?.notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${note?.noteId}`}
                        onClick={onNoteButtonClick}
                        className={styles.noteButton}
                    >
                        {open ? <LuChevronDown className={'icon'}/> : <LuChevronRight className={'icon'}/>}
                        {note.title}
                    </Button>
                    {open &&
                        <div className={styles.childContainer}>
                            <NoteHierarchyList
                                workspace={workspace}
                                notebookId={notebookId}
                                parentNote={note}/>
                        </div>
                    }
                </div>)
            )}
            {hasMore &&
                <Button
                    heightVariant={ButtonHeight.short2x}
                    onClick={incPage}>
                    {t('noteListLoadMore')}
                </Button>
            }
        </div>
    );
}

export default NoteHierarchyList;