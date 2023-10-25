import Button, { ButtonVariants } from "@/components/button";
import { AddTeamMemberRequest, TeamMemberRoleType } from "@/model/be/jinear-core";
import { useAddTeamMemberMutation, useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { useRetrieveWorkspaceMembersQuery } from "@/store/api/workspaceMemberApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useMemo } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import styles from "./AddMemberToTeamForm.module.css";

interface AddMemberToTeamFormProps {
  workspaceId: string;
  workspaceName: string;
  teamId: string;
  onAddSuccess: () => void;
  requestClose: () => void;
}

const logger = Logger("AddMemberToTeamForm");

const AVAILABLE_TEAM_ROLES: TeamMemberRoleType[] = ["ADMIN", "MEMBER"]; //, "GUEST"

const AddMemberToTeamForm: React.FC<AddMemberToTeamFormProps> = ({
  workspaceId,
  workspaceName,
  teamId,
  onAddSuccess,
  requestClose,
}) => {
  const { t } = useTranslation();
  const { register, handleSubmit, setFocus, setValue, watch } = useForm<AddTeamMemberRequest>();
  const currentSelectedRole: TeamMemberRoleType = watch("role");
  const { data: teamMembersResponse } = useRetrieveTeamMembersQuery({ teamId });
  const { data: workplaceMembersResponse } = useRetrieveWorkspaceMembersQuery({ workspaceId });
  const [addTeamMember, { isLoading: isAddLoading, isSuccess: isAddSuccess }] = useAddTeamMemberMutation();

  const availableMembers = useMemo(() => {
    const teamMembers = teamMembersResponse?.data.content;
    const workspaceMembers = workplaceMembersResponse?.data.content;
    return (
      workspaceMembers?.filter(
        (workspaceMember) => teamMembers?.findIndex((teamMember) => teamMember.accountId == workspaceMember.accountId) == -1
      ) || []
    );
  }, [teamMembersResponse, workplaceMembersResponse]);

  useEffect(() => {
    setValue("role", "MEMBER");
  }, []);

  useEffect(() => {
    if (onAddSuccess && isAddSuccess) {
      onAddSuccess();
    }
  }, [onAddSuccess, isAddSuccess]);

  const submit: SubmitHandler<AddTeamMemberRequest> = (data) => {
    logger.log({ data });
    addTeamMember(data);
  };

  return (
    <div className={styles.container}>
      <form autoComplete="off" id={"team-add-member-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
        <input type="hidden" value={teamId} {...register("teamId")} />

        <label className={styles.label} htmlFor={"new-member-account-id"}>
          {t("addMemberToTeamFormAccountSelectLabel")}
          <select id="new-member-account-id" {...register("accountId")}>
            {availableMembers.map((member) => (
              <option key={`available-member-${member.accountId}`} value={member.accountId}>
                {member.account.username}
              </option>
            ))}
          </select>
        </label>

        <span className={styles.workspaceMembersInfoContainer}>
          {t("addMemberToTeamFormAccountSelectInfo")}
          <Button href={`/${workspaceName}/members`} onClick={requestClose} className={styles.workspaceMembersButton}>
            {t("addMemberToTeamFormAccountSelectInfoWorkspaceMembers")}
          </Button>
        </span>

        <div>
          <label className={styles.label} htmlFor={"new-member-team-role"}>
            {t("addMemberToTeamFormAccountTeamRole")}
            <select id="new-member-team-role" {...register("role")}>
              {AVAILABLE_TEAM_ROLES.map((role) => (
                <option key={`new-team-member-role-${role}`} value={role}>
                  {t(`addMemberToTeamFormRole_${role}`)}
                </option>
              ))}
            </select>
          </label>
          <div className="spacer-h-1" />
          <div className={cn(styles.inputSubtext, styles.inputRoleText)}>
            {t(`addMemberToTeamFormRoleText_${currentSelectedRole}`)}
          </div>
        </div>

        <div className="spacer-h-1" />

        <Button
          disabled={isAddLoading}
          loading={isAddLoading}
          type="submit"
          className={styles.submitButton}
          variant={ButtonVariants.contrast}
        >
          <div>{t("addMemberToTeamFormSubmit")}</div>
        </Button>
      </form>
    </div>
  );
};

export default AddMemberToTeamForm;
