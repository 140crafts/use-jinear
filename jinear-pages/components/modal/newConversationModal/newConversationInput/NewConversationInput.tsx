import React, { useRef } from "react";
import styles from "./NewConversationInput.module.scss";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { LuPencilLine } from "react-icons/lu";
import CustomKeyboardEventHandler from "@/components/tiptap/keyboardEventHandler/KeyboardEventHandler";
import useTranslation from "@/locals/useTranslation";
import { useToggle } from "@/hooks/useToggle";

interface NewConversationInputProps {
  onSubmit: (html: string) => void;
  isLoading: boolean;
}

const NewConversationInput: React.FC<NewConversationInputProps> = ({ onSubmit, isLoading }) => {
  const { t } = useTranslation();
  const tiptapRef = useRef<ITiptapRef>(null);
  const [actionBarVisible, toggleActionBarVisible, setActionBarVisible] = useToggle(false);

  const onEnter = (html: string) => {
    onSubmit(html);
  };

  const send = () => {
    if (tiptapRef.current) {
      onEnter(tiptapRef.current.getHTML());
    }
  };

  const KeyboardEventHandler = CustomKeyboardEventHandler({ onEnter, shouldClearContentOnEnter: false });

  return (
    <div className={styles.container}>
      <div className={styles.inputContainer}>
        <Tiptap
          ref={tiptapRef}
          className={styles.input}
          editorClassName={styles.input}
          placeholder={t("threadReplyMessageInputPlaceholder")}
          actionBarMode={actionBarVisible ? "full" : "none"}
          hideActionBarWhenEmpty={false}
          extensions={[KeyboardEventHandler]}
          editable={!isLoading}
        />
        <div className={styles.inputActionBar}>
          <Button
            variant={actionBarVisible ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            disabled={isLoading}
            onClick={toggleActionBarVisible}
            heightVariant={ButtonHeight.short}
          >
            <LuPencilLine />
          </Button>
          <Button
            variant={ButtonVariants.contrast}
            disabled={isLoading}
            loading={isLoading}
            onClick={send}
            heightVariant={ButtonHeight.short}
          >
            {t("newConversationSend")}
          </Button>
        </div>
      </div>
    </div>);
};

export default NewConversationInput;