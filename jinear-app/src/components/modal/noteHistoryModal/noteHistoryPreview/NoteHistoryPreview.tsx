import React, {useMemo} from 'react';
import styles from './NoteHistoryPreview.module.css';
import Tiptap from "@/components/tiptap/Tiptap.tsx";
import {TaskLinkChips} from "@/components/tiptap/taskLinkChips/TaskLinkChips.ts";
import taskLinkChipStyles from "@/components/tiptap/taskLinkChips/TaskLinkChips.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import type {RichTextVersionDetailDto} from "@/model/be/jinear-core";

interface NoteHistoryPreviewProps {
    version?: RichTextVersionDetailDto;
    noteTitle?: string;
    workspaceUsername?: string;
}

const NoteHistoryPreview: React.FC<NoteHistoryPreviewProps> = ({version, noteTitle, workspaceUsername}) => {
    const extensions = useMemo(() => [
        TaskLinkChips.configure({
            workspaceUsername,
            chipClassName: taskLinkChipStyles.taskLinkChip
        })
    ], [workspaceUsername]);

    return (
        <div className={styles.container}>
            {noteTitle && <h1 className={styles.title}>{noteTitle}</h1>}
            {version
                ? <Tiptap
                    key={version.richTextVersionId}
                    content={version.html}
                    editable={false}
                    extensions={extensions}
                    className={styles.editor}
                    editorClassName={styles.editorContent}
                />
                : <div className={styles.loading}><CircularLoading size={21}/></div>}
        </div>
    );
}

export default NoteHistoryPreview;
