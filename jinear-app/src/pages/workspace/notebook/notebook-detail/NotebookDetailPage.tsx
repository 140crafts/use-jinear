import React from 'react';
import styles from './NotebookDetailPage.module.css';
import {useParams} from "react-router-dom";
import NotebookDetail from "@/components/notebookDetail/NotebookDetail.tsx";

interface NotebookDetailPageProps {

}

const NotebookDetailPage: React.FC<NotebookDetailPageProps> = ({}) => {
    const {workspaceName, notebookId} = useParams();
    return (
        <div className={styles.container}>
            {workspaceName && notebookId && <NotebookDetail workspaceName={workspaceName} notebookId={notebookId}/>}
        </div>
    );
}

export default NotebookDetailPage;