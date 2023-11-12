import Button, { ButtonVariants } from "@/components/button";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import { TeamInitializeRequest, TeamTaskVisibilityType } from "@/model/be/jinear-core";
import { useInitializeTeamMutation } from "@/store/api/teamApi";
import { useUpdatePreferredTeamMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import { normalizeUsernameReplaceSpaces } from "@/utils/normalizeHelper";
import { hasWorkspaceTeamVisibilityTypeSelectAccess } from "@/utils/permissionHelper";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import styles from "./NewTeamForm.module.css";

interface NewTeamFormProps {
  close: () => void;
}

const logger = Logger("NewWorkspaceForm");
const MAX_TAG_LENGTH = 10;

const NewTeamForm: React.FC<NewTeamFormProps> = ({ close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [initializeTeam, { data: initializeResponse, isLoading }] = useInitializeTeamMutation();
  const [updatePreferredTeam, { isLoading: isPrefferedTeamUpdateLoading }] = useUpdatePreferredTeamMutation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const { register, handleSubmit, setFocus, setValue, watch } = useForm<TeamInitializeRequest>();
  const [candidateUsername, setCandidateUsername] = useState<string>("example-team");
  const currName = watch("name");
  const currTag = watch("tag");
  const [taskVisibilityType, setTaskVisibilityType] = useState<TeamTaskVisibilityType>("VISIBLE_TO_ALL_TEAM_MEMBERS");
  const hasAccess = hasWorkspaceTeamVisibilityTypeSelectAccess(currentWorkspace);

  useEffect(() => {
    const normalizedUsername = normalizeUsernameReplaceSpaces(currName);
    const normalizedTag = normalizedUsername?.substring(0, MAX_TAG_LENGTH)?.toLocaleUpperCase();
    setCandidateUsername(normalizedUsername);
    setValue("tag", normalizedTag);
    setValue("username", normalizedUsername);
  }, [currName]);

  useEffect(() => {
    const normalizedTag = normalizeUsernameReplaceSpaces(currTag)?.substring(0, MAX_TAG_LENGTH)?.toLocaleUpperCase();
    setValue("tag", normalizedTag);
  }, [currTag]);

  useEffect(() => {
    if (initializeResponse && initializeResponse.data.teamId) {
      updatePreferredTeam({
        teamId: initializeResponse.data.teamId,
        workspaceId: initializeResponse.data.workspaceId,
      });
    }
  }, [initializeResponse]);

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isPrefferedTeamUpdateLoading }));
    isPrefferedTeamUpdateLoading && close?.();
  }, [isPrefferedTeamUpdateLoading]);

  const changeTaskVisibilityType = (value: string, index: number) => {
    if (value && (value == "VISIBLE_TO_ALL_TEAM_MEMBERS" || value == "OWNER_ASSIGNEE_AND_ADMINS")) {
      setTaskVisibilityType?.(value);
    }
  };

  const submit: SubmitHandler<TeamInitializeRequest> = (data) => {
    const req = { ...data };
    req.taskVisibility = taskVisibilityType;
    logger.log({ req });
    initializeTeam(req);
  };

  return (
    <form autoComplete="off" id={"new-team-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      {currentWorkspace && <input type="hidden" value={currentWorkspace.workspaceId} {...register("workspaceId")} />}
      <input id={"new-team-username"} type={"hidden"} {...register("username")} />

      <label className={cn(styles.label, "flex-1")} htmlFor={"new-team-name"}>
        {`${t("newTeamFormName")} *`}
        <input id={"new-team-name"} type={"text"} {...register("name", { required: t("formRequiredField") })} />
        <div className={styles.link}>{`${HOST}/${currentWorkspace?.username}/${candidateUsername?.toLocaleLowerCase()}`}</div>
      </label>

      <label className={cn(styles.label, "flex-1")} htmlFor={"new-team-tag"}>
        {`${t("newTeamFormTag")} * ${currTag?.length}/${MAX_TAG_LENGTH}`}
        <input
          id={"new-team-tag"}
          type={"text"}
          minLength={1}
          maxLength={10}
          {...register("tag", { required: t("formRequiredField") })}
        />
      </label>

      <label className={cn(styles.label, "flex-1")} htmlFor={"new-team-task-visibility-type-segment-control"}>
        {t("newTeamTaskVisibility")}
        <div className={styles.visibilityTypeContainer}>
          <SegmentedControl
            id="new-team-task-visibility-type-segment-control"
            name="new-team-task-visibility-type-segment-control"
            defaultIndex={["VISIBLE_TO_ALL_TEAM_MEMBERS", "OWNER_ASSIGNEE_AND_ADMINS"].indexOf(taskVisibilityType)}
            segments={[
              { label: t("teamTaskVisibility_VISIBLE_TO_ALL_TEAM_MEMBERS"), value: "VISIBLE_TO_ALL_TEAM_MEMBERS" },
              { label: t("teamTaskVisibility_OWNER_ASSIGNEE_AND_ADMINS"), value: "OWNER_ASSIGNEE_AND_ADMINS" },
            ]}
            segmentLabelClassName={styles.viewTypeSegmentLabel}
            callback={changeTaskVisibilityType}
          />
          <label>{t(`teamTaskVisibilityDetail_${taskVisibilityType}`)}</label>
          {!hasAccess && currentWorkspace && taskVisibilityType != "VISIBLE_TO_ALL_TEAM_MEMBERS" && (
            <div className={styles.upgradeYourPlanContainer}>
              {t("genericYouNeedToUpgradePlanText")}
              <WorkspaceUpgradeButton workspace={currentWorkspace} variant={"FULL"} className={styles.upgradeButton} />
            </div>
          )}
        </div>
      </label>

      <div className={styles.footerContainer}>
        <Button disabled={isLoading} onClick={close} className={styles.footerButton}>
          {t("newWorkspaceFormCancel")}
        </Button>
        <Button
          type="submit"
          disabled={isLoading || (taskVisibilityType != "VISIBLE_TO_ALL_TEAM_MEMBERS" && !hasAccess)}
          loading={isLoading}
          className={styles.footerButton}
          variant={ButtonVariants.contrast}
        >
          {t("newWorkspaceFormCreate")}
        </Button>
      </div>
    </form>
  );
};

export default NewTeamForm;
