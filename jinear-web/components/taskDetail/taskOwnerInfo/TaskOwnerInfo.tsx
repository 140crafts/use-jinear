import AccountProfileInfo from "@/components/accountProfileInfo/AccountProfileInfo";
import { PlainAccountProfileDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TaskOwnerInfo.module.css";

interface TaskOwnerAndAssigneeInfoProps {
  createdDate: Date;
  owner?: PlainAccountProfileDto | null;
}

const TaskOwnerAndAssigneeInfo: React.FC<TaskOwnerAndAssigneeInfoProps> = ({
  createdDate,
  owner,
}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      {/* <div>
        {`${t("taskDetailPageCreatedAt")} ${format(
          new Date(createdDate),
          t("dateTimeFormat")
        )}`}
      </div> */}
      {owner && <AccountProfileInfo {...owner} />}
    </div>
  );
};

export default TaskOwnerAndAssigneeInfo;
