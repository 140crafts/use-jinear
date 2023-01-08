import Button from "@/components/button";
import OrLine from "@/components/orLine/OrLine";
import { useUpdateTaskAssigneeMutation } from "@/store/api/taskUpdateApi";
import {
  changeLoadingModalVisibility,
  selectChangeTaskAssigneeModalTaskCurrentAssigneeId,
  selectChangeTaskAssigneeModalTaskId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./RemoveCurrentAssignee.module.css";

interface RemoveCurrentAssigneeProps {
  close: () => void;
}

const RemoveCurrentAssignee: React.FC<RemoveCurrentAssigneeProps> = ({
  close,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const taskId = useTypedSelector(selectChangeTaskAssigneeModalTaskId);
  const taskCurrentAssigneeId = useTypedSelector(
    selectChangeTaskAssigneeModalTaskCurrentAssigneeId
  );
  const [updateTaskAssignee, { isSuccess, isError }] =
    useUpdateTaskAssigneeMutation();

  useEffect(() => {
    if (isSuccess || isError) {
      dispatch(changeLoadingModalVisibility({ visible: false }));
    }
    if (isSuccess) {
      close();
    }
  }, [isSuccess, isError]);

  const removeAssignee = () => {
    if (taskId) {
      dispatch(changeLoadingModalVisibility({ visible: true }));
      updateTaskAssignee({ taskId, body: { assigneeId: null } });
    }
  };

  return !taskCurrentAssigneeId ? null : (
    <div className={styles.container}>
      <OrLine />
      <Button className={styles.button} onClick={removeAssignee}>
        {t("changeTaskTopicModalRemoveTopic")}
      </Button>
    </div>
  );
};
export default RemoveCurrentAssignee;
