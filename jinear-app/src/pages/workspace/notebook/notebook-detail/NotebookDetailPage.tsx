import React from 'react';
import styles from './NotebookDetailPage.module.css';
import {useParams} from "react-router-dom";

interface NotebookDetailPageProps {

}

const NotebookDetailPage: React.FC<NotebookDetailPageProps> = ({}) => {
    const {workspaceName, notebookId} = useParams();

    return (
        <div className={styles.container}>
            {workspaceName}
            {notebookId}
        </div>
    );
}

export default NotebookDetailPage;