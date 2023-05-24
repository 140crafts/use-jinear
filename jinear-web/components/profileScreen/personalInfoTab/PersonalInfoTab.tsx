import { useUpdateProfilePictureMutation } from "@/store/api/accountMediaApi";
import { s3Base } from "@/store/api/api";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import styles from "./PersonalInfoTab.module.css";
import UserProfilePicturePicker from "./userProfilePicturePicker/UserProfilePicturePicker";

interface PersonalInfoTabProps {}

const logger = Logger("PersonalInfoTab");

const PersonalInfoTab: React.FC<PersonalInfoTabProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

  const [updateProfilePicture, { isSuccess, isLoading, isError }] = useUpdateProfilePictureMutation();

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
          <h2 className={cn(styles.title, "line-clamp-2")}>{currentAccount?.username}</h2>
          <label className={cn(styles.title, "line-clamp-2")}>{currentAccount?.email}</label>
          <h3>
            {/* <Link target="_blank" href={`${HOST}/${selectedWorkspace?.username}`}> */}
            {/* {`${HOST?.replace("https://", "")?.replace("http://", "")}/${selectedWorkspace?.username}`} */}
            {/* </Link> */}
          </h3>
        </div>
      </div>
    </div>
  );
};

export default PersonalInfoTab;
