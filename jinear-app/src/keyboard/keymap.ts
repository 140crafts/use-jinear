// Not Cmd/Ctrl: browsers reserve Cmd/Ctrl + 1..9, T, W, N, L and the page cannot override them.
export const SHORTCUT_MODIFIER = "Alt";

export const shortcuts = {
    navCalendar: "1",
    navTasks: "2",
    navNotes: "3",
    navFiles: "4",
    navInstanceSettings: "5",
    inbox: "i",
    switchWorkspace: "w",

    newTask: "n",
    searchTask: "k",
    lastActivities: "l",
    assignedToMe: "a",

    newNote: "n",

    calendarPrevious: "b",
    calendarToday: "t",
    calendarNext: "f",

    // No digits or Q below: the title field has focus, and on a Turkish Mac Option + Q types @, Option + digits type #, $, {, [.
    taskTitle: "e",
    taskTeam: "m",
    taskTopic: "t",
    taskAssignee: "a",
    taskCollaborators: "c",
    taskBoard: "b",
    taskAssignedDate: "s",
    taskDueDate: "d",
    taskCreate: "enter",

    pickerNew: "n",
    pickerConfirm: "enter",
    pickerClear: "x",

    taskMenuOpen: "o",
    taskMenuOpenInNewTab: "n",
    taskMenuCopyLink: "l",
    taskMenuStatus: "s",
    taskMenuTopic: "t",
    taskMenuDates: "d",
    taskMenuAssignee: "a",
    taskMenuCollaborators: "c",
    taskMenuBoards: "b",
} as const;

export type ShortcutId = keyof typeof shortcuts;

export const JUMP_MODE_KEY = "j";

export const JUMP_LABEL_CHARACTERS = "asdfghjklqwertyuiopzxcvbnm";

export const SHORTCUT_ATTRIBUTE = "data-kb";

export const SCOPE_ATTRIBUTE = "data-kb-scope";

export const CLEAR_ATTRIBUTE = "data-kb-clear";

export const ACTIVE_ATTRIBUTE = "data-kb-active";
export const NAV_SKIP_ATTRIBUTE = "data-kb-nav-skip";

export const DISMISS_ATTRIBUTE = "data-kb-dismiss";
