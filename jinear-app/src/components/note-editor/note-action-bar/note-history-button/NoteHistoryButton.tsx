import React from 'react';
import {LuHistory} from "react-icons/lu";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation.ts";
import {useAppDispatch} from "@/store";
import {popNoteHistoryModal} from "@/slice/modalSlice.ts";
import {useNoteEditorContext} from "@/components/note-editor/note-editor-context.ts";

const NoteHistoryButton: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {workspace, note, isPendingCreate, restoreBody} = useNoteEditorContext();
    const richTextId = note?.richText?.richTextId;

    if (isPendingCreate || !richTextId) {
        return null;
    }

    const openHistory = () => {
        dispatch(popNoteHistoryModal({
            visible: true,
            workspace,
            richTextId,
            noteTitle: note?.title,
            onRestore: restoreBody
        }));
    };

    return (
        <Button
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.mid}
            onClick={openHistory}
            data-tooltip-left={t("noteHistoryButtonTooltip")}
        >
            <LuHistory className={'icon'}/>
        </Button>
    );
}

export default NoteHistoryButton;
