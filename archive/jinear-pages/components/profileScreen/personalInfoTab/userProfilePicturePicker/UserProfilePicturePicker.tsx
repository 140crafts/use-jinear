import Button, { ButtonVariants } from "@/components/button";
import Logger from "@/utils/logger";
import React, { ChangeEvent, useEffect, useRef } from "react";
import { FaPen } from "react-icons/fa";
import { IoCamera } from "react-icons/io5";
import styles from "./UserProfilePicturePicker.module.css";

interface UserProfilePicturePickerProps {
  currentPhotoPath?: string;
  selectedFile: File | undefined;
  setSelectedFile: React.Dispatch<React.SetStateAction<File | undefined>>;
  selectedFilePreview: string | undefined;
  setSelectedFilePreview: React.Dispatch<React.SetStateAction<string | undefined>>;
}

const logger = Logger("UserProfilePicturePicker");

const UserProfilePicturePicker: React.FC<UserProfilePicturePickerProps> = ({
  currentPhotoPath,
  selectedFile,
  setSelectedFile,
  selectedFilePreview,
  setSelectedFilePreview,
}) => {
  const photoPickerButtonRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (!selectedFile) {
      setSelectedFilePreview(undefined);
      return;
    }
    const objectUrl = URL.createObjectURL(selectedFile);
    setSelectedFilePreview(objectUrl);
    return () => URL.revokeObjectURL(objectUrl);
  }, [selectedFile]);

  const pickPhoto = () => {
    logger.log({ selectedFilePreview });
    setSelectedFile(undefined);
    if (photoPickerButtonRef.current) {
      photoPickerButtonRef.current.value = "";
      photoPickerButtonRef.current.click();
    }
  };

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      const file = event.target?.files?.[0];
      setSelectedFile(file);
      return;
    }
    setSelectedFile(undefined);
  };

  return (
    <>
      {selectedFile || currentPhotoPath ? (
        <div className={styles.photoContainer}>
          <img
            src={selectedFilePreview ? selectedFilePreview : currentPhotoPath}
            className={styles.profilePicture}
            onClick={pickPhoto}
          />
          <Button className={styles.changeLabel} variant={ButtonVariants.filled2} onClick={pickPhoto}>
            <FaPen />
          </Button>
        </div>
      ) : (
        <Button className={styles.profilePicture} onClick={pickPhoto}>
          <IoCamera size={32} />
        </Button>
      )}
      <input
        ref={photoPickerButtonRef}
        id={"photo-picker"}
        type="file"
        accept="image/*"
        className={styles.photoInput}
        onChange={onSelectFile}
      />
    </>
  );
};

export default UserProfilePicturePicker;
