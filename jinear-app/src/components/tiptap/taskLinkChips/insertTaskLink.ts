import type {Editor} from "@tiptap/core";

export const insertTaskLink = (editor: Editor, href: string, taskTag: string) =>
    editor.chain().focus().insertContent([
        {type: "text", text: taskTag.toUpperCase(), marks: [{type: "link", attrs: {href}}]},
        {type: "text", text: " "}
    ]).run();
