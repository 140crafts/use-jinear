import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { WorkspaceInvitationDto } from "@/model/be/jinear-core";
import { useDeleteInvitationMutation } from "@/store/api/workspaceMemberInvitationApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoClose } from "react-icons/io5";
import styles from "./InvitationRow.module.scss";

interface InvitationRowProps {
  invitation: WorkspaceInvitationDto;
  workspaceRoleIsAdminOrOwner: boolean;
}

const logger = Logger("InvitationRow");

const InvitationRow: React.FC<InvitationRowProps> = ({ invitation, workspaceRoleIsAdminOrOwner }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [deleteInvitation, {}] = useDeleteInvitationMutation();

  const deleteWorkspaceInvitation = () => {
    logger.log({ deleteWorkspaceInvitation: invitation.workspaceInvitationId });
    deleteInvitation({ invitationId: invitation.workspaceInvitationId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDeleteInvitation = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteWorkspaceInvitationAreYouSureTitle"),
        content: t("deleteWorkspaceInvitationAreYouSureText"),
        confirmButtonLabel: t("deleteWorkspaceInvitationAreYouSureConfirmLabel"),
        onConfirm: deleteWorkspaceInvitation,
      })
    );
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.title}>{invitation.email}</div>
          <div className={styles.date}>{`(${format(new Date(invitation.createdDate), t("dateTimeFormat"))})`}</div>
        </div>

        {workspaceRoleIsAdminOrOwner && (
          <div className={styles.rightInfoContainer}>
            <Button
              variant={ButtonVariants.filled}
              className={styles.button}
              onClick={popAreYouSureModalForDeleteInvitation}
              data-tooltip-right={t("activeInvitationCancel")}
            >
              <div className={styles.iconContainer}>
                <IoClose size={14} />
              </div>
            </Button>
          </div>
        )}
      </div>
      <Line className={styles.line} />
    </>
  );
};

export default InvitationRow;
