import { useUpdateProfilePictureMutation } from "@/store/api/accountMediaApi";
import { s3Base } from "@/store/api/api";
import { useLogoutMutation } from "@/store/api/authApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility, popDialogModal, resetModals } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import styles from "./PersonalInfoTab.module.css";
import UserProfilePicturePicker from "./userProfilePicturePicker/UserProfilePicturePicker";

import Button, { ButtonVariants } from "@/components/button";

interface PersonalInfoTabProps {}

const logger = Logger("PersonalInfoTab");

const PersonalInfoTab: React.FC<PersonalInfoTabProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

  const [updateProfilePicture, { isSuccess, isLoading, isError }] = useUpdateProfilePictureMutation();
  const [logoutCall, { isLoading: isLogoutLoading }] = useLogoutMutation();

  const logout = () => {
    logoutCall();
    dispatch(resetModals());
  };

  const popAreYouSureModalForLogout = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("logoutAreYouSureTitle"),
        content: t("logoutAreYouSureText"),
        confirmButtonLabel: t("logoutAreYouSureConfirmLabel"),
        onConfirm: logout,
      })
    );
  };

  useEffect(() => {
    if (selectedFile && currentAccount) {
      logger.log({ selectedFile });
      let formData = new FormData();
      if (selectedFile) {
        formData.append("file", selectedFile);
      }
      dispatch(changeLoadingModalVisibility({ visible: true }));
      updateProfilePicture({ formData });
    }
  }, [selectedFile]);

  return (
    <div className={styles.container}>
      <h2>{t("userProfilePersonalInfoTitle")}</h2>
      <div className={styles.profileContainer}>
        <UserProfilePicturePicker
          currentPhotoPath={
            currentAccount?.profilePicture?.storagePath ? s3Base + currentAccount?.profilePicture?.storagePath : undefined
          }
          selectedFile={selectedFile}
          setSelectedFile={setSelectedFile}
          selectedFilePreview={selectedFilePreview}
          setSelectedFilePreview={setSelectedFilePreview}
        />
        <div className={styles.infoContainer}>
          <h2 className={cn(styles.title, "single-line")}>{currentAccount?.username}</h2>
          <label className={cn(styles.title, "single-line")}>{currentAccount?.email}</label>
          <Button
            className={styles.logoutButton}
            loading={isLogoutLoading}
            variant={ButtonVariants.filled}
            onClick={popAreYouSureModalForLogout}
          >
            {t("sideMenuFooterLogout")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default PersonalInfoTab;
