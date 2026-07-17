import React, {useState} from 'react';
import styles from './NotebookList.module.css';
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle.tsx";
import useTranslation from "@/locals/useTranslation.ts";
import {useListWorkspaceNotebooksQuery} from "@/api/notebookListingApi.ts";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import Notebook from "@/components/notebookSectionSideMenu/notebookList/notebook/Notebook.tsx";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useAppDispatch} from "@/store";
import {popNotebookInitializeModal} from "@/store/slice/modalSlice";
import InfiniteLineLoading from "@/components/infiniteLineLoading/InfiniteLineLoading.tsx";
import Drafts from "@/components/notebookSectionSideMenu/notebookList/drafts/Drafts.tsx";

interface NotebookListProps {
    workspace: WorkspaceDto;
}

const NotebookList: React.FC<NotebookListProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);

    const {
        currentData: listWorkspaceNotebooksResponse,
        isLoading
    } = useListWorkspaceNotebooksQuery({workspaceId: workspace.workspaceId, page});

    const openNewNotebookModal = () => {
        dispatch(popNotebookInitializeModal({visible: true, workspace}));
    }

    return (
        <div className={styles.container}>
            <MenuGroupTitle
                label={t("sideMenuYourNotebooksTitle")}
                hasAddButton={true}
                onAddButtonClick={openNewNotebookModal}
            />
            {isLoading && <InfiniteLineLoading/>}
            <div className="spacer-h-1"/>
            <div className={styles.notebookListContainer}>
                <Drafts workspace={workspace}/>
                {listWorkspaceNotebooksResponse?.data?.content?.map((notebook, index) =>
                    <Notebook
                        key={notebook.notebookId}
                        notebook={notebook}
                        workspace={workspace}
                        initiallyOpen={index == 0}
                    />)}
                {!listWorkspaceNotebooksResponse?.data?.hasContent && !isLoading &&
                    <div className={styles.noNotebookContainer}>
                        <span>{t('sideMenuNoNotebook')}</span>
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.contrast}
                            onClick={openNewNotebookModal}>
                            {t('sideMenuNewNotebook')}
                        </Button>
                    </div>
                }
            </div>
        </div>
    );
}

export default NotebookList;