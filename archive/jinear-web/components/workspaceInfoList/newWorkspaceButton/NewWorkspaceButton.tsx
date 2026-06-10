import Button from "@/components/button";
import { popNewWorkspaceModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuPlus } from "react-icons/lu";
import styles from "./NewWorkspaceButton.module.css";

interface NewWorkspaceButtonProps {}

const NewWorkspaceButton: React.FC<NewWorkspaceButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popNewWorkspace = () => {
    dispatch(popNewWorkspaceModal());
  };

  return (
    <div className={styles.infoContainer}>
      <div className={cn(styles.profilePicContainer, styles["profilePicContainer-without-profile-pic"])}>
        <div className={styles.firstLetter}>
          <LuPlus />
        </div>
      </div>
      <Button className={styles.nameContainer} onClick={popNewWorkspace}>
        <span className={styles.title}>{t("newWorkspaceButtonTooltip")}</span>
      </Button>
    </div>
  );
};

export default NewWorkspaceButton;
