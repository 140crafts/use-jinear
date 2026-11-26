import React from "react";
import type {Editor} from "@tiptap/react";
import {LuSquareCheckBig} from "react-icons/lu";
import Button, {ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import {useAppDispatch} from "@/store";
import {closeSearchTaskModal, popSearchTaskModal} from "@/slice/modalSlice";
import type {TaskDto, WorkspaceDto} from "@/be/jinear-core";
import {buildTaskLink} from "@/util/taskLinkUtils";
import {insertTaskLink} from "@/components/tiptap/taskLinkChips/insertTaskLink";

interface TaskLinkButtonProps {
    editor: Editor;
    workspace: WorkspaceDto;
}

const TaskLinkButton: React.FC<TaskLinkButtonProps> = ({editor, workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();

    const onTaskSelect = (task: TaskDto) => {
        const teamTag = task.team?.tag;
        if (!teamTag) {
            return;
        }
        const href = buildTaskLink(workspace.username, teamTag, task.teamTagNo);
        insertTaskLink(editor, href, `${teamTag}-${task.teamTagNo}`);
        dispatch(closeSearchTaskModal());
    };

    const openSearchTaskModal = () => {
        dispatch(popSearchTaskModal({
            visible: true,
            workspaceId: workspace.workspaceId,
            onSelect: onTaskSelect
        }));
    };

    return (
        <Button
            onClick={openSearchTaskModal}
            variant={ButtonVariants.hoverFilled}
            data-tooltip-top={t("textEditorInsertTaskLink")}
        >
            <LuSquareCheckBig/>
        </Button>
    );
};

export default TaskLinkButton;
