import React from 'react';
import styles from './NotebookDetail.module.css';
import {useGetNotebookQuery} from "@/api/notebookListingApi.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import NotebookGeneralSettings from "@/components/notebookDetail/notebookGeneralSettings/NotebookGeneralSettings.tsx";
import NotebookMembersSettings from "@/components/notebookDetail/notebookMembersSettings/NotebookMembersSettings.tsx";
import Line from "@/components/line/Line";
import {useWorkspaceRoleIsAdminOrOwner} from "@/hooks/useWorkspaceRoleIsAdminOrOwner";
import {selectCurrentAccountId} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";

interface NotebookDetailProps {
    notebookId: string
    workspaceName: string
}

const NotebookDetail: React.FC<NotebookDetailProps> = ({notebookId}) => {
    const {data: getNotebookResponse, isFetching} = useGetNotebookQuery({notebookId});
    const notebook = getNotebookResponse?.data;

    const isAdminOrOwner = useWorkspaceRoleIsAdminOrOwner({workspaceId: notebook?.workspaceId});
    const currentAccountId = useTypedSelector(selectCurrentAccountId);
    const accountHasEditRole = !!notebook && (isAdminOrOwner || notebook.ownerId === currentAccountId);

    return (
        <div className={styles.container}>
            {isFetching && !notebook && <CircularLoading/>}

            {notebook && (
                <NotebookGeneralSettings notebook={notebook} accountHasEditRole={accountHasEditRole}/>
            )}

            {notebook && notebook.visibility === "SHARED" && (
                <>
                    <Line/>
                    <NotebookMembersSettings notebook={notebook} accountHasEditRole={accountHasEditRole}/>
                </>
            )}
        </div>
    );
}

export default NotebookDetail;
