import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {OauthClientDto} from "@/model/be/jinear-core";
import {useAdminRevokeOauthClientMutation} from "@/store/api/adminOauthApi";
import {changeLoadingModalVisibility} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import {format} from "date-fns";
import React, {useEffect} from "react";
import styles from "./AdminMcpClientRow.module.css";

interface AdminMcpClientRowProps {
    client: OauthClientDto;
}

const AdminMcpClientRow: React.FC<AdminMcpClientRowProps> = ({client}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [revokeClient, {isLoading}] = useAdminRevokeOauthClientMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isLoading}));
    }, [isLoading]);

    const revoke = () => {
        revokeClient({clientId: client.clientId});
    };

    return (
        <tr className={styles.row}>
            <td className={styles.nameCell}>{client.clientName || client.clientId}</td>
            <td>
                <code className={styles.mono}>{client.clientId}</code>
            </td>
            <td className={styles.metaCell}>{client.registrationType}</td>
            <td className={styles.metaCell}>
                {client.clientIdIssuedAt && format(new Date(client.clientIdIssuedAt), t("dateTimeFormat"))}
            </td>
            <td className={styles.actionsCell}>
                <div className={styles.actionsContainer}>
                    <Button heightVariant={ButtonHeight.short} variant={ButtonVariants.filled}
                            disabled={isLoading} onClick={revoke}>
                        {t("adminMcpClientRevokeButton")}
                    </Button>
                </div>
            </td>
        </tr>
    );
};

export default AdminMcpClientRow;
