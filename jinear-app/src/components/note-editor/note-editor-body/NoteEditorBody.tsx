import React, {useMemo} from 'react';
import styles from './NoteEditorBody.module.css';
import CollaborativeRichText from "@/components/tiptap/CollaborativeRichText.tsx";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";
import useTranslation from "@/locals/useTranslation.ts";
import {TaskLinkChips} from "@/components/tiptap/taskLinkChips/TaskLinkChips.ts";
import taskLinkChipStyles from "@/components/tiptap/taskLinkChips/TaskLinkChips.module.css";
import {useTypedSelector} from "@/store";
import {selectNoteTextWidth} from "@/slice/displayPreferenceSlice.ts";
import cn from "classnames";
import NoteTextWidthButton from "@/components/note-editor/note-editor-body/note-text-width-button/NoteTextWidthButton.tsx";

const NoteEditorBody: React.FC = () => {
    const {t} = useTranslation();
    const {workspace, doc, flushNow, registerEditor, setTaskTags} = useNoteEditorContext();
    const noteTextWidth = useTypedSelector(selectNoteTextWidth);

    const extensions = useMemo(() => [
        TaskLinkChips.configure({
            workspaceUsername: workspace?.username,
            chipClassName: taskLinkChipStyles.taskLinkChip,
            onTaskTagsChange: setTaskTags
        })
    ], [workspace?.username, setTaskTags]);

    return (doc ?
        <CollaborativeRichText
            ref={registerEditor}
            editable
            doc={doc}
            className={styles.container}
            editorWrapperClassName={cn(styles.editorWrapperClassName, noteTextWidth === "page" && styles.pageWidth)}
            editorClassName={styles.editorClassName}
            workspaceIdForImages={workspace?.workspaceId}
            workspaceForTaskLinks={workspace}
            extensions={extensions}
            actionBarEndContent={<NoteTextWidthButton/>}
            onSettle={flushNow}
            actionBarClassName={styles.actionBarClassName}
            withOutlineOnFocus={false}
            placeholder={t('emptyNotePlaceholder')}
        /> : null);
}

export default NoteEditorBody;
