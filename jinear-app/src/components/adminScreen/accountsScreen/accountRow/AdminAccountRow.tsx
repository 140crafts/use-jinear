import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {AccountDto} from "@/model/be/jinear-core";
import {useAdminUpdateAccountPasswordMutation} from "@/store/api/adminAccountApi";
import {
    changeLoadingModalVisibility,
    closeBasicTextInputModal,
    popAdminAccountTeamsModal,
    popAdminAccountWorkspacesModal,
    popBasicTextInputModal
} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import {format} from "date-fns";
import React, {useEffect} from "react";
import toast from "react-hot-toast";
import styles from "./AdminAccountRow.module.css";

interface AdminAccountRowProps {
    account: AccountDto;
}

const AdminAccountRow: React.FC<AdminAccountRowProps> = ({account}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [adminUpdateAccountPassword, {isLoading, isSuccess}] = useAdminUpdateAccountPasswordMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isLoading}));
    }, [isLoading]);

    useEffect(() => {
        if (isSuccess) {
            toast(t("adminAccountPasswordUpdatedToast"));
        }
    }, [isSuccess]);

    const onPasswordSubmit = (newPassword: string) => {
        dispatch(closeBasicTextInputModal());
        adminUpdateAccountPassword({accountId: account.accountId, body: {newPassword}});
    };

    const popPasswordModal = () => {
        dispatch(
            popBasicTextInputModal({
                visible: true,
                title: t("adminAccountPasswordModalTitle"),
                infoText: t("adminAccountPasswordModalInfoText"),
                inputType: "password",
                onSubmit: onPasswordSubmit
            })
        );
    };

    const popWorkspacesModal = () => {
        dispatch(popAdminAccountWorkspacesModal({visible: true, account}));
    };

    const popTeamsModal = () => {
        dispatch(popAdminAccountTeamsModal({visible: true, account}));
    };

    return (
        <tr className={styles.row}>
            <td className={styles.emailCell}>{account.email}</td>
            <td className={styles.dateCell}>{format(new Date(account.createdDate), t("dateTimeFormat"))}</td>
            <td className={styles.actionsCell}>
                <div className={styles.actionsContainer}>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled}
                            onClick={popPasswordModal}>
                        {t("adminAccountRowUpdatePassword")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled}
                            onClick={popWorkspacesModal}>
                        {t("adminAccountRowWorkspaces")}
                    </Button>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled}
                            onClick={popTeamsModal}>
                        {t("adminAccountRowTeams")}
                    </Button>
                </div>
            </td>
        </tr>
    );
};

export default AdminAccountRow;
