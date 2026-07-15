import React from 'react';
import styles from './Notebook.module.css';
import type {NotebookDto, WorkspaceDto} from "@/be/jinear-core.ts";
import Button, {ButtonVariants} from "@/components/button";
import cn from "classnames";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {useLocation} from "react-router-dom";
import {useToggle} from "@/hooks/useToggle.ts";
import {LuFolder, LuFolderOpen} from "react-icons/lu";
import NoteHierarchyList
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";

interface NotebookProps {
    workspace: WorkspaceDto,
    notebook: NotebookDto,
    initiallyOpen?: boolean
}

const Notebook: React.FC<NotebookProps> = ({workspace, notebook, initiallyOpen = false}) => {
    const {pathname} = useLocation()
    const notebookIdForPath = notebook?.notebookId ?? DRAFTS_NOTEBOOK_ID;
    const notebookPath = `/${workspace?.username}/notebook/${notebookIdForPath}`;
    const atPath = pathname?.indexOf(notebookPath) != -1;
    const [open, toggle] = useToggle(initiallyOpen);

    return (
        <div className={styles.container}>
            <Button
                className={styles.notebookButton}
                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                onClick={toggle}
            >
                {open ? <LuFolderOpen className={'icon'}/> : <LuFolder className={'icon'}/>}

                <span className={cn(styles.notebookName, 'bold', "single-line")}>
                    {shortenStringIfMoreThanMaxLength({text: notebook.title, maxLength: 29,})}
                </span>
            </Button>
            {open && <div className={styles.notebookNotesContainer}>
                <NoteHierarchyList
                    workspace={workspace}
                    notebookId={notebook?.notebookId}
                />
            </div>}
        </div>
    );
}

export default Notebook;