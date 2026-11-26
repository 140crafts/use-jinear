import React, {useMemo, useState} from 'react';
import styles from './NoteRelatedTasks.module.css';
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import {useFilterTasksQuery} from "@/api/taskListingApi.ts";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect.ts";
import TaskRow from "@/components/taskRow/TaskRow.tsx";
import useTranslation from "@/locals/useTranslation.ts";
import type {TaskDto} from "@/be/jinear-core.ts";

const TASK_TAGS_DEBOUNCE_MS = 300;
const MAX_TASK_TAGS = 250;

const taskTagOf = (task: TaskDto) => `${task.team?.tag}-${task.teamTagNo}`.toLowerCase();

const NoteRelatedTasks: React.FC = () => {
    const {t} = useTranslation();
    const {workspace, taskTags = []} = useNoteEditorContext();
    const [requestedTaskTags, setRequestedTaskTags] = useState<string[]>([]);

    useDebouncedEffect(() => {
        setRequestedTaskTags(taskTags.slice(0, MAX_TASK_TAGS).sort());
    }, [taskTags], TASK_TAGS_DEBOUNCE_MS);

    const {currentData, data} = useFilterTasksQuery({
        workspaceId: workspace?.workspaceId ?? "",
        taskTags: requestedTaskTags,
        page: 0,
        size: MAX_TASK_TAGS
    }, {skip: !workspace || requestedTaskTags.length == 0});

    const tasks = useMemo(() => {
        const response = currentData ?? data;
        return (response?.data?.content ?? [])
            .filter((task) => taskTags.includes(taskTagOf(task)))
            .sort((first, second) => taskTags.indexOf(taskTagOf(first)) - taskTags.indexOf(taskTagOf(second)));
    }, [currentData, data, taskTags]);

    return tasks.length != 0 ? (
        <div className={styles.container}>
            <div className={styles.title}>{t("noteRelatedTasksTitle")}</div>
            <div className={styles.list}>
                {tasks.map((task, index) => (
                    <TaskRow key={task.taskId} task={task} withBottomBorderLine={index != tasks.length - 1} readOnly/>
                ))}
            </div>
        </div>
    ) : null;
}

export default NoteRelatedTasks;
