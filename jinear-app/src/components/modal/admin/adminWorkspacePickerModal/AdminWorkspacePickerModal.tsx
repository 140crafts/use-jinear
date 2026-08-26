import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Modal from "@/components/modal/modal/Modal";
import Pagination from "@/components/pagination/Pagination";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {useAdminRetrieveWorkspacesQuery} from "@/store/api/adminWorkspaceApi";
import {
    closeAdminWorkspacePickerModal,
    selectAdminWorkspacePickerModalOnPick,
    selectAdminWorkspacePickerModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import styles from "./AdminWorkspacePickerModal.module.css";

interface AdminWorkspacePickerModalProps {
}

const AdminWorkspacePickerModal: React.FC<AdminWorkspacePickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);

    const visible = useTypedSelector(selectAdminWorkspacePickerModalVisible);
    const onPick = useTypedSelector(selectAdminWorkspacePickerModalOnPick);

    const {data: response, isLoading, isFetching} = useAdminRetrieveWorkspacesQuery({page}, {skip: !visible});

    useEffect(() => {
        if (visible) {
            setPage(0);
        }
    }, [visible]);

    const close = () => {
        dispatch(closeAdminWorkspacePickerModal());
    };

    const pick = (workspace: WorkspaceDto) => {
        onPick?.(workspace);
        close();
    };

    return (
        <Modal
            visible={visible}
            title={t("adminWorkspacePickerModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <div className={styles.header}>
                {response && (
                    <Pagination
                        id={"admin-workspace-picker-paginator"}
                        className={styles.pagination}
                        pageNumber={response.data.number}
                        pageSize={response.data.size}
                        totalPages={response.data.totalPages}
                        totalElements={response.data.totalElements}
                        hasPrevious={response.data.hasPrevious}
                        hasNext={response.data.hasNext}
                        isLoading={isLoading || isFetching}
                        page={page}
                        setPage={setPage}
                    />
                )}
            </div>
            <div className={styles.list}>
                {response?.data.content.map((workspace) => (
                    <Button
                        key={`admin-workspace-picker-${workspace.workspaceId}`}
                        className={styles.row}
                        variant={ButtonVariants.hoverFilled2}
                        onClick={() => pick(workspace)}
                    >
                        <span className={styles.rowInfo}>
                            <span className={styles.title}>{workspace.title}</span>
                            <span className={styles.username}>{workspace.username}</span>
                        </span>
                    </Button>
                ))}
                {!response?.data.hasContent && !isFetching && (
                    <div className={styles.emptyLabel}>{t("adminWorkspacesEmptyLabel")}</div>
                )}
            </div>
            {isFetching && !response && (
                <div className={styles.loading}>
                    <CircularLoading/>
                </div>
            )}
        </Modal>
    );
};

export default AdminWorkspacePickerModal;
