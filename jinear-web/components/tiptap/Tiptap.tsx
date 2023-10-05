import Mention from "@tiptap/extension-mention";
import { EditorContent, useEditor } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";

import Logger from "@/utils/logger";
import React, { useRef, useState } from "react";
import Button from "../button";
import { MentionList } from "./mentionList/MentionList";

interface TiptapProps {
  suggestionList?: string[];
  readOnly?: boolean;
  initialValue?: string;
}

const logger = Logger("Tiptap");
const Tiptap: React.FC<TiptapProps> = ({ readOnly = false, initialValue, suggestionList = [] }) => {
  const mentionListRef = useRef(null);
  const [mentionListRect, setMentionListRect] = useState<DOMRect | null | undefined>(null);
  const [mentionListItems, setMentionListItems] = useState<string[]>([]);
  const commandFunc = useRef<(props: any) => void>(() => {});

  const editor = useEditor({
    extensions: [
      StarterKit,
      Mention.configure({
        HTMLAttributes: {
          class: "mention",
        },
        suggestion: {
          items: ({ editor, query }) =>
            suggestionList.filter((item) => item.toLowerCase().startsWith(query.toLowerCase())).slice(0, 5),
          render: () => {
            let component;
            let popup;

            return {
              onStart: (props) => {
                logger.log({ onStart: props, clientRect: props.clientRect?.() });
                setMentionListRect(props.clientRect?.());
              },

              onUpdate(props) {
                logger.log({ onUpdate: props });
                setMentionListRect(props.clientRect?.());
                setMentionListItems(props.items);
                commandFunc.current = props.command;
              },

              onKeyDown(props) {
                logger.log({ onKeyDown: props });
                if (props.event.key === "Escape") {
                  setMentionListRect(null);
                  return true;
                }
                //@ts-ignore
                return mentionListRef?.current?.onKeyDown?.(props);
              },

              onExit() {
                logger.log({ onExit: "onExit" });
                setMentionListRect(null);
              },
            };
          },
        },
      }),
    ],
    content: initialValue,
    editable: !readOnly,
    injectCSS: false,
  });

  return (
    <div>
      <Button
        onClick={() => {
          const html = editor?.getHTML();
          logger.log({ html });
        }}
      >
        getHtml
      </Button>
      <Button
        onClick={() => {
          const json = editor?.getJSON();
          logger.log({ json });
        }}
      >
        json
      </Button>

      <EditorContent editor={editor} />
      {mentionListRect && (
        <MentionList
          mentionListRect={mentionListRect}
          items={mentionListItems}
          command={commandFunc.current}
          ref={mentionListRef}
        />
      )}
    </div>
  );
};

export default Tiptap;
