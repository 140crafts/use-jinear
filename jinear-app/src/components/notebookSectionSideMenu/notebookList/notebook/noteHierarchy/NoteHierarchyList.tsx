import React, {useEffect, useState} from 'react';
import styles from './NotebookNoteList.module.css';
import {useFilterNotesQuery} from "@/api/noteFilterApi.ts";
import Button, {ButtonHeight} from "@/components/button";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import useTranslation from "@/locals/useTranslation.ts";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading.tsx";
import {useLocation} from "react-router-dom";
import NoteHierarchyItem
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/noteHierarchyItem/NoteHierarchyItem.tsx";

interface NoteHierarchyListProps {
    workspace: WorkspaceDto,
    notebookId?: string;
    parentNote?: NoteDto;
    forDrafts?: boolean
}

const NoteHierarchyList: React.FC<NoteHierarchyListProps> = ({
                                                                 workspace,
                                                                 notebookId,
                                                                 parentNote,
                                                                 forDrafts = false
                                                             }) => {
    const {pathname} = useLocation();
    const {t} = useTranslation();
    const [page, setPage] = useState<number>(0);
    const [notes, setNotes] = useState<NoteDto[]>([]);
    const [hasMore, setHasMore] = useState<boolean>(false);

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

    return (
        <div className={styles.container}>
            {isLoading && <InfiniteLineLoading className={styles.lineLoading}/>}
            {notes?.length == 0 && <span className={styles.noChild}>{t('noteListNoChildNotes')}</span>}
            {notes?.map(note =>
                <NoteHierarchyItem
                    key={`sidebar-note-${note.noteId}`}
                    workspace={workspace}
                    note={note}
                    notebookId={notebookId}
                    forDrafts={forDrafts}
                    pathname={pathname}/>
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