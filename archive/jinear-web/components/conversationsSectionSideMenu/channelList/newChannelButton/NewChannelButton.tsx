import React from "react";
import styles from "./NewChannelButton.module.css";
import Button, { ButtonHeight } from "@/components/button";
import cn from "classnames";
import useTranslation from "@/locals/useTranslation";
import { LuPlus } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { popNewChannelModal } from "@/slice/modalSlice";

interface NewChannelButtonProps {
  workspaceId: string;
}

const NewChannelButton: React.FC<NewChannelButtonProps> = ({ workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popModal = () => {
    dispatch(popNewChannelModal({ visible: true, workspaceId }));
  };

  return (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x} onClick={popModal}>
      <LuPlus />
      <span className={cn(styles.channelName, "single-line")}>
        {t("newChannelButton")}
      </span>
    </Button>
  );
};

export default NewChannelButton;