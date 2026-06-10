import React, { useEffect, useRef } from "react";
import styles from "./NewThreadInput.module.scss";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuSendHorizonal, LuX } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { useChannelFromChannelMemberships } from "@/hooks/messaging/useChannelFromChannelMemberships";
import { useChannelMembership } from "@/hooks/messaging/useChannelMembership";
import Logger from "@/utils/logger";
import CustomKeyboardEventHandler from "@/components/tiptap/keyboardEventHandler/KeyboardEventHandler";
import { useInitializeThreadMutation } from "@/api/threadApi";
import { useToggle } from "@/hooks/useToggle";
import { scrollToBottom } from "@/utils/htmlUtils";

interface NewThreadInputProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
  width: number;
}

const logger = Logger("NewThreadInput");

const NewThreadInput: React.FC<NewThreadInputProps> = ({ workspaceName, channelId, workspaceId, width }) => {
  const { t } = useTranslation();
  const [inputVisible, toggleInputVisible, setInputVisible] = useToggle(false);
  const [initializeThread, {
    data: initializeThreadResponse,
    isLoading: isInitializeThreadLoading
  }] = useInitializeThreadMutation();

  const tiptapRef = useRef<ITiptapRef>(null);
  const channel = useChannelFromChannelMemberships({ workspaceName, channelId });
  const channelMembership = useChannelMembership({ workspaceId: channel?.workspaceId, channelId: channel?.channelId });
  const isAdmin = ["ADMIN", "OWNER"].includes(channelMembership?.roleType ?? "");
  const canStartThread = channel?.participationType == "EVERYONE" || isAdmin;

  useEffect(() => {
    if (tiptapRef.current && !isInitializeThreadLoading) {
      tiptapRef.current.clearContent();
      setInputVisible(false);
    }
  }, [tiptapRef, channelId, isInitializeThreadLoading, initializeThreadResponse, setInputVisible]);

  useEffect(() => {
    if (inputVisible) {
      setTimeout(() => {
        tiptapRef?.current?.focus();
      }, 250);
    }
  }, [inputVisible]);

  const onEnter = (html: string) => {
    initializeThread({ channelId, initialMessageBody: html });
  };

  const submit = () => {
    if (tiptapRef.current) {
      const html = tiptapRef.current.getHTML();
      initializeThread({ channelId, initialMessageBody: html });
    }
  };

  const onNewThreadButtonClick = () => {
    scrollToBottom();
    toggleInputVisible();
  };

  const KeyboardEventHandler = CustomKeyboardEventHandler({ onEnter });

  return canStartThread ? (
    inputVisible ? <div className={styles.inputContainer} style={{ width: width }}>
      <div className={styles.newThreadInputContainer}>
        <Tiptap
          ref={tiptapRef}
          className={styles.input}
          editorClassName={styles.input}
          placeholder={t("newThreadInputPlaceholder")}
          editable={!isInitializeThreadLoading}
          hideActionBarWhenEmpty={true}
          extensions={[KeyboardEventHandler]}
          onFocus={scrollToBottom}
        />
        <div className={styles.actionBar}>
          <Button
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
            onClick={toggleInputVisible}
          >
            <LuX />
          </Button>
          <Button
            loading={isInitializeThreadLoading}
            disabled={isInitializeThreadLoading}
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
            className={styles.submitButton}
            onClick={submit}
            data-tooltip-right={t("newThreadButtonLabel")}
          >
            <LuSendHorizonal />
          </Button>
        </div>
      </div>
    </div> : <Button
      variant={ButtonVariants.contrast}
      className={styles.newThreadInputToggleButton}
      onClick={onNewThreadButtonClick}
    >
      {t("newThreadFloatingButtonLabel")}
    </Button>
  ) : null;
};

export default NewThreadInput;