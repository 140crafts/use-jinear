import React from 'react';
import styles from './NotebookDetailPage.module.css';
import {Navigate, useParams} from "react-router-dom";
import NotebookDetail from "@/components/notebookDetail/NotebookDetail.tsx";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";

interface NotebookDetailPageProps {

}

const NotebookDetailPage: React.FC<NotebookDetailPageProps> = ({}) => {
    const {workspaceName, notebookId} = useParams();
    const isDraftsNotebook = notebookId === DRAFTS_NOTEBOOK_ID;

    if (isDraftsNotebook) {
        return <Navigate to={`/${workspaceName}/notebook`} replace/>;
    }

    return (
        <div className={styles.container}>
            {workspaceName && notebookId && <NotebookDetail workspaceName={workspaceName} notebookId={notebookId}/>}
        </div>
    );
}

export default NotebookDetailPage;