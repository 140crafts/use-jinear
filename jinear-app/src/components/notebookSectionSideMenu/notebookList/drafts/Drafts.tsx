import React from 'react';
import styles from './Drafts.module.css';
import useTranslation from "@/locals/useTranslation.ts";
import {useLocation} from "react-router-dom";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import {useToggle} from "@/hooks/useToggle.ts";
import Button, {ButtonVariants} from "@/components/button";
import {LuFolder, LuFolderOpen} from "react-icons/lu";
import cn from "classnames";
import NoteHierarchyList
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";
import PendingDraftList from "@/components/notebookSectionSideMenu/notebookList/drafts/PendingDraftList.tsx";

interface DraftsProps {
    workspace: WorkspaceDto
}

const Drafts: React.FC<DraftsProps> = ({workspace}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation()
    const notebookPath = `/${workspace?.username}/notebook/${DRAFTS_NOTEBOOK_ID}`;
    const atPath = pathname?.indexOf(notebookPath) != -1;
    const [open, toggle] = useToggle(true);

    return (
        <div className={styles.container}>
            <Button
                className={styles.notebookButton}
                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                onClick={toggle}
            >
                {open ? <LuFolderOpen className={'icon'}/> : <LuFolder className={'icon'}/>}

                <span className={cn(styles.notebookName, 'bold', "single-line")}>
                    {t('notebookDraftsTitle')}
                </span>
            </Button>
            {open &&
                <div className={styles.notebookNotesContainer}>
                <PendingDraftList workspace={workspace}/>
                <NoteHierarchyList
                    workspace={workspace}
                    forDrafts={true}
                />
            </div>}
        </div>
    );
}

export default Drafts;