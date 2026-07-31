import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { popChangeTaskTopicModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store";
import { getTextColor } from "@/util/colorHelper";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";

interface ChangeTopicButtonProps {}

const ChangeTopicButton: React.FC<ChangeTopicButtonProps> = ({}) => {
  const task = useTask();
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeTopicModal = () => {
    dispatch(popChangeTaskTopicModal({ visible: true, task }));
  };

  return (
    <Button
      variant={ButtonVariants.outline}
      heightVariant={ButtonHeight.short}
      data-tooltip-right={t("taskDetailChangeTopicTooltip")}
      onClick={popChangeTopicModal}
      style={{
        backgroundColor: task.topic ? `#${task.topic?.color}` : undefined,
      }}
    >
      {task.topic ? (
        <b
          style={{
            color: task.topic ? getTextColor(`#${task.topic.color}`) : undefined,
          }}
        >
          {task.topic.name}
        </b>
      ) : (
        <>{t("taskDetailInfoActionBarAssignTopic")}</>
      )}
    </Button>
  );
};

export default ChangeTopicButton;
