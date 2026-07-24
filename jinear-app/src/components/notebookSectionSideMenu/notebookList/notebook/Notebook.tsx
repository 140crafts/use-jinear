import React from 'react';
import styles from './Notebook.module.css';
import type {NotebookDto, NotebookVisibilityType, NoteDto, WorkspaceDto} from "@/be/jinear-core.ts";
import Button, {ButtonVariants} from "@/components/button";
import cn from "classnames";
import {useLocation, useNavigate} from "react-router-dom";
import {useToggle} from "@/hooks/useToggle.ts";
import {LuEllipsisVertical, LuFolder, LuFolderOpen, LuLock, LuUserCheck, LuUsers} from "react-icons/lu";
import NoteHierarchyList
    from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/NoteHierarchyList.tsx";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import Logger from "@/util/logger.ts";
import {useChangeNotebookMutation, useMoveNoteMutation} from "@/api/noteOperationApi.ts";
import {closeDialogModal, popDialogModal} from "@/slice/modalSlice.ts";
import {useAppDispatch} from "@/store";
import useTranslation, {type StringKeys} from "@/locals/useTranslation.ts";
import {
    NOTE_DRAG_TYPE
} from "@/components/notebookSectionSideMenu/notebookList/notebook/noteHierarchy/noteHierarchyItem/NoteHierarchyItem.tsx";
import type {IconType} from "react-icons/lib";

interface NotebookProps {
    workspace: WorkspaceDto,
    notebook: NotebookDto,
    initiallyOpen?: boolean
}

const logger = Logger("Notebook")


export const ACCESS_TYPE_ICON_MAP: Record<NotebookVisibilityType, IconType> = {
    PRIVATE: LuLock,
    SHARED: LuUsers,
    PUBLIC_WITHIN_WORKSPACE: LuUserCheck
};

export const ACCESS_TYPE_TOOLTIP_MAP: Record<NotebookVisibilityType, StringKeys> = {
    PRIVATE: 'notebookVisibilityDetail_PRIVATE',
    SHARED: 'notebookVisibilityDetail_SHARED',
    PUBLIC_WITHIN_WORKSPACE: 'notebookVisibilityDetail_PUBLIC_WITHIN_WORKSPACE'
};

const Notebook: React.FC<NotebookProps> = ({workspace, notebook, initiallyOpen = false}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {pathname} = useLocation()
    const dispatch = useAppDispatch();
    const notebookIdForPath = notebook?.notebookId ?? DRAFTS_NOTEBOOK_ID;
    const notebookPath = `/${workspace?.username}/notebook/${notebookIdForPath}`;
    const atPath = pathname == notebookPath;
    const [open, toggle] = useToggle(initiallyOpen);
    const Icon = ACCESS_TYPE_ICON_MAP[notebook.visibility];
    const tooltipKey = ACCESS_TYPE_TOOLTIP_MAP[notebook.visibility];

    const [moveNote] = useMoveNoteMutation();
    const [changeNotebook] = useChangeNotebookMutation();

    const popAreYouSureToMoveToNotebookModal = (note: NoteDto) => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("moveNoteToNotebookAreYouSureTitle"),
                content: t("moveNoteToNotebookAreYouSureText").replace("{notebookTitle}", notebook.title),
                confirmButtonLabel: t("moveNoteToNotebookAreYouSureConfirmLabel"),
                onConfirm: () => {
                    workspace && note && changeNotebook({
                        workspaceId: workspace?.workspaceId,
                        noteId: note?.noteId,
                        notebookId: notebook.notebookId
                    }).then(() => {
                        notebook && note && navigate(`/${workspace?.username}/notebook/${notebook.notebookId}/note/${note.noteId}`)
                    })
                    dispatch(closeDialogModal());
                }
            })
        );
    }

    const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        const raw = e.dataTransfer.getData(NOTE_DRAG_TYPE);
        if (!raw) return;

        let dragged: NoteDto;
        try {
            dragged = JSON.parse(raw);
        } catch {
            return;
        }
        const draggedNoteId = dragged?.noteId;
        logger.log({draggedNoteId})
        if (dragged?.notebookId != notebook.notebookId) {
            popAreYouSureToMoveToNotebookModal(dragged);
            return;
        }
        workspace && draggedNoteId && moveNote({
            workspaceId: workspace.workspaceId,
            noteId: draggedNoteId,
            parentNoteId: null
        })
    }

    return (
        <div className={styles.container}>

            <div className={cn(styles.notebookButtonContainer, atPath && styles.atPath)}>
                <Button
                    className={styles.notebookButton}
                    variant={ButtonVariants.hoverFilled2}
                    onClick={toggle}
                    onDragOver={e => e.preventDefault()}
                    onDrop={handleDrop}
                >
                    {open ? <LuFolderOpen className={'icon'}/> : <LuFolder className={'icon'}/>}
                    <span className={cn(styles.notebookName, 'bold', "line-clamp")}>
                    {notebook.title}
                    </span>
                </Button>
                <Button
                    disabled={true}
                    className={styles.notebookDetailsButton}
                    data-tooltip-multiline={t(tooltipKey)}>
                    <Icon className={'icon'}/>
                </Button>
                <Button
                    variant={ButtonVariants.hoverFilled2}
                    className={styles.notebookDetailsButton}
                    href={`/${workspace.username}/notebook/${notebook.notebookId}`}
                >
                    <LuEllipsisVertical className={cn('icon')}/>
                </Button>
            </div>

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