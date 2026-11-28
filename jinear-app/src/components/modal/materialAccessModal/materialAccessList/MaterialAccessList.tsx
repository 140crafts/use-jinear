import React from "react";
import styles from "./MaterialAccessList.module.css";
import {useGiveAccessMutation, useListMaterialAccessQuery, useRevokeAccessMutation} from "@/api/materialAccessApi";
import {useRetrieveWorkspaceMembersQuery} from "@/api/workspaceMemberApi";
import Button, {ButtonVariants} from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import useTranslation from "@/locals/useTranslation";
import {selectMaterialAccessModalResetList} from "@/slice/modalSlice";
import {useTypedSelector} from "@/store";
import type {WorkspaceMemberDto} from "@/be/jinear-core";

interface MaterialAccessListProps {
    materialId: string;
    workspaceId: string;
    ownerId: string;
}

const MaterialAccessList: React.FC<MaterialAccessListProps> = ({materialId, workspaceId, ownerId}) => {
    const {t} = useTranslation();
    const {
        data: listMaterialAccessResponse
    } = useListMaterialAccessQuery({materialId});
    const {
        data: workplaceMembersResponse
    } = useRetrieveWorkspaceMembersQuery({workspaceId});
    const resetList = useTypedSelector(selectMaterialAccessModalResetList);

    const workspaceMembers = workplaceMembersResponse?.data?.content ?? [];
    const owner = workspaceMembers.filter(wm => wm.accountId == ownerId);
    const otherMembers = workspaceMembers.filter(wm => wm.accountId != ownerId);
    const ownerFirstMembers = [...owner, ...otherMembers];
    const [giveAccess, {isLoading: isGiveAccessLoading, originalArgs: giveAccessArgs}] = useGiveAccessMutation();
    const [revokeAccess, {isLoading: isRevokeAccessLoading, originalArgs: revokeAccessArgs}] = useRevokeAccessMutation();
    const loading = isGiveAccessLoading || isRevokeAccessLoading;
    const savingAccountId = isGiveAccessLoading ? giveAccessArgs?.accountId : revokeAccessArgs?.accountId;

    const hasAccess = (accountId: string) =>
        listMaterialAccessResponse?.data?.content?.some(materialAccess => materialAccess.accountId == accountId) ?? false;

    const toggleAccess = (accountId: string) => {
        if (hasAccess(accountId)) {
            revokeAccess({materialId, accountId});
        } else {
            giveAccess({materialId, accountId});
        }
        resetList?.();
    };

    const renderAccessAction = (wm: WorkspaceMemberDto) => {
        if (wm.accountId == ownerId) {
            return <span className={styles.ownerLabel}>{t("materialAccessListOwner")}</span>;
        }
        const memberHasAccess = hasAccess(wm.accountId);
        return (
            <Button
                disabled={loading}
                loading={loading && savingAccountId == wm.accountId}
                variant={memberHasAccess ? ButtonVariants.contrast : ButtonVariants.hoverFilled2}
                onClick={() => toggleAccess(wm.accountId)}
            >
                {t(memberHasAccess ? "materialAccessListRevoke" : "materialAccessListAdd")}
            </Button>
        );
    };

    return (
        <div className={styles.container}>
            {ownerFirstMembers.map(wm => (
                <div key={wm.workspaceMemberId} className={styles.memberRow}>
                    <ProfilePhoto
                        boringAvatarKey={wm.accountId}
                        url={wm?.account.profilePicture?.url}
                        wrapperClassName={styles.profilePic}
                    />
                    <span className={"flex-1 single-line"}>{wm.account?.username}</span>
                    {renderAccessAction(wm)}
                </div>
            ))}
        </div>
    );
};

export default MaterialAccessList;
