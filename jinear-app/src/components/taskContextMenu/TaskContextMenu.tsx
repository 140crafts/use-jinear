import React, {useEffect, useLayoutEffect, useRef, useState} from "react";
import {format} from "date-fns";
import toast from "react-hot-toast";
import {useNavigate} from "react-router-dom";
import type {IconType} from "react-icons/lib";
import {
    LuCalendarRange,
    LuExternalLink,
    LuFileText,
    LuFolderKanban,
    LuLink,
    LuTag,
    LuUser,
    LuUsers
} from "react-icons/lu";
import {useAppDispatch, useTypedSelector} from "@/store";
import {
    closeTaskContextMenu,
    popChangeTaskAssigneeModal,
    popChangeTaskDateModal,
    popChangeTaskTopicModal,
    popChangeTaskWorkflowStatusModal,
    popTaskOverviewModal,
    popTaskTaskBoardAssignModal,
    selectTaskContextMenu
} from "@/store/slice/modalSlice";
import useTranslation, {type StringKeys} from "@/locales/useTranslation";
import useWindowSize from "@/hooks/useWindowSize";
import {useTaskCollaboratorPicker} from "@/hooks/useTaskCollaboratorPicker";
import {copyTextToClipboard} from "@/util/clipboard";
import {HOST} from "@/util/constants";
import {retrieveTaskStatusIcon} from "@/util/taskIconFactory";
import {DISMISS_ATTRIBUTE, SCOPE_ATTRIBUTE, SHORTCUT_ATTRIBUTE, type ShortcutId} from "@/keyboard/keymap";
import styles from "./TaskContextMenu.module.css";

interface MenuItem {
    shortcut: ShortcutId;
    label: StringKeys;
    icon: IconType;
    value?: string;
    disabled?: boolean;
    onSelect: () => void;
}

const EDGE_GAP = 8;

const TaskContextMenu: React.FC = () => {
    const {t, dateFnsLocale} = useTranslation();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const {isMobile} = useWindowSize();
    const menu = useTypedSelector(selectTaskContextMenu);
    const task = menu?.visible ? menu.task : undefined;
    const {popCollaboratorPicker, isReady: collaboratorsReady} = useTaskCollaboratorPicker(task);
    const menuRef = useRef<HTMLDivElement>(null);
    const [position, setPosition] = useState({top: 0, left: 0});

    useLayoutEffect(() => {
        if (!task || !menuRef.current) {
            return;
        }
        const {width, height} = menuRef.current.getBoundingClientRect();
        const x = menu?.x ?? 0;
        const y = menu?.y ?? 0;
        const left = x + width + EDGE_GAP > window.innerWidth ? Math.max(EDGE_GAP, x - width) : x;
        const top = y + height + EDGE_GAP > window.innerHeight ? Math.max(EDGE_GAP, window.innerHeight - height - EDGE_GAP) : y;
        setPosition({top, left});
    }, [task, menu?.x, menu?.y]);

    useEffect(() => {
        if (!task) {
            return;
        }
        const close = () => dispatch(closeTaskContextMenu());
        window.addEventListener("resize", close);
        return () => window.removeEventListener("resize", close);
    }, [task, dispatch]);

    if (!task) {
        return null;
    }

    const taskPath = `/${task.workspace?.username}/task/${task.team?.tag}-${task.teamTagNo}`;

    const close = () => dispatch(closeTaskContextMenu());

    const closeAnd = (action: () => void) => () => {
        close();
        action();
    };

    const openTask = () => {
        if (isMobile) {
            navigate(taskPath);
            return;
        }
        dispatch(popTaskOverviewModal({
            taskTag: `${task.team?.tag}-${task.teamTagNo}`,
            workspaceName: task.workspace?.username,
            task,
            visible: true
        }));
    };

    const copyLink = () => {
        copyTextToClipboard(`${HOST}${taskPath}`);
        toast(t("newTaskCreatedToastCopiedToClipboard"));
    };

    const dates = [task.assignedDate, task.dueDate]
        .filter((date) => date != null)
        .map((date) => format(new Date(date), "d MMM", {locale: dateFnsLocale}))
        .join(" - ");
    const collaboratorCount = task.taskCollaborators?.length ?? 0;
    const boardCount = task.taskBoardEntries?.length ?? 0;
    const none = t("taskContextMenuNone");

    const groups: MenuItem[][] = [
        [
            {shortcut: "taskMenuOpen", label: "taskContextMenuOpen", icon: LuFileText, onSelect: openTask},
            {
                shortcut: "taskMenuOpenInNewTab",
                label: "taskContextMenuOpenInNewTab",
                icon: LuExternalLink,
                onSelect: () => window.open(taskPath, "_blank", "noopener")
            },
            {shortcut: "taskMenuCopyLink", label: "taskContextMenuCopyLink", icon: LuLink, onSelect: copyLink},
        ],
        [
            {
                shortcut: "taskMenuStatus",
                label: "taskContextMenuStatus",
                icon: retrieveTaskStatusIcon(task.workflowStatus?.workflowStateGroup),
                value: task.workflowStatus?.name,
                onSelect: () => dispatch(popChangeTaskWorkflowStatusModal({visible: true, task}))
            },
            {
                shortcut: "taskMenuTopic",
                label: "taskContextMenuTopic",
                icon: LuTag,
                value: task.topic?.name ?? none,
                onSelect: () => dispatch(popChangeTaskTopicModal({visible: true, task}))
            },
            {
                shortcut: "taskMenuDates",
                label: "taskContextMenuDates",
                icon: LuCalendarRange,
                value: dates || none,
                onSelect: () => dispatch(popChangeTaskDateModal({visible: true, task}))
            },
            {
                shortcut: "taskMenuAssignee",
                label: "taskContextMenuAssignee",
                icon: LuUser,
                value: task.assignedToAccount?.username ?? none,
                onSelect: () => dispatch(popChangeTaskAssigneeModal({visible: true, task}))
            },
            {
                shortcut: "taskMenuCollaborators",
                label: "taskContextMenuCollaborators",
                icon: LuUsers,
                value: collaboratorCount == 0 ? none : `${collaboratorCount}`,
                disabled: !collaboratorsReady,
                onSelect: popCollaboratorPicker
            },
            {
                shortcut: "taskMenuBoards",
                label: "taskContextMenuBoards",
                icon: LuFolderKanban,
                value: boardCount == 0 ? none : `${boardCount}`,
                onSelect: () => dispatch(popTaskTaskBoardAssignModal({taskId: task.taskId, visible: true}))
            },
        ],
    ];

    const onBackdropContextMenu = (event: React.MouseEvent) => {
        event.preventDefault();
        close();
    };

    return (
        <div {...{[SCOPE_ATTRIBUTE]: true}} className={styles.container}>
            <div ref={menuRef} role="menu" className={styles.menu} style={position}>
                {groups.map((items, index) => (
                    <React.Fragment key={`task-context-menu-group-${index}`}>
                        {index != 0 && <div className={styles.separator}/>}
                        {items.map((item) => (
                            <button
                                key={item.shortcut}
                                type="button"
                                role="menuitem"
                                className={styles.item}
                                disabled={item.disabled}
                                onClick={closeAnd(item.onSelect)}
                                {...{[SHORTCUT_ATTRIBUTE]: item.shortcut}}
                            >
                                <item.icon className={styles.icon} size={15}/>
                                <span>{t(item.label)}</span>
                                {item.value && <span className={styles.value}>{item.value}</span>}
                            </button>
                        ))}
                    </React.Fragment>
                ))}
            </div>
            <div
                {...{[DISMISS_ATTRIBUTE]: true}}
                className={styles.backdrop}
                onClick={close}
                onContextMenu={onBackdropContextMenu}
            />
        </div>
    );
};

export default TaskContextMenu;
