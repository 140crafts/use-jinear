import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useTaskTopicPicker } from "@/hooks/useTaskTopicPicker";
import { getTextColor } from "@/util/colorHelper";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";

interface ChangeTopicButtonProps {}

const ChangeTopicButton: React.FC<ChangeTopicButtonProps> = ({}) => {
  const task = useTask();
  const { t } = useTranslation();
  const { popTopicPicker, isReady } = useTaskTopicPicker(task);

  return (
    <Button
      variant={ButtonVariants.outline}
      heightVariant={ButtonHeight.short}
      data-tooltip-right={t("taskDetailChangeTopicTooltip")}
      disabled={!isReady}
      onClick={popTopicPicker}
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
