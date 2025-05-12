import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useInitializeChecklistMutation } from "@/store/api/taskChecklistApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { useTask } from "../../context/TaskDetailContext";

interface TaskAddChecklistButtonProps {
  className: string;
}

const TaskAddChecklistButton: React.FC<TaskAddChecklistButtonProps> = ({ className }) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const task = useTask();

  const [initializeChecklist, { isLoading: isInitializeChecklistLoading }] = useInitializeChecklistMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isInitializeChecklistLoading }));
  }, [isInitializeChecklistLoading]);

  const addChecklist = () => {
    const checklistCount = task.checklists?.length != null ? task.checklists.length : 0;
    const newChecklistTitle = t("newChecklistDefaultTitle").replace("${no}", `${checklistCount + 1}`);
    initializeChecklist({
      taskId: task.taskId,
      title: newChecklistTitle,
    });
  };

  return (
    <Button className={className} variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={addChecklist}>
      {t("taskDetailAddChecklistButtonLabel")}
    </Button>
  );
};

export default TaskAddChecklistButton;
