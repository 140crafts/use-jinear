import React from 'react';
import styles from './index.module.css';
import {useParams} from "react-router-dom";
import {useTypedSelector} from "@/store";
import {selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice.ts";
import NoteEditor from "@/components/note-editor/NoteEditor.tsx";

interface NotePageProps {

}

const NotePage: React.FC<NotePageProps> = ({}) => {
    const {workspaceName, noteId, notebookId} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    return (
        <div className={styles.container}>
            {workspace && noteId && notebookId &&
                <NoteEditor key={noteId} workspace={workspace} notebookId={notebookId} noteId={noteId}/>}
        </div>
    );
}

export default NotePage;