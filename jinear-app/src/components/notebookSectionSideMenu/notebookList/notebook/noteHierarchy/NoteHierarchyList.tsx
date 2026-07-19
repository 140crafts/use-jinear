import React, {useEffect, useState} from 'react';
import styles from './NotebookNoteList.module.css';
import {useFilterNotesQuery} from "@/api/noteFilterApi.ts";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useToggle} from "@/hooks/useToggle.ts";
import type {NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import {LuChevronDown, LuChevronRight, LuFileText} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation.ts";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading.tsx";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {useLocation} from "react-router-dom";

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

    const [open, toggle] = useToggle(false);

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
            {notes?.map(note => {
                    const path = `/${workspace.username}/notebook/${note?.notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${note?.noteId}`;
                    const atPath = pathname?.indexOf(path) != -1;
                    return (
                        <div key={`sidebar-note-${note.noteId}`} className={styles.noteButtonGroup}>
                            <Button
                                heightVariant={ButtonHeight.short}
                                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                                href={path}
                                onClick={onNoteButtonClick}
                                className={styles.noteButton}
                            >
                                {forDrafts ? <LuFileText className={'icon'}/> :
                                    (open ? <LuChevronDown className={'icon'}/> : <LuFileText className={'icon'}/>)}
                                {shortenStringIfMoreThanMaxLength({
                                    text: (note.title == null || note.title == '') ? t('untitledNote') : note.title,
                                    maxLength: 29
                                })}
                            </Button>
                            {!forDrafts && open &&
                                <div className={styles.childContainer}>
                                    <NoteHierarchyList
                                        workspace={workspace}
                                        notebookId={notebookId}
                                        parentNote={note}/>
                                </div>
                            }
                        </div>
                    )
                }
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