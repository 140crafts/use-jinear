import React, { useRef } from "react";
import styles from "./NewThreadInput.module.scss";
import Button, { ButtonVariants } from "@/components/button";
import { LuPlus } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";

interface NewThreadInputProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
  width: number;
}

const NewThreadInput: React.FC<NewThreadInputProps> = ({ workspaceName, channelId, workspaceId, width }) => {
  const { t } = useTranslation();
  const tiptapRef = useRef<ITiptapRef>(null);

  return (
    <div className={styles.container}>
      <div className={styles.inputContainer} style={{ width: width }}>
        <div className={styles.newCommentContainer}>
          <Tiptap
            ref={tiptapRef}
            className={styles.input}
            editorClassName={styles.input}
            placeholder={t("newThreadInputPlaceholder")}
            editable={true}
            hideActionBarWhenEmpty={true}
          />
        </div>
      </div>
      <Button variant={ButtonVariants.filled2} className={styles.newThreadButton}>
        <LuPlus />
        <div>{t("newThreadFloatingButtonLabel")}</div>
      </Button>
    </div>
  );
};

export default NewThreadInput;