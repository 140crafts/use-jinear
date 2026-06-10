import { copyTextToClipboard } from "@/utils/clipboard";
import { HOST } from "@/utils/constants";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import React from "react";
import toast from "react-hot-toast";
import { FaCheck } from "react-icons/fa";
import Button, { ButtonHeight } from "../button";
import styles from "./TaskCreatedToast.module.css";

interface TaskCreatedToastProps {
  taskLink: string;
  teamTaskNo: string;
}

const TaskCreatedToast: React.FC<TaskCreatedToastProps> = ({ taskLink, teamTaskNo }) => {
  const { t } = useTranslation();

  const copyToClipboard = () => {
    copyTextToClipboard(`${HOST}/${taskLink}`);
    toast(t("newTaskCreatedToastCopiedToClipboard"));
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <FaCheck />
        <div className={styles.title}>{t("newTastCreatedToastText").replace("${taskNo}", teamTaskNo)}</div>
      </div>
      <div className={styles.actionButtonContainer}>
        <Button onClick={copyToClipboard} className={styles.button} heightVariant={ButtonHeight.short}>
          {t("newTastCreatedToastCopyUrl")}
        </Button>

        <Button className={styles.button} heightVariant={ButtonHeight.short}>
          <Link className={styles.button} href={`/${taskLink}`}>
            {t("newTastCreatedToastGoToTask")}
          </Link>
        </Button>
      </div>
    </div>
  );
};

export default TaskCreatedToast;
