import React, { useEffect, useRef } from "react";
import styles from "./ReplyConversationInput.module.scss";
import Button, { ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { LuPencilLine, LuSendHorizonal } from "react-icons/lu";
import CustomKeyboardEventHandler from "@/components/tiptap/keyboardEventHandler/KeyboardEventHandler";
import useTranslation from "@/locals/useTranslation";
import { useSendToConversationMutation } from "@/api/messageOperationApi";
import { useToggle } from "@/hooks/useToggle";

interface ReplyConversationInputProps {
  workspaceId: string;
  conversationId: string;
  width: number;
}

const ReplyConversationInput: React.FC<ReplyConversationInputProps> = ({ workspaceId, conversationId, width }) => {
  const { t } = useTranslation();
  const tiptapRef = useRef<ITiptapRef>(null);
  const [sendToConversation, {
    isLoading: isSendToConversationLoading,
    isSuccess: isSendToConversationSuccess
  }] = useSendToConversationMutation();
  const [actionBarVisible, toggleActionBarVisible, setActionBarVisible] = useToggle(false);

  useEffect(() => {
    if (isSendToConversationSuccess && tiptapRef.current) {
      tiptapRef.current.clearContent();
    }
  }, [isSendToConversationSuccess]);

  const onEnter = (html: string) => {
    sendToConversation({ workspaceId, conversationId, body: { body: html } });
  };

  const send = () => {
    if (tiptapRef.current) {
      onEnter(tiptapRef.current.getHTML());
    }
  };

  const KeyboardEventHandler = CustomKeyboardEventHandler({ onEnter, shouldClearContentOnEnter: false });

  return (
    <div className={styles.container} style={{ width: width }}>
      <div className={styles.inputContainer}>
        <Tiptap
          ref={tiptapRef}
          className={styles.input}
          editorClassName={styles.input}
          placeholder={t("threadReplyMessageInputPlaceholder")}
          actionBarMode={actionBarVisible ? "full" : "none"}
          hideActionBarWhenEmpty={false}
          extensions={[KeyboardEventHandler]}
          editable={!isSendToConversationLoading}
        />
        <div className={styles.inputActionBar}>
          <Button
            variant={actionBarVisible ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            disabled={isSendToConversationLoading}
            onClick={toggleActionBarVisible}
          >
            <LuPencilLine />
          </Button>
          <Button
            variant={ButtonVariants.hoverFilled2}
            disabled={isSendToConversationLoading}
            loading={isSendToConversationLoading}
            onClick={send}
          >
            <LuSendHorizonal />
          </Button>
        </div>
      </div>
    </div>);
};

export default ReplyConversationInput;