import React, {useState} from 'react';
import styles from './NotebookNoteList.module.css';
import {useRetrieveNotePathAwareQuery} from "@/api/noteListingApi.ts";

interface NoteHierarchyListProps {
    notebookId: string;
    noteId?: string;
}

const NoteHierarchyList: React.FC<NoteHierarchyListProps> = ({notebookId, noteId}) => {
    const [page, setPage] = useState<number>(0);
    const {currentData: retrieveNoteResponse, isFetching} = useRetrieveNotePathAwareQuery({notebookId, noteId, page});

    return (
        <div className={styles.container}>
            {retrieveNoteResponse?.data?.children?.content?.map(childNote =>
                <div key={childNote.noteId}>
                    {childNote.noteId}
                </div>
            )}
        </div>
    );
}

export default NoteHierarchyList;