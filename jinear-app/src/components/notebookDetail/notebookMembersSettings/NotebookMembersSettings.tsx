import Button, {ButtonVariants} from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import type {NotebookDto} from "@/model/be/jinear-core";
import {
    useAddNotebookMemberMutation,
    useGetNotebookMembersQuery,
    useRemoveNotebookMemberMutation
} from "@/store/api/notebookMemberApi";
import {useRetrieveWorkspaceMembersQuery} from "@/store/api/workspaceMemberApi";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./NotebookMembersSettings.module.css";

interface NotebookMembersSettingsProps {
    notebook: NotebookDto;
    accountHasEditRole: boolean;
}

interface MemberAccount {
    accountId: string;
    username?: string | null;
    profilePictureUrl?: string;
}

const NotebookMembersSettings: React.FC<NotebookMembersSettingsProps> = ({notebook, accountHasEditRole}) => {
    const {t} = useTranslation();

    const {data: notebookMembersResponse} = useGetNotebookMembersQuery({notebookId: notebook.notebookId});
    const {data: workspaceMembersResponse} = useRetrieveWorkspaceMembersQuery(
        {workspaceId: notebook.workspaceId},
        {skip: !accountHasEditRole}
    );

    const [addNotebookMember, {isLoading: isAdding}] = useAddNotebookMemberMutation();
    const [removeNotebookMember, {isLoading: isRemoving}] = useRemoveNotebookMemberMutation();
    const loading = isAdding || isRemoving;

    const notebookMembers = notebookMembersResponse?.data?.content ?? [];

    // Editors manage the full workspace roster; others see only the members who already have access.
    const rows: MemberAccount[] = accountHasEditRole
        ? (workspaceMembersResponse?.data?.content ?? []).map((wm) => ({
            accountId: wm.accountId,
            username: wm.account?.username,
            profilePictureUrl: wm.account?.profilePicture?.url
        }))
        : notebookMembers
            .filter((nm) => nm.account != null)
            .map((nm) => ({
                accountId: nm.accountId,
                username: nm?.account?.username,
                profilePictureUrl: nm?.account?.profilePicture?.url
            }));

    return (
        <div className={styles.container}>
            <SectionTitle
                title={t("notebookSettingsMembersSectionTitle")}
                description={t("notebookSettingsMembersSectionDescription")}
            />

            <div className={styles.contentContainer}>
                {rows.map((row) => {
                    const isOwner = row.accountId === notebook.ownerId;
                    const hasAccess = isOwner || notebookMembers.some((nm) => nm.accountId === row.accountId);

                    return (
                        <div key={row.accountId} className={styles.memberItemContainer}>
                            <ProfilePhoto
                                boringAvatarKey={row.accountId}
                                url={row.profilePictureUrl}
                                wrapperClassName={styles.profilePic}
                            />
                            <span className={"flex-1"}>{row.username}</span>

                            {isOwner ? (
                                <span className={styles.ownerLabel}>{t("notebookSettingsMemberOwnerLabel")}</span>
                            ) : accountHasEditRole ? (
                                <Button
                                    disabled={loading}
                                    variant={hasAccess ? ButtonVariants.contrast : ButtonVariants.hoverFilled2}
                                    onClick={() =>
                                        hasAccess
                                            ? removeNotebookMember({notebookId: notebook.notebookId, accountId: row.accountId})
                                            : addNotebookMember({notebookId: notebook.notebookId, accountId: row.accountId})
                                    }
                                >
                                    {t(hasAccess ? "notebookSettingsMemberRemoveButton" : "notebookSettingsAddMemberButton")}
                                </Button>
                            ) : null}
                        </div>
                    );
                })}

                {rows.length === 0 && (
                    <div className={styles.emptyStateContainer}>{t("notebookSettingsMembersEmpty")}</div>
                )}
            </div>
        </div>
    );
};

export default NotebookMembersSettings;
