import React from 'react';
import styles from './page.module.css';
import {Navigate, useParams} from "react-router-dom";
import {LuFilePen, LuFileText} from "react-icons/lu";
import cn from "classnames";
import {useTypedSelector} from "@/store";
import {selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice.ts";
import {useFirstAvailableNote} from "@/hooks/useFirstAvailableNote.ts";
import {useCreateNoteDraft} from "@/hooks/useCreateNoteDraft.ts";
import useTranslation from "@/locals/useTranslation.ts";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";

interface NotebookFirstNoteNavigatorProps {

}

/**
 * Index route of the notebook section: sends the user to the note they most likely want (see
 * useFirstAvailableNote for the priority order), or offers to start one when the workspace has none.
 */
const NotebookFirstNoteNavigator: React.FC<NotebookFirstNoteNavigatorProps> = ({}) => {
    const {t} = useTranslation();
    const {workspaceName} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const {path, isLoading, hasNote} = useFirstAvailableNote(workspace);
    const onNewNote = useCreateNoteDraft(workspace);

    // replace: the index route must not stay in history, otherwise Back lands here and redirects again.
    if (hasNote && path) {
        return <Navigate to={path} replace/>;
    }

    return (
        <div className={styles.container}>
            {!workspace || isLoading
                ? <CircularLoading size={20}/>
                : <>
                    <LuFileText className={cn('icon', styles.icon)} size={32}/>
                    <b>{t('notebookEmptyStateTitle')}</b>
                    <span className={styles.text}>{t('notebookEmptyStateText')}</span>
                    <div className="spacer-h-1"/>
                    <Button
                        heightVariant={ButtonHeight.short}
                        variant={ButtonVariants.brandColor}
                        onClick={onNewNote}
                    >
                        <LuFilePen className={'icon'}/>
                        <b>{t('sideMenuNewNote')}</b>
                    </Button>
                </>
            }
        </div>
    );
}

export default NotebookFirstNoteNavigator;
