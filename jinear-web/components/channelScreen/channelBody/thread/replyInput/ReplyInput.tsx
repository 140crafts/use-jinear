import React, { useEffect, useRef } from "react";
import styles from "./ReplyInput.module.css";
import CustomKeyboardEventHandler from "@/components/tiptap/keyboardEventHandler/KeyboardEventHandler";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useToggle } from "@/hooks/useToggle";
import { LuReply, LuSendHorizonal, LuX } from "react-icons/lu";
import { useSendToThreadMutation } from "@/api/messageOperationApi";

interface ReplyInputProps {
  threadId: string;
}

const ReplyInput: React.FC<ReplyInputProps> = ({ threadId }) => {
  const { t } = useTranslation();
  const [replyVisible, toggleReplyVisible, setReplyVisible] = useToggle(false);
  const tiptapRef = useRef<ITiptapRef>(null);
  const [sendToThread, {
    isLoading: isSendToThreadLoading,
    isSuccess: isSendToThreadSuccess
  }] = useSendToThreadMutation();

  useEffect(() => {
    if (replyVisible && tiptapRef.current) {
      tiptapRef.current.focus();
    }
  }, [replyVisible]);

  useEffect(() => {
    if (isSendToThreadSuccess && tiptapRef.current) {
      tiptapRef.current.clearContent();
      setReplyVisible(false);
    }
  }, [isSendToThreadSuccess, setReplyVisible]);

  const onEnter = (html: string) => {
    sendToThread({ threadId, body: { body: html } });
  };

  const send = () => {
    if (tiptapRef.current) {
      onEnter(tiptapRef.current.getHTML());
    }
  };

  const KeyboardEventHandler = CustomKeyboardEventHandler({ onEnter, shouldClearContentOnEnter: false });

  return (
    <div className={styles.container}>
      {!replyVisible ?
        <Button variant={ButtonVariants.hoverFilled2} heightVariant={ButtonHeight.short} onClick={toggleReplyVisible}
                className={styles.replyToggleButton}>
          <LuReply />
          <div className={"spacer-w-1"} />
          {t("threadReplyMessageInputButton")}
        </Button> :
        <div className={styles.inputContainer}>
          <Tiptap
            ref={tiptapRef}
            className={styles.input}
            editorClassName={styles.input}
            placeholder={t("threadReplyMessageInputPlaceholder")}
            hideActionBarWhenEmpty={false}
            extensions={[KeyboardEventHandler]}
            editable={!isSendToThreadLoading}
          />
          <div className={styles.inputActionBar}>
            <Button
              variant={ButtonVariants.hoverFilled2}
              onClick={toggleReplyVisible}
              disabled={isSendToThreadLoading}
            >
              <LuX />
            </Button>
            <Button
              variant={ButtonVariants.hoverFilled2}
              disabled={isSendToThreadLoading}
              loading={isSendToThreadLoading}
              onClick={send}
            >
              <LuSendHorizonal />
            </Button>
          </div>
        </div>
      }
    </div>
  );
};

export default ReplyInput;