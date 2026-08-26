import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Line from "@/components/line/Line";
import Modal from "@/components/modal/modal/Modal";
import Pagination from "@/components/pagination/Pagination";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import type {AccountDto, WorkspaceAccountRoleType, WorkspaceMemberDto} from "@/model/be/jinear-core";
import {
    useAdminAddWorkspaceMemberMutation,
    useAdminRemoveWorkspaceMemberMutation,
    useAdminRetrieveWorkspaceMembersQuery
} from "@/store/api/adminWorkspaceApi";
import {
    changeLoadingModalVisibility,
    closeAdminWorkspaceMembersModal,
    closeDialogModal,
    popAdminAccountPickerModal,
    popDialogModal,
    selectAdminWorkspaceMembersModalVisible,
    selectAdminWorkspaceMembersModalWorkspace
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import toast from "react-hot-toast";
import styles from "./AdminWorkspaceMembersModal.module.css";

interface AdminWorkspaceMembersModalProps {
}

const AdminWorkspaceMembersModal: React.FC<AdminWorkspaceMembersModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);
    const [pickedAccount, setPickedAccount] = useState<AccountDto | undefined>();
    const [role, setRole] = useState<WorkspaceAccountRoleType>("MEMBER");

    const visible = useTypedSelector(selectAdminWorkspaceMembersModalVisible);
    const workspace = useTypedSelector(selectAdminWorkspaceMembersModalWorkspace);
    const workspaceId = workspace?.workspaceId;

    const {data: response, isLoading, isFetching} = useAdminRetrieveWorkspaceMembersQuery(
        {workspaceId: workspaceId ?? "", page},
        {skip: !visible || !workspaceId}
    );
    const [adminAddWorkspaceMember, {isLoading: isAddLoading, isSuccess: isAddSuccess}] = useAdminAddWorkspaceMemberMutation();
    const [adminRemoveWorkspaceMember, {isLoading: isRemoveLoading, isSuccess: isRemoveSuccess}] = useAdminRemoveWorkspaceMemberMutation();

    useEffect(() => {
        setPage(0);
        setPickedAccount(undefined);
    }, [workspaceId]);

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isAddLoading || isRemoveLoading}));
    }, [isAddLoading, isRemoveLoading]);

    useEffect(() => {
        if (isAddSuccess) {
            toast(t("adminMemberAddedToast"));
            setPickedAccount(undefined);
        }
    }, [isAddSuccess]);

    useEffect(() => {
        if (isRemoveSuccess) {
            toast(t("adminMemberRemovedToast"));
        }
    }, [isRemoveSuccess]);

    const close = () => {
        dispatch(closeAdminWorkspaceMembersModal());
    };

    const popAccountPicker = () => {
        dispatch(popAdminAccountPickerModal({visible: true, onPick: setPickedAccount}));
    };

    const popRemoveDialog = (member: WorkspaceMemberDto) => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("adminMemberRemoveDialogTitle"),
                content: t("adminMemberRemoveDialogContent"),
                confirmButtonLabel: t("adminMemberRemoveDialogConfirmLabel"),
                onConfirm: () => {
                    adminRemoveWorkspaceMember({
                        workspaceId: member.workspaceId,
                        workspaceMemberId: member.workspaceMemberId
                    });
                    dispatch(closeDialogModal());
                }
            })
        );
    };

    const onAddClick = () => {
        if (workspaceId && pickedAccount) {
            adminAddWorkspaceMember({workspaceId, body: {accountId: pickedAccount.accountId, role}});
        }
    };

    return (
        <Modal
            visible={visible}
            title={t("adminWorkspaceMembersModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <div className={styles.workspaceTitle}>{workspace?.title}</div>

            <div className={styles.header}>
                {response && (
                    <Pagination
                        id={"admin-workspace-members-paginator"}
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
                {response?.data.hasContent && (
                    <div className={styles.tableWrapper}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th className={styles.emailHeader}>{t("adminTableHeaderEmail")}</th>
                                <th>{t("adminTableHeaderRole")}</th>
                                <th className={styles.actionsHeader}>{t("adminTableHeaderActions")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {response.data.content.map((member) => (
                                <tr key={`admin-workspace-member-${member.workspaceMemberId}`}>
                                    <td className={styles.emailCell}>{member.account.email}</td>
                                    <td className={styles.roleCell}>{t(`workspaceMemberRole_${member.role}`)}</td>
                                    <td className={styles.actionsCell}>
                                        <Button
                                            heightVariant={ButtonHeight.short}
                                            variant={ButtonVariants.filled}
                                            onClick={() => popRemoveDialog(member)}
                                        >
                                            {t("adminMembersModalRemoveButtonLabel")}
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
                {!response?.data.hasContent && !isFetching && (
                    <div className={styles.emptyLabel}>{t("adminMembersModalEmptyLabel")}</div>
                )}
                {isFetching && !response && (
                    <div className={styles.loading}>
                        <CircularLoading/>
                    </div>
                )}
            </div>

            <Line/>

            <div className={styles.addSection}>
                <div className={styles.addSectionTitle}>{t("adminMembersModalAddSectionTitle")}</div>
                <Button
                    variant={ButtonVariants.filled}
                    onClick={popAccountPicker}
                >
                    {pickedAccount ? pickedAccount.email : t("adminMembersModalPickAccountLabel")}
                </Button>
                <SegmentedControl
                    id={"admin-workspace-member-role"}
                    name={"admin-workspace-member-role"}
                    defaultIndex={2}
                    segments={[
                        {label: t("workspaceMemberRole_OWNER"), value: "OWNER"},
                        {label: t("workspaceMemberRole_ADMIN"), value: "ADMIN"},
                        {label: t("workspaceMemberRole_MEMBER"), value: "MEMBER"},
                        {label: t("workspaceMemberRole_GUEST"), value: "GUEST"}
                    ]}
                    callback={(value) => setRole(value as WorkspaceAccountRoleType)}
                />
                <Button
                    variant={ButtonVariants.contrast}
                    disabled={!pickedAccount || isAddLoading}
                    loading={isAddLoading}
                    onClick={onAddClick}
                >
                    {t("adminMembersModalAddButtonLabel")}
                </Button>
            </div>
        </Modal>
    );
};

export default AdminWorkspaceMembersModal;
