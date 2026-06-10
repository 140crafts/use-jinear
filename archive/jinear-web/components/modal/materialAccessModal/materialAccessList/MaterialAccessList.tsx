import React from "react";
import styles from "./MaterialAccessList.module.css";
import { useGiveAccessMutation, useListMaterialAccessQuery, useRevokeAccessMutation } from "@/api/materialAccessApi";
import { useRetrieveWorkspaceMembersQuery } from "@/api/workspaceMemberApi";
import Button, { ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import useTranslation from "@/locals/useTranslation";
import { selectMaterialAccessModalResetList } from "@/slice/modalSlice";
import { useTypedSelector } from "@/store/store";

interface MaterialAccessListProps {
  materialId: string;
  workspaceId: string;
}

const MaterialAccessList: React.FC<MaterialAccessListProps> = ({ materialId, workspaceId }) => {
  const { t } = useTranslation();
  const {
    data: listMaterialAccessResponse
  } = useListMaterialAccessQuery({ materialId });
  const {
    data: workplaceMembersResponse
  } = useRetrieveWorkspaceMembersQuery({ workspaceId });
  const resetList = useTypedSelector(selectMaterialAccessModalResetList);

  const workspaceMembers = workplaceMembersResponse?.data?.content ?? [];
  const [giveAccess, { isLoading: isGiveAccessLoading }] = useGiveAccessMutation();
  const [revokeAccess, { isLoading: isRevokeAccessLoading }] = useRevokeAccessMutation();
  const loading = isGiveAccessLoading || isRevokeAccessLoading;

  return (
    <div className={styles.container}>
      <h3>{t("materialAccessListMembersTitle")}</h3>
      <div className={styles.contentContainer}>
        {workspaceMembers.map(wm => {
            const hasAccess = listMaterialAccessResponse?.data?.content?.find(materialAccess => materialAccess.accountId == wm.accountId) != null;
            return (
              <div key={wm.workspaceMemberId} className={styles.accessListItemContainer}>
                <ProfilePhoto
                  boringAvatarKey={wm.accountId}
                  url={wm?.account.profilePicture?.url}
                  wrapperClassName={styles.profilePic}
                />
                <span className={"flex-1"}>{wm.account?.username}</span>
                <Button
                  disabled={loading}
                  variant={hasAccess ? ButtonVariants.contrast : ButtonVariants.hoverFilled2}
                  onClick={() => {
                    hasAccess ?
                      revokeAccess({ materialId, accountId: wm.accountId }) :
                      giveAccess({ materialId, accountId: wm.accountId });
                    resetList?.();
                  }}>
                  {t(hasAccess ? "materialAccessListRevoke" : "materialAccessListAdd")}
                </Button>
              </div>
            );
          }
        )}
      </div>
    </div>
  );
};

export default MaterialAccessList;