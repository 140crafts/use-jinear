import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Modal from "@/components/modal/modal/Modal";
import {useAdminRetrieveAccountWorkspacesQuery} from "@/store/api/adminAccountApi";
import {
    closeAdminAccountWorkspacesModal,
    selectAdminAccountWorkspacesModalAccount,
    selectAdminAccountWorkspacesModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./AdminAccountWorkspacesModal.module.css";

interface AdminAccountWorkspacesModalProps {
}

const AdminAccountWorkspacesModal: React.FC<AdminAccountWorkspacesModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectAdminAccountWorkspacesModalVisible);
    const account = useTypedSelector(selectAdminAccountWorkspacesModalAccount);
    const accountId = account?.accountId;
    const {data: response, isFetching} = useAdminRetrieveAccountWorkspacesQuery(
        {accountId: accountId ?? ""},
        {skip: !visible || !accountId}
    );

    const close = () => {
        dispatch(closeAdminAccountWorkspacesModal());
    };

    return (
        <Modal
            visible={visible}
            title={t("adminAccountWorkspacesModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <div className={styles.accountEmail}>{account?.email}</div>
            {response && response.data.length > 0 && (
                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                        <tr>
                            <th className={styles.titleHeader}>{t("adminTableHeaderTitle")}</th>
                            <th>{t("adminTableHeaderHandle")}</th>
                            <th>{t("adminTableHeaderRole")}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {response.data.map((membership) => (
                            <tr key={`admin-account-workspace-${membership.workspaceMemberId}`}>
                                <td className={styles.titleCell}>{membership.workspace.title}</td>
                                <td className={styles.paleCell}>{membership.workspace.username}</td>
                                <td className={styles.roleCell}>{t(`workspaceMemberRole_${membership.role}`)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            {response && response.data.length == 0 && !isFetching && (
                <div className={styles.emptyLabel}>{t("adminAccountWorkspacesModalEmptyLabel")}</div>
            )}
            {isFetching && !response && (
                <div className={styles.loading}>
                    <CircularLoading/>
                </div>
            )}
        </Modal>
    );
};

export default AdminAccountWorkspacesModal;
