import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceMemberInviteRequest } from "@/model/be/jinear-core";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { useInviteWorkspaceMutation } from "@/store/api/workspaceMemberInvitationApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import styles from "./WorkspaceMemberInvitationForm.module.css";

interface WorkspaceMemberInvitationFormProps {
  workspaceId: string;
  onInviteSuccess?: () => void;
}

const logger = Logger("WorkspaceMemberInvitationForm");

interface IWorkspaceMemberInviteForm {
  email: string;
  workspaceId: string;
  initialTeamId: string;
  isGuest: boolean;
}
type ASSIGNABLE_WORKSPACE_ROLE_TYPES = "ADMIN" | "MEMBER" | "GUEST";
const ASSIGNABLE_WORKSPACE_ROLES: ASSIGNABLE_WORKSPACE_ROLE_TYPES[] = ["ADMIN", "MEMBER", "GUEST"];

const WorkspaceMemberInvitationForm: React.FC<WorkspaceMemberInvitationFormProps> = ({ workspaceId, onInviteSuccess }) => {
  const { t } = useTranslation();
  const { register, handleSubmit, setFocus, setValue, watch } = useForm<WorkspaceMemberInviteRequest>();
  //@ts-ignore
  const forRole: ASSIGNABLE_WORKSPACE_ROLE_TYPES = watch("forRole");

  const {
    data: teamsResponse,
    isSuccess: isTeamsResponseSuccess,
    isError: isTeamsResponseError,
    isLoading: isTeamsResponseLoading,
  } = useRetrieveWorkspaceTeamsQuery(workspaceId);

  const [inviteWorkspace, { isSuccess: isInviteSuccess, isLoading: isInviteLoading }] = useInviteWorkspaceMutation();

  useEffect(() => {
    setFocus("email");
    setValue("forRole", "MEMBER");
  }, []);

  useEffect(() => {
    if (isInviteSuccess && onInviteSuccess) {
      onInviteSuccess();
    }
  }, [onInviteSuccess, isInviteSuccess]);

  const submit: SubmitHandler<WorkspaceMemberInviteRequest> = (data) => {
    logger.log({ data });
    inviteWorkspace(data);
  };

  return (
    <div className={styles.container}>
      <form
        autoComplete="off"
        id={"workspace-invitation-form"}
        className={styles.form}
        onSubmit={handleSubmit(submit)}
        action="#"
      >
        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <label className={styles.label} htmlFor={"new-member-mail"}>
          {`${t("workspaceMemberInvititationFormEmailLabel")} *`}
          <input id={"new-member-mail"} type={"email"} {...register("email", { required: t("formRequiredField") })} />
          <div className={styles.inputSubtext}>{t("workspaceMemberInvititationFormEmailText")}</div>
        </label>

        <div>
          <label className={styles.label} htmlFor={"new-member-role"}>
            {t("workspaceMemberInvititationFormForRoleLabel")}
            <select id="new-member-role" {...register("forRole")}>
              {ASSIGNABLE_WORKSPACE_ROLES.map((role) => (
                <option key={`new-member-role-${role}`} value={role}>
                  {t(`workspaceMemberInvititationFormForRole_${role}`)}
                </option>
              ))}
            </select>
          </label>
          <div className="spacer-h-1" />
          <div className={cn(styles.inputSubtext, styles.inputRoleText)}>
            {t(`workspaceMemberInvititationFormForRoleText_${forRole}`)}
          </div>
        </div>

        <div>
          <label className={styles.label} htmlFor={"new-member-initial-team"}>
            {t("workspaceMemberInvititationFormInitialTeam")}
            <select id="new-member-initial-team" {...register("initialTeamId")}>
              {teamsResponse?.data.map((team) => (
                <option key={`new-member-initial-team-${team.teamId}`} value={team.teamId}>
                  {team.name}
                </option>
              ))}
            </select>
          </label>
          <div className="spacer-h-1" />
          <div className={styles.inputSubtext}>{t("workspaceMemberInvititationFormInitialTeamText")}</div>
        </div>

        <div className="spacer-h-4" />

        <Button
          disabled={isInviteLoading}
          loading={isInviteLoading}
          type="submit"
          className={styles.submitButton}
          variant={ButtonVariants.contrast}
        >
          <div>{t("workspaceMemberInvititationFormSubmit")}</div>
        </Button>
        {/* <ErrorMessagee errors={errors} name="singleErrorInput" /> */}
      </form>
    </div>
  );
};

export default WorkspaceMemberInvitationForm;
