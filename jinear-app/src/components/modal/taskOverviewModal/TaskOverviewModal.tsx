import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import TaskDetail from "@/components/taskDetail/TaskDetail";
import useWindowSize from "@/hooks/useWindowSize";
import {useRetrieveWithWorkspaceNameAndTeamTagNoQuery} from "@/store/api/taskApi";
import {
    closeTaskOverviewModal, selectTaskOverviewModalTask,
    selectTaskOverviewModalTaskTag,
    selectTaskOverviewModalVisible,
    selectTaskOverviewModalWorkspaceName,
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {IoResize} from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./TaskOverviewModal.module.scss";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TaskOverviewModalProps {
}

const TaskOverviewModal: React.FC<TaskOverviewModalProps> = ({}) => {
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectTaskOverviewModalVisible);
    const {isMobile} = useWindowSize();
    const taskTag = useTypedSelector(selectTaskOverviewModalTaskTag);
    const workspaceName = useTypedSelector(selectTaskOverviewModalWorkspaceName);
    const task = useTypedSelector(selectTaskOverviewModalTask);

    const {
        currentData: taskResponse,
        isFetching: isTaskResponseFetching,
    } = useRetrieveWithWorkspaceNameAndTeamTagNoQuery(
        {workspaceName: workspaceName || "", taskTag: taskTag || ""},
        {skip: workspaceName == null || taskTag == null}
    );

    const taskToView = taskResponse?.data ? taskResponse?.data : task;

    const close = () => {
        dispatch(closeTaskOverviewModal());
    };

    return (
        <Modal
            visible={visible}
            width={isMobile ? "fullscreen" : "xxlarge"}
            title={taskTag ? `[${taskTag}] ${taskToView?.title || ""}` : ""}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.modalBody}
            contentContainerClass={styles.modal}
        >
            {isTaskResponseFetching && taskToView == null && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={21}/>
                </div>
            )}

            {taskToView && (
                <div className={styles.taskContentWrapper}>
                    {/* <TaskDetailHeader task={taskResponse.data} /> */}
                    <TaskDetail task={taskToView} withGoToTaskButton={true} onGoToTaskButtonClick={close}/>
                </div>
            )}
        </Modal>
    );
};

export default TaskOverviewModal;
