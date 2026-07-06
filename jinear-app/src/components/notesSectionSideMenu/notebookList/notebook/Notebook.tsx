import React from 'react';
import styles from './Notebook.module.css';
import type {NotebookDto, WorkspaceDto} from "@/be/jinear-core.ts";
import Button, {ButtonVariants} from "@/components/button";
import cn from "classnames";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {useLocation} from "react-router-dom";
import {useToggle} from "@/hooks/useToggle.ts";
import {LuChevronDown, LuChevronUp} from "react-icons/lu";
import NoteHierarchyList
    from "@/components/notesSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";

interface NotebookProps {
    workspace: WorkspaceDto,
    notebook: NotebookDto,
    initiallyOpen?: boolean
}

const Notebook: React.FC<NotebookProps> = ({workspace, notebook, initiallyOpen = false}) => {
    const {pathname} = useLocation()
    const notebookPath = `/${workspace?.username}/notes/${notebook?.notebookId}`;
    const atPath = pathname?.indexOf(notebookPath) != -1;
    const [open, toggle] = useToggle(initiallyOpen);

    return (
        <div className={styles.container}>
            <Button
                className={styles.notebookButton}
                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                onClick={toggle}
            >
                {open ? <LuChevronDown className={'icon'}/> : <LuChevronUp className={'icon'}/>}

                <span className={cn(styles.notebookName, 'bold', "single-line")}>
                    {shortenStringIfMoreThanMaxLength({text: notebook.title, maxLength: 29,})}
                </span>
            </Button>
            <div className={styles.notebookNotesContainer}>
                <NoteHierarchyList notebookId={notebook.notebookId}/>
            </div>
        </div>
    );
}

export default Notebook;