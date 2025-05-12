import Button from "@/components/button";
import OrLine from "@/components/orLine/OrLine";
import { useRemoveTaskTopicMutation } from "@/store/api/taskTopicApi";
import {
  changeLoadingModalVisibility,
  selectChangeTaskTopicModalTaskCurrentTopicId,
  selectChangeTaskTopicModalTaskId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./RemoveCurrentTopic.module.css";

interface RemoveCurrentTopicProps {
  close: () => void;
}

const RemoveCurrentTopic: React.FC<RemoveCurrentTopicProps> = ({ close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const taskId = useTypedSelector(selectChangeTaskTopicModalTaskId);
  const taskCurrentTopicId = useTypedSelector(selectChangeTaskTopicModalTaskCurrentTopicId);
  const [removeTaskTopic, { isSuccess, isError }] = useRemoveTaskTopicMutation();

  useEffect(() => {
    if (isSuccess || isError) {
      dispatch(changeLoadingModalVisibility({ visible: false }));
      close();
    }
  }, [isSuccess, isError]);

  const removeTopic = () => {
    if (taskCurrentTopicId && taskId) {
      dispatch(changeLoadingModalVisibility({ visible: true }));
      removeTaskTopic({ taskId });
    }
  };

  return !taskCurrentTopicId ? null : (
    <div className={styles.container}>
      <OrLine />
      <Button className={styles.button} onClick={removeTopic}>
        {t("changeTaskTopicModalRemoveTopic")}
      </Button>
    </div>
  );
};

export default RemoveCurrentTopic;
