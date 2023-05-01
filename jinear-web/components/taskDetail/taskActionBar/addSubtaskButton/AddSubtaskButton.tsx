import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask, useToggleShowSubTaskListEvenIfNoSubtasks } from "../../context/TaskDetailContext";

interface AddSubtaskButtonProps {
  className: string;
}

const AddSubtaskButton: React.FC<AddSubtaskButtonProps> = ({ className }) => {
  const task = useTask();
  const { t } = useTranslation();
  const toggleShowSubTaskListEvenIfNoSubtasks = useToggleShowSubTaskListEvenIfNoSubtasks();

  const taskHasNoActiveSubtasks = task?.relatedIn?.filter((relation) => relation.relationType == "SUBTASK")?.length == 0;

  return !taskHasNoActiveSubtasks ? null : (
    <Button
      onClick={toggleShowSubTaskListEvenIfNoSubtasks}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      className={className}
      data-tooltip-multiline={t("taskDetailCreateRelatedTasklist")}
    >
      {t("taskDetailCreateRelatedTasklist")}
    </Button>
  );
};

export default AddSubtaskButton;
