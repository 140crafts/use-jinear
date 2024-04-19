import { Extension } from "@tiptap/core";

const CustomKeyboardEventHandler = ({ onEnter }: { onEnter?: (html: string) => void }) => {
  return Extension.create({
    name: "keyboardHandler",
    addKeyboardShortcuts() {
      return {
        Enter: () => {
          const isCodeBlockActive = this.editor.isActive("codeBlock");
          if (isCodeBlockActive) {
            return false;
          }
          onEnter?.(this.editor.getHTML());
          return this.editor.commands.clearContent();
        },
        "Shift-Enter": () => {
          return this.editor.commands.first(({ commands }) => [
            () => commands.newlineInCode(),
            () => commands.createParagraphNear(),
            () => commands.liftEmptyBlock(),
            () => commands.splitBlock()
          ]);
        }
      };
    }
  });
};

export default CustomKeyboardEventHandler;