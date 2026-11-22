import {type Editor, Extension} from "@tiptap/core";
import type {Node as ProseMirrorNode} from "@tiptap/pm/model";
import {Plugin, PluginKey} from "@tiptap/pm/state";
import {Decoration, DecorationSet} from "@tiptap/pm/view";
import type {EditorView} from "@tiptap/pm/view";
import {parseTaskLinkTag} from "@/util/taskLinkUtils";
import {insertTaskLink} from "./insertTaskLink";

const LINK_MARK = "link";

interface TaskLinkChipsOptions {
    workspaceUsername?: string;
    chipClassName: string;
    onTaskTagsChange?: (taskTags: string[]) => void;
}

interface TaskLinkChipsStorage {
    reportedTaskTags: string[];
}

interface TaskLinkRange {
    from: number;
    to: number;
    href: string;
}

interface TaskLinkChipsState {
    decorations: DecorationSet;
    taskTags: string[];
}

const taskLinkChipsKey = new PluginKey<TaskLinkChipsState>("taskLinkChips");

const linkHrefOf = (node: ProseMirrorNode): string | undefined =>
    node.marks.find((mark) => mark.type.name === LINK_MARK)?.attrs.href;

const appendRange = (ranges: TaskLinkRange[], range: TaskLinkRange) => {
    const last = ranges[ranges.length - 1];
    if (last && last.to === range.from && last.href === range.href) {
        last.to = range.to;
        return;
    }
    ranges.push(range);
};

const scanTaskLinks = (doc: ProseMirrorNode, options: TaskLinkChipsOptions): TaskLinkChipsState => {
    const ranges: TaskLinkRange[] = [];
    const taskTags = new Set<string>();
    doc.descendants((node, pos) => {
        if (!node.isText) {
            return true;
        }
        const href = linkHrefOf(node);
        const taskTag = parseTaskLinkTag(href, options.workspaceUsername);
        if (href && taskTag) {
            taskTags.add(taskTag);
            appendRange(ranges, {from: pos, to: pos + node.nodeSize, href});
        }
        return false;
    });
    const decorations = ranges.map(({from, to}) => Decoration.inline(from, to, {class: options.chipClassName}));
    return {decorations: DecorationSet.create(doc, decorations), taskTags: [...taskTags]};
};

const sameTaskTags = (first: string[], second: string[]) =>
    first.length === second.length && first.every((taskTag, index) => taskTag === second[index]);

const pastedTaskLink = (view: EditorView, event: ClipboardEvent, workspaceUsername?: string) => {
    const href = event.clipboardData?.getData("text/plain").trim();
    const taskTag = parseTaskLinkTag(href, workspaceUsername);
    if (!view.state.selection.empty || !href || !taskTag) {
        return null;
    }
    return {href, taskTag};
};

const reportTaskTags = (editor: Editor, storage: TaskLinkChipsStorage, options: TaskLinkChipsOptions) => {
    const taskTags = taskLinkChipsKey.getState(editor.state)?.taskTags ?? [];
    if (sameTaskTags(taskTags, storage.reportedTaskTags)) {
        return;
    }
    storage.reportedTaskTags = taskTags;
    options.onTaskTagsChange?.(taskTags);
};

export const TaskLinkChips = Extension.create<TaskLinkChipsOptions, TaskLinkChipsStorage>({
    name: "taskLinkChips",

    priority: 1001,

    addOptions() {
        return {
            workspaceUsername: undefined,
            chipClassName: "",
            onTaskTagsChange: undefined
        };
    },

    addStorage() {
        return {
            reportedTaskTags: []
        };
    },

    addProseMirrorPlugins() {
        const options = this.options;
        const editor = this.editor;
        return [
            new Plugin<TaskLinkChipsState>({
                key: taskLinkChipsKey,
                state: {
                    init: (_config, state) => scanTaskLinks(state.doc, options),
                    apply: (transaction, value) => transaction.docChanged ? scanTaskLinks(transaction.doc, options) : value
                },
                props: {
                    decorations: (state) => taskLinkChipsKey.getState(state)?.decorations,
                    handlePaste: (view, event) => {
                        const taskLink = pastedTaskLink(view, event, options.workspaceUsername);
                        return taskLink ? insertTaskLink(editor, taskLink.href, taskLink.taskTag) : false;
                    }
                }
            })
        ];
    },

    onCreate() {
        reportTaskTags(this.editor, this.storage, this.options);
    },

    onTransaction() {
        reportTaskTags(this.editor, this.storage, this.options);
    }
});
