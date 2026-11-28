import type {TaskLinkingNoteDto} from "@/model/be/jinear-core";
import {useRetrieveNotesLinkingTaskQuery} from "@/store/api/noteFilterApi";
import {closeTaskOverviewModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import {DRAFTS_NOTEBOOK_ID} from "@/components/tiptap/crdt/constants.ts";
import React from "react";
import {LuFileText} from "react-icons/lu";
import {Link} from "react-router-dom";
import {useTask} from "../context/TaskDetailContext";
import styles from "./TaskLinkingNoteList.module.css";
import TaskSectionHeader from "../taskSectionHeader/TaskSectionHeader";

const TaskLinkingNoteList: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const task = useTask();
    const {currentData: response} = useRetrieveNotesLinkingTaskQuery(task.taskId, {refetchOnMountOrArgChange: true});
    const notes = response?.data ?? [];
    const workspaceUsername = task.workspace?.username;

    const notePath = (note: TaskLinkingNoteDto) =>
        `/${workspaceUsername}/notebook/${note.notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${note.noteId}`;

    const closeOverviewModal = () => {
        dispatch(closeTaskOverviewModal());
    };

    if (notes.length == 0 || !workspaceUsername) {
        return null;
    }

    return (
        <div className={styles.container}>
            <TaskSectionHeader title={t("taskLinkingNoteListTitle")}/>
            <div className={styles.list}>
                {notes.map((note) => (
                    <Link key={note.noteId} to={notePath(note)} className={styles.row} onClick={closeOverviewModal}>
                        <LuFileText className={styles.icon}/>
                        <span className={styles.title}>{note.title || t("untitledNote")}</span>
                        <span className={styles.notebook}>{note.notebookTitle ?? t("notebookDraftsTitle")}</span>
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default TaskLinkingNoteList;
