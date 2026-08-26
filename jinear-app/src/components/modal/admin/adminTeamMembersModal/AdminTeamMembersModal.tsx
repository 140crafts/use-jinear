import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Line from "@/components/line/Line";
import Modal from "@/components/modal/modal/Modal";
import Pagination from "@/components/pagination/Pagination";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import type {AccountDto, TeamMemberDto, TeamMemberRoleType} from "@/model/be/jinear-core";
import {
    useAdminAddTeamMemberMutation,
    useAdminRemoveTeamMemberMutation,
    useAdminRetrieveTeamMembersQuery
} from "@/store/api/adminTeamApi";
import {
    changeLoadingModalVisibility,
    closeAdminTeamMembersModal,
    closeDialogModal,
    popAdminAccountPickerModal,
    popDialogModal,
    selectAdminTeamMembersModalTeam,
    selectAdminTeamMembersModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import toast from "react-hot-toast";
import styles from "./AdminTeamMembersModal.module.css";

interface AdminTeamMembersModalProps {
}

const AdminTeamMembersModal: React.FC<AdminTeamMembersModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);
    const [pickedAccount, setPickedAccount] = useState<AccountDto | undefined>();
    const [role, setRole] = useState<TeamMemberRoleType>("MEMBER");

    const visible = useTypedSelector(selectAdminTeamMembersModalVisible);
    const team = useTypedSelector(selectAdminTeamMembersModalTeam);
    const teamId = team?.teamId;

    const {data: response, isLoading, isFetching} = useAdminRetrieveTeamMembersQuery(
        {teamId: teamId ?? "", page},
        {skip: !visible || !teamId}
    );
    const [adminAddTeamMember, {isLoading: isAddLoading, isSuccess: isAddSuccess}] = useAdminAddTeamMemberMutation();
    const [adminRemoveTeamMember, {isLoading: isRemoveLoading, isSuccess: isRemoveSuccess}] = useAdminRemoveTeamMemberMutation();

    useEffect(() => {
        setPage(0);
        setPickedAccount(undefined);
    }, [teamId]);

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
        dispatch(closeAdminTeamMembersModal());
    };

    const popAccountPicker = () => {
        dispatch(popAdminAccountPickerModal({
            visible: true,
            workspaceId: team?.workspaceId,
            onPick: setPickedAccount
        }));
    };

    const popRemoveDialog = (member: TeamMemberDto) => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("adminMemberRemoveDialogTitle"),
                content: t("adminMemberRemoveDialogContent"),
                confirmButtonLabel: t("adminMemberRemoveDialogConfirmLabel"),
                onConfirm: () => {
                    adminRemoveTeamMember({teamMemberId: member.teamMemberId});
                    dispatch(closeDialogModal());
                }
            })
        );
    };

    const onAddClick = () => {
        if (teamId && pickedAccount) {
            adminAddTeamMember({teamId, body: {accountId: pickedAccount.accountId, role}});
        }
    };

    return (
        <Modal
            visible={visible}
            title={t("adminTeamMembersModalTitle")}
            hasTitleCloseButton={true}
            requestClose={close}
            bodyClass={styles.body}
        >
            <div className={styles.teamTitle}>{team?.name}</div>

            <div className={styles.header}>
                {response && (
                    <Pagination
                        id={"admin-team-members-paginator"}
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
                                <tr key={`admin-team-member-${member.teamMemberId}`}>
                                    <td className={styles.emailCell}>{member.account.email}</td>
                                    <td className={styles.roleCell}>{t(`teamMemberRole_${member.role}`)}</td>
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
                    id={"admin-team-member-role"}
                    name={"admin-team-member-role"}
                    defaultIndex={1}
                    segments={[
                        {label: t("teamMemberRole_ADMIN"), value: "ADMIN"},
                        {label: t("teamMemberRole_MEMBER"), value: "MEMBER"},
                        {label: t("teamMemberRole_GUEST"), value: "GUEST"}
                    ]}
                    callback={(value) => setRole(value as TeamMemberRoleType)}
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

export default AdminTeamMembersModal;
