import React from 'react';
import styles from './Notebook.module.css';
import type {NotebookDto, WorkspaceDto} from "@/be/jinear-core.ts";
import Button, {ButtonVariants} from "@/components/button";
import cn from "classnames";
import {useLocation} from "react-router-dom";
import {useToggle} from "@/hooks/useToggle.ts";
import {LuFolder, LuFolderOpen} from "react-icons/lu";
import NoteHierarchyList
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import Logger from "@/util/logger.ts";
import {useMoveNoteMutation} from "@/api/noteOperationApi.ts";

interface NotebookProps {
    workspace: WorkspaceDto,
    notebook: NotebookDto,
    initiallyOpen?: boolean
}

const logger = Logger("Notebook")

const Notebook: React.FC<NotebookProps> = ({workspace, notebook, initiallyOpen = false}) => {
    const {pathname} = useLocation()
    const notebookIdForPath = notebook?.notebookId ?? DRAFTS_NOTEBOOK_ID;
    const notebookPath = `/${workspace?.username}/notebook/${notebookIdForPath}`;
    const atPath = pathname == notebookPath;
    const [open, toggle] = useToggle(initiallyOpen);

    const [moveNote] = useMoveNoteMutation();

    const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        const draggedNoteId = e.dataTransfer.getData("text/plain");
        logger.log({draggedNoteId})
        workspace && draggedNoteId && moveNote({
            workspaceId: workspace.workspaceId,
            noteId: draggedNoteId,
            parentNoteId: null
        })
    }

    return (
        <div
            className={styles.container}
            onDragOver={e => e.preventDefault()}
            onDrop={handleDrop}
        >
            <Button
                className={styles.notebookButton}
                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                onClick={toggle}
            >
                {open ? <LuFolderOpen className={'icon'}/> : <LuFolder className={'icon'}/>}

                <span className={cn(styles.notebookName, 'bold', "single-line")}>
                    {notebook.title}
                </span>
            </Button>
            {open &&
                <div className={styles.notebookNotesGreaterContainer}>
                    <div className={'spacer-w-1'}/>
                    <div className={styles.notebookNotesContainer}>
                        <NoteHierarchyList
                            workspace={workspace}
                            notebookId={notebook?.notebookId}
                        />
                    </div>
                </div>
            }
        </div>
    );
}

export default Notebook;