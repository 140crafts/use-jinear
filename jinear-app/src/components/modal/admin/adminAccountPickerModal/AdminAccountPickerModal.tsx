import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Modal from "@/components/modal/modal/Modal";
import Pagination from "@/components/pagination/Pagination";
import type {AccountDto} from "@/model/be/jinear-core";
import {useAdminRetrieveAccountsQuery} from "@/store/api/adminAccountApi";
import {useAdminRetrieveWorkspaceMembersQuery} from "@/store/api/adminWorkspaceApi";
import {
    closeAdminAccountPickerModal,
    selectAdminAccountPickerModalOnPick,
    selectAdminAccountPickerModalVisible,
    selectAdminAccountPickerModalWorkspaceId
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useMemo, useState} from "react";
import styles from "./AdminAccountPickerModal.module.css";

interface AdminAccountPickerModalProps {
}

const AdminAccountPickerModal: React.FC<AdminAccountPickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);

    const visible = useTypedSelector(selectAdminAccountPickerModalVisible);
    const workspaceId = useTypedSelector(selectAdminAccountPickerModalWorkspaceId);
    const onPick = useTypedSelector(selectAdminAccountPickerModalOnPick);

    const {
        data: accountsResponse,
        isLoading: isAccountsLoading,
        isFetching: isAccountsFetching
    } = useAdminRetrieveAccountsQuery({page}, {skip: !visible || workspaceId != null});
    const {
        data: membersResponse,
        isLoading: isMembersLoading,
        isFetching: isMembersFetching
    } = useAdminRetrieveWorkspaceMembersQuery(
        {workspaceId: workspaceId ?? "", page},
        {skip: !visible || workspaceId == null}
    );

    const pageData = workspaceId != null ? membersResponse?.data : accountsResponse?.data;
    const isLoading = workspaceId != null ? isMembersLoading : isAccountsLoading;
    const isFetching = workspaceId != null ? isMembersFetching : isAccountsFetching;

    const accounts: AccountDto[] = useMemo(() => {
        if (workspaceId != null) {
            return membersResponse?.data.content.map((member) => member.account) ?? [];
        }
        return accountsResponse?.data.content ?? [];
    }, [workspaceId, membersResponse, accountsResponse]);

    useEffect(() => {
        if (visible) {
            setPage(0);
        }
    }, [visible, workspaceId]);

    const close = () => {
        dispatch(closeAdminAccountPickerModal());
    };

    const pick = (account: AccountDto) => {
        onPick?.(account);
        close();
    };

    return (
        <Modal
            visible={visible}
            title={t("adminAccountPickerModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <div className={styles.header}>
                {pageData && (
                    <Pagination
                        id={"admin-account-picker-paginator"}
                        className={styles.pagination}
                        pageNumber={pageData.number}
                        pageSize={pageData.size}
                        totalPages={pageData.totalPages}
                        totalElements={pageData.totalElements}
                        hasPrevious={pageData.hasPrevious}
                        hasNext={pageData.hasNext}
                        isLoading={isLoading || isFetching}
                        page={page}
                        setPage={setPage}
                    />
                )}
            </div>
            <div className={styles.list}>
                {accounts.map((account) => (
                    <Button
                        key={`admin-account-picker-${account.accountId}`}
                        className={styles.row}
                        variant={ButtonVariants.hoverFilled2}
                        onClick={() => pick(account)}
                    >
                        {account.email}
                    </Button>
                ))}
                {!pageData?.hasContent && !isFetching && (
                    <div className={styles.emptyLabel}>{t("adminAccountsEmptyLabel")}</div>
                )}
            </div>
            {isFetching && !pageData && (
                <div className={styles.loading}>
                    <CircularLoading/>
                </div>
            )}
        </Modal>
    );
};

export default AdminAccountPickerModal;
