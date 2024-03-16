import Button from "@/components/button";
import { useLazyCheckEligibilityQuery, useSendAccountDeleteEmailMutation } from "@/store/api/accountDeleteApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import toast from "react-hot-toast";
import styles from "./AccountDeleteButton.module.css";

interface AccountDeleteButtonProps {}

const AccountDeleteButton: React.FC<AccountDeleteButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [checkEligibility, { isFetching: isCheckEligibilityFetching }] = useLazyCheckEligibilityQuery();
  const [sendAccountDeleteEmail, { isLoading: isSendAccountDeleteEmailFetching, isSuccess: isSendAccountDeleteEmailSuccess }] =
    useSendAccountDeleteEmailMutation();

  useEffect(() => {
    if (isSendAccountDeleteEmailSuccess) {
      toast(t("accountDeletionMailSendSuccessfully"));
    }
  }, [isSendAccountDeleteEmailSuccess]);

  const popAreYouSureModalForDeleteAccount = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("accountDeleteAreYouSureTitle"),
        content: t("accountDeleteAreYouSureText"),
        confirmButtonLabel: t("accountDeleteAreYouSureConfirmLabel"),
        onConfirm: sendEmail,
      })
    );
  };

  const sendEmail = () => {
    sendAccountDeleteEmail();
    dispatch(closeDialogModal());
  };

  const onDeleteClick = async () => {
    const eligibility = await checkEligibility();
    if (eligibility.data?.data.eligible) {
      popAreYouSureModalForDeleteAccount();
    }
  };

  return (
    <div className={styles.container}>
      <Button
        loading={isCheckEligibilityFetching || isSendAccountDeleteEmailFetching}
        disabled={isCheckEligibilityFetching || isSendAccountDeleteEmailFetching}
        onClick={onDeleteClick}
      >
        {t("accountDeleteButtonLabel")}
      </Button>
    </div>
  );
};

export default AccountDeleteButton;
