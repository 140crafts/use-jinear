import { Extension } from "@tiptap/core";

const CustomKeyboardEventHandler = ({ onEnter, shouldClearContentOnEnter = false }: {
  onEnter?: (html: string) => void,
  shouldClearContentOnEnter: boolean
}) => {
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
          if (shouldClearContentOnEnter) {
            return this.editor.commands.clearContent();
          }
          return true;
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