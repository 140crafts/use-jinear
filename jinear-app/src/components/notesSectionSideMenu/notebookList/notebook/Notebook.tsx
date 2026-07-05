import React from 'react';
import styles from './Notebook.module.css';
import type {NotebookDto, WorkspaceDto} from "@/be/jinear-core.ts";
import Button, {ButtonVariants} from "@/components/button";
import cn from "classnames";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import {useLocation} from "react-router-dom";

interface NotebookProps {
    workspace: WorkspaceDto,
    notebook: NotebookDto
}

const Notebook: React.FC<NotebookProps> = ({workspace, notebook}) => {
    const {pathname} = useLocation()
    const notebookPath = `/${workspace?.username}/notes/${notebook?.notebookId}`;
    const atPath = pathname?.indexOf(notebookPath) != -1;

    return (
        <div className={styles.container}>
            <Button
                className={styles.notebookButton}
                variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
                <span className={cn(styles.notebookName, atPath && 'bold', "single-line")}>
                    {shortenStringIfMoreThanMaxLength({text: notebook.title, maxLength: 29,})}
                </span>
            </Button>
        </div>
    );
}

export default Notebook;