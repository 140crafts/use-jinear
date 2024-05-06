import React from "react";
import styles from "./NewConversationButton.module.css";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import Button, { ButtonHeight } from "@/components/button";
import { LuPlus } from "react-icons/lu";
import cn from "classnames";

interface NewConversationButtonProps {
  workspaceId: string;
}

const NewConversationButton: React.FC<NewConversationButtonProps> = ({ workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popModal = () => {
    // dispatch(popNewChannelModal({ visible: true, workspaceId }));
  };

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x} onClick={popModal}>
      <LuPlus />
      <span className={cn(styles.conversationName, "single-line")}>
        {t("newConversationButton")}
      </span>
    </Button>
  );
};

export default NewConversationButton;