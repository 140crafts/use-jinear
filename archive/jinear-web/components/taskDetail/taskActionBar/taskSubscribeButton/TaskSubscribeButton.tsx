import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import {
  useInitializeTaskSubscriptionMutation,
  useRemoveTaskSubscriptionMutation,
  useRetrieveSubscriptionQuery,
} from "@/store/api/taskSubscriptionApi";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../../context/TaskDetailContext";
import styles from "./TaskSubscribeButton.module.css";

interface TaskSubscribeButtonProps {
  className: string;
}

const TaskSubscribeButton: React.FC<TaskSubscribeButtonProps> = ({ className }) => {
  const { t } = useTranslation();
  const task = useTask();
  const { data: taskSubscriptionResponse, isFetching: isRetrieveFetching } = useRetrieveSubscriptionQuery({
    taskId: task.taskId,
  });
  const isSubscribed = taskSubscriptionResponse?.data != null;

  const [initializeTaskSubscription, { isLoading: isInitializeSubscriptionLoading }] = useInitializeTaskSubscriptionMutation();
  const [removeTaskSubscription, { isLoading: isRemoveTaskSubscriptionLoading }] = useRemoveTaskSubscriptionMutation();

  const anyRequestLoading = isRetrieveFetching || isInitializeSubscriptionLoading || isRemoveTaskSubscriptionLoading;

  const toggleSubscription = () => {
    const req = { taskId: task.taskId };
    if (isSubscribed) {
      removeTaskSubscription(req);
    } else {
      initializeTaskSubscription(req);
    }
  };

  return (
    <Button
      disabled={anyRequestLoading}
      loading={anyRequestLoading}
      className={cn(styles.subscribeButton, className)}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      onClick={toggleSubscription}
      data-tooltip-right={
        isSubscribed ? t("taskSubscriptionActionButtonUnsubscribeTooltip") : t("taskSubscriptionActionButtonSubscribeTooltip")
      }
    >
      {isSubscribed ? t("taskSubscriptionActionButtonUnsubscribe") : t("taskSubscriptionActionButtonSubscribe")}
    </Button>
  );
};

export default TaskSubscribeButton;
