import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {useAdminDeleteWorkspaceMutation, useAdminUpdateWorkspaceTitleMutation} from "@/store/api/adminWorkspaceApi";
import {
    changeLoadingModalVisibility,
    closeBasicTextInputModal,
    closeDialogModal,
    popAdminWorkspaceMembersModal,
    popAdminWorkspaceTierModal,
    popBasicTextInputModal,
    popDialogModal
} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import styles from "./AdminWorkspaceRow.module.css";

interface AdminWorkspaceRowProps {
    workspace: WorkspaceDto;
}

const AdminWorkspaceRow: React.FC<AdminWorkspaceRowProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [adminUpdateWorkspaceTitle, {isLoading: isRenameLoading}] = useAdminUpdateWorkspaceTitleMutation();
    const [adminDeleteWorkspace, {isLoading: isDeleteLoading}] = useAdminDeleteWorkspaceMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isRenameLoading || isDeleteLoading}));
    }, [isRenameLoading, isDeleteLoading]);

    const onRenameSubmit = (title: string) => {
        dispatch(closeBasicTextInputModal());
        adminUpdateWorkspaceTitle({workspaceId: workspace.workspaceId, body: {title}});
    };

    const popRenameModal = () => {
        dispatch(
            popBasicTextInputModal({
                visible: true,
                title: t("adminWorkspaceRenameModalTitle"),
                infoText: t("adminWorkspaceRenameModalInfoText"),
                initialText: workspace.title,
                onSubmit: onRenameSubmit
            })
        );
    };

    const popTierModal = () => {
        dispatch(popAdminWorkspaceTierModal({visible: true, workspace}));
    };

    const popMembersModal = () => {
        dispatch(popAdminWorkspaceMembersModal({visible: true, workspace}));
    };

    const popDeleteDialog = () => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("adminWorkspaceDeleteDialogTitle"),
                content: t("adminWorkspaceDeleteDialogContent"),
                confirmButtonLabel: t("adminWorkspaceDeleteDialogConfirmLabel"),
                onConfirm: () => {
                    adminDeleteWorkspace({workspaceId: workspace.workspaceId});
                    dispatch(closeDialogModal());
                }
            })
        );
    };

    return (
        <tr className={styles.row}>
            <td className={styles.titleCell}>{workspace.title}</td>
            <td className={styles.handleCell}>{workspace.username}</td>
            <td>
                <span className={styles.tier}>{t(`adminWorkspaceTier_${workspace.tier}`)}</span>
            </td>
            <td className={styles.actionsCell}>
                <div className={styles.actionsContainer}>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} onClick={popRenameModal}>
                        {t("adminWorkspaceRowRename")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} onClick={popTierModal}>
                        {t("adminWorkspaceRowTier")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} href={"/admin/teams"}>
                        {t("adminWorkspaceRowTeams")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} onClick={popMembersModal}>
                        {t("adminWorkspaceRowMembers")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} onClick={popDeleteDialog}>
                        {t("adminWorkspaceRowDelete")}
                    </Button>
                </div>
            </td>
        </tr>
    );
};

export default AdminWorkspaceRow;
