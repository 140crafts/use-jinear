import React from "react";
import styles from "./JoinableChannelListButton.module.css";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { popNewChannelModal } from "@/slice/modalSlice";
import Button, { ButtonHeight } from "@/components/button";
import { LuChevronRight, LuPlus } from "react-icons/lu";
import cn from "classnames";

interface JoinableChannelListButtonProps {
  workspaceId: string;
  count: number;
}

const JoinableChannelListButton: React.FC<JoinableChannelListButtonProps> = ({ workspaceId, count = 0 }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popModal = () => {
    dispatch(popNewChannelModal({ visible: true, workspaceId }));
  };

  return count > 0 ? (
    <Button className={styles.container} heightVariant={ButtonHeight.short2x} onClick={popModal}>
      <LuChevronRight />
      <span className={cn(styles.label, "single-line")}>
        {t("joinChannelButton").replace("${number}", `${count}`)}
      </span>
    </Button>
  ) : null;
};

export default JoinableChannelListButton;