import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Modal from "@/components/modal/modal/Modal";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {useAdminRetrieveAccountTeamsQuery, useAdminRetrieveAccountWorkspacesQuery} from "@/store/api/adminAccountApi";
import {
    closeAdminAccountTeamsModal,
    selectAdminAccountTeamsModalAccount,
    selectAdminAccountTeamsModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useMemo} from "react";
import styles from "./AdminAccountTeamsModal.module.css";

interface AdminAccountTeamsModalProps {
}

const AdminAccountTeamsModal: React.FC<AdminAccountTeamsModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectAdminAccountTeamsModalVisible);
    const account = useTypedSelector(selectAdminAccountTeamsModalAccount);
    const accountId = account?.accountId;
    const {data: response, isFetching} = useAdminRetrieveAccountTeamsQuery(
        {accountId: accountId ?? ""},
        {skip: !visible || !accountId}
    );
    const {data: workspacesResponse} = useAdminRetrieveAccountWorkspacesQuery(
        {accountId: accountId ?? ""},
        {skip: !visible || !accountId}
    );
    const workspaceById = useMemo(() => {
        const map: Record<string, WorkspaceDto> = {};
        workspacesResponse?.data.forEach((membership) => {
            map[membership.workspace.workspaceId] = membership.workspace;
        });
        return map;
    }, [workspacesResponse]);

    const close = () => {
        dispatch(closeAdminAccountTeamsModal());
    };

    return (
        <Modal
            visible={visible}
            title={t("adminAccountTeamsModalTitle")}
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
                            <th className={styles.titleHeader}>{t("adminTableHeaderName")}</th>
                            <th>{t("adminTableHeaderWorkspace")}</th>
                            <th>{t("adminTableHeaderRole")}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {response.data.map((membership) => (
                            <tr key={`admin-account-team-${membership.teamMemberId}`}>
                                <td className={styles.titleCell}>{membership.team?.name}</td>
                                <td className={styles.paleCell}>
                                    {workspaceById[membership.workspaceId]?.title ?? membership.team?.workspaceUsername}
                                </td>
                                <td className={styles.roleCell}>{t(`teamMemberRole_${membership.role}`)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            {response && response.data.length == 0 && !isFetching && (
                <div className={styles.emptyLabel}>{t("adminAccountTeamsModalEmptyLabel")}</div>
            )}
            {isFetching && !response && (
                <div className={styles.loading}>
                    <CircularLoading/>
                </div>
            )}
        </Modal>
    );
};

export default AdminAccountTeamsModal;
