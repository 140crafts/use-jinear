import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {TeamDto} from "@/model/be/jinear-core";
import {useAdminDeleteTeamMutation, useAdminRenameTeamMutation} from "@/store/api/adminTeamApi";
import {
    changeLoadingModalVisibility,
    closeBasicTextInputModal,
    closeDialogModal,
    popAdminTeamMembersModal,
    popBasicTextInputModal,
    popDialogModal
} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import styles from "./AdminTeamRow.module.css";

interface AdminTeamRowProps {
    team: TeamDto;
}

const AdminTeamRow: React.FC<AdminTeamRowProps> = ({team}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [adminRenameTeam, {isLoading: isRenameLoading}] = useAdminRenameTeamMutation();
    const [adminDeleteTeam, {isLoading: isDeleteLoading}] = useAdminDeleteTeamMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isRenameLoading || isDeleteLoading}));
    }, [isRenameLoading, isDeleteLoading]);

    const onRenameSubmit = (name: string) => {
        dispatch(closeBasicTextInputModal());
        adminRenameTeam({teamId: team.teamId, body: {name}});
    };

    const popRenameModal = () => {
        dispatch(
            popBasicTextInputModal({
                visible: true,
                title: t("adminTeamRenameModalTitle"),
                infoText: t("adminTeamRenameModalInfoText"),
                initialText: team.name,
                onSubmit: onRenameSubmit
            })
        );
    };

    const popMembersModal = () => {
        dispatch(popAdminTeamMembersModal({visible: true, team}));
    };

    const popDeleteDialog = () => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("adminTeamDeleteDialogTitle"),
                content: t("adminTeamDeleteDialogContent"),
                confirmButtonLabel: t("adminTeamDeleteDialogConfirmLabel"),
                onConfirm: () => {
                    adminDeleteTeam({teamId: team.teamId});
                    dispatch(closeDialogModal());
                }
            })
        );
    };

    return (
        <tr className={styles.row}>
            <td className={styles.titleCell}>{team.name}</td>
            <td>
                <span className={styles.tag}>{team.tag}</span>
            </td>
            <td className={styles.actionsCell}>
                <div className={styles.actionsContainer}>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} onClick={popRenameModal}>
                        {t("adminTeamRowRename")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled}
                            onClick={popMembersModal}>
                        {t("adminTeamRowMembers")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled} onClick={popDeleteDialog}>
                        {t("adminTeamRowDelete")}
                    </Button>
                </div>
            </td>
        </tr>
    );
};

export default AdminTeamRow;
