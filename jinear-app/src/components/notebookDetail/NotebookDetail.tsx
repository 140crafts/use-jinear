import React from 'react';
import styles from './NotebookDetail.module.css';
import {useGetNotebookQuery} from "@/api/notebookListingApi.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface NotebookDetailProps {
    notebookId: string
    workspaceName: string
}

const NotebookDetail: React.FC<NotebookDetailProps> = ({notebookId}) => {
    const {data: getNotebookResponse, isFetching} = useGetNotebookQuery({notebookId});

    return (
        <div className={styles.container}>
            {isFetching && <CircularLoading/>}

        </div>
    );
}

export default NotebookDetail;