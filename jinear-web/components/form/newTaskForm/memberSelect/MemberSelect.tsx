import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import styles from "./MemberSelect.module.css";

interface MemberSelectProps {
  teamId: string;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
  labelClass: string;
  loadingClass: string;
  selectClass: string;
}

const MemberSelect: React.FC<MemberSelectProps> = ({ teamId, setValue, register, labelClass, loadingClass, selectClass }) => {
  const { t } = useTranslation();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);

  const {
    data: teamMembersResponse,
    isSuccess: teamMembersIsSuccess,
    isLoading: teamMembersIsLoading,
    isError: teamMembersIsError,
  } = useRetrieveTeamMembersQuery(teamId, { skip: teamId == null });

  const assignTaskToCurrentAccount = () => {
    setValue("assignedTo", currentAccountId);
  };

  return teamMembersIsLoading ? (
    <div className={cn(labelClass, styles.topicLabel, loadingClass)}>
      <CircularProgress size={18} />
    </div>
  ) : (
    <label className={labelClass} htmlFor="task-assigned-to">
      {t("newTaskModalTaskAssignedToLabel")}
      <select
        disabled={!teamMembersResponse?.data?.hasContent}
        id="task-topic-id"
        className={selectClass}
        {...register("assignedTo")}
      >
        <option value={"no-assignee"}>{t("newTaskModalTaskTopicNoContentValue")}</option>

        {teamMembersResponse?.data?.content.map((teamMember) => (
          <option key={teamMember.teamMemberId} value={teamMember.accountId}>
            {teamMember?.account?.username}
          </option>
        ))}
      </select>
      <Button
        className={styles.assignSelfButton}
        heightVariant={ButtonHeight.mid}
        variant={ButtonVariants.filled}
        onClick={assignTaskToCurrentAccount}
      >
        {t("newTaskModalAssignToYourself")}
      </Button>
    </label>
  );
};

export default MemberSelect;
