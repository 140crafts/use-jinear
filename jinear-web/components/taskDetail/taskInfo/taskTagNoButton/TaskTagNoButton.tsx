import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { copyTextToClipboard } from "@/utils/clipboard";
import { HOST } from "@/utils/constants";
import useTranslation from "locales/useTranslation";
import React from "react";
import toast from "react-hot-toast";
import { useTask } from "../../context/TaskDetailContext";

interface TaskTagNoButtonProps {
  className?: string;
}

const TaskTagNoButton: React.FC<TaskTagNoButtonProps> = ({ className }) => {
  const task = useTask();
  const { t } = useTranslation();

  const copyToClipboard = () => {
    const teamTag = task.team?.tag;
    const teamTagNo = task.teamTagNo;
    const tag = `${teamTag}-${teamTagNo}`;
    const taskLink = `${task.workspace?.username}/task/${tag}`;
    copyTextToClipboard(`${HOST}/${taskLink}`);
    toast(t("newTaskCreatedToastCopiedToClipboard"));
  };

  return (
    <Button
      onClick={copyToClipboard}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.mid}
      className={className}
      data-tooltip-right={t("taskDetailCopyLinkTooltip")}
    >
      {task.team?.tag}-{task.teamTagNo}
    </Button>
  );
};

export default TaskTagNoButton;
