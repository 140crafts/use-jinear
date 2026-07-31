import {useUpdateProfilePictureMutation} from "@/store/api/accountMediaApi";
import {useLogoutMutation} from "@/store/api/authApi";
import {selectCurrentAccount} from "@/store/slice/accountSlice";
import {
    changeLoadingModalVisibility,
    popDialogModal,
    popPasswordChangeModal,
    resetModals
} from "@/store/slice/modalSlice";
import {selectPendingDraftsMap} from "@/store/slice/noteDraftsSlice";
import {performLogoutCleanup, useAppDispatch, useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import styles from "./PersonalInfoTab.module.css";
import UserProfilePicturePicker from "./userProfilePicturePicker/UserProfilePicturePicker";

import Button, {ButtonVariants} from "@/components/button";

interface PersonalInfoTabProps {
}

const logger = Logger("PersonalInfoTab");

const PersonalInfoTab: React.FC<PersonalInfoTabProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const currentAccount = useTypedSelector(selectCurrentAccount);
    const pendingDrafts = useTypedSelector(selectPendingDraftsMap);
    const [selectedFile, setSelectedFile] = useState<File | undefined>();
    const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

    const [updateProfilePicture, {
        isSuccess,
        isLoading: isUpdateProfilePictureLoading,
        isError
    }] = useUpdateProfilePictureMutation();
    const [logoutCall, {isLoading: isLogoutLoading}] = useLogoutMutation();

    const logout = async () => {
        dispatch(resetModals());
        try {
            await logoutCall().unwrap();
        } catch (error) {
            logger.error({message: "Logout call failed", error});
        }
        await performLogoutCleanup(dispatch);
    };

    const popAreYouSureModalForLogout = () => {
        // Pending drafts live only in this device's IndexedDB, so logging out destroys them.
        const pendingCount = Object.keys(pendingDrafts).length;
        const warning = pendingCount == 0
            ? undefined
            : `${t("logoutAreYouSureText")}<br/><br/>${t("logoutPendingDraftsWarning").replace("${count}", `${pendingCount}`)}`;
        dispatch(
            popDialogModal({
                visible: true,
                title: t("logoutAreYouSureTitle"),
                content: t("logoutAreYouSureText"),
                htmlContent: warning,
                confirmButtonLabel: t("logoutAreYouSureConfirmLabel"),
                onConfirm: logout
            })
        );
    };

    const popChangePasswordModal = () => {
        dispatch(popPasswordChangeModal({forced: false, visible: true}));
    };

    useEffect(() => {
        if (selectedFile && currentAccount) {
            logger.log({selectedFile});
            const formData = new FormData();
            if (selectedFile) {
                formData.append("file", selectedFile);
            }
            dispatch(changeLoadingModalVisibility({visible: true}));
            updateProfilePicture({formData});
        }
    }, [selectedFile]);

    useEffect(() => {
        logger.log({isUpdateProfilePictureLoading});
        dispatch(changeLoadingModalVisibility({visible: isUpdateProfilePictureLoading}));
    }, [isUpdateProfilePictureLoading]);

    return (
        <div className={styles.container}>
            <h2>{t("userProfilePersonalInfoTitle")}</h2>
            <div className={styles.profileContainer}>
                <UserProfilePicturePicker
                    currentPhotoPath={
                        currentAccount?.profilePicture?.url ? currentAccount?.profilePicture?.url : undefined
                    }
                    selectedFile={selectedFile}
                    setSelectedFile={setSelectedFile}
                    selectedFilePreview={selectedFilePreview}
                    setSelectedFilePreview={setSelectedFilePreview}
                />
                <div className={styles.infoContainer}>
                    <h2 className={cn(styles.title, "single-line")}>{currentAccount?.username}</h2>
                    <label className={cn(styles.title, "single-line")}>{currentAccount?.email}</label>
                </div>
            </div>
            <div className={styles.actionButtonContainer}>
                <Button
                    variant={ButtonVariants.filled}
                    onClick={popChangePasswordModal}
                >
                    {t("sideMenuFooterChangePassword")}
                </Button>

                <Button
                    loading={isLogoutLoading}
                    variant={ButtonVariants.filled}
                    onClick={popAreYouSureModalForLogout}
                >
                    {t("sideMenuFooterLogout")}
                </Button>
            </div>
        </div>
    );
};

export default PersonalInfoTab;
