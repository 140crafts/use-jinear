import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { PageDto, WorkspaceAccountRoleType, WorkspaceMemberDto } from "@/model/be/jinear-core";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { popWorkspaceMemberInviteModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd } from "react-icons/io5";
import MemberProfilePictureList from "../memberProfilePictureList/MemberProfilePictureList";
import styles from "./WorkspaceMemberList.module.css";

interface WorkspaceMemberListProps {
  page: PageDto<WorkspaceMemberDto>;
}

const WorkspaceMemberList: React.FC<WorkspaceMemberListProps> = ({ page }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const currentAccountsWorkspaceRole: WorkspaceAccountRoleType[] = page?.content
    ?.filter((memberShip) => memberShip.accountId == currentAccountId)
    .map((memberShip) => memberShip?.role);
  const currentAccountAdminOrOwner =
    currentAccountsWorkspaceRole.indexOf("ADMIN") != -1 || currentAccountsWorkspaceRole.indexOf("OWNER") != -1;

  const popInvitationModal = () => {
    dispatch(popWorkspaceMemberInviteModal());
  };

  return (
    <div className={styles.container}>
      <MemberProfilePictureList accountList={page.content.map((member) => member.account)} type="workspace" />
      {currentAccountAdminOrOwner && (
        <Button
          onClick={popInvitationModal}
          variant={ButtonVariants.hoverFilled2}
          heightVariant={ButtonHeight.short}
          data-tooltip-right={t("sideMenuWorkspaceInviteMember")}
        >
          <IoAdd />
        </Button>
      )}
    </div>
  );
};

export default WorkspaceMemberList;
