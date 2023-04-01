import Button, { ButtonVariants } from "@/components/button";
import { TeamInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeTeamMutation } from "@/store/api/teamApi";
import { useUpdatePreferredTeamMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import { normalizeUsernameReplaceSpaces } from "@/utils/normalizeHelper";
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

  const submit: SubmitHandler<TeamInitializeRequest> = (data) => {
    logger.log({ data });
    initializeTeam(data);
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

      <div className={styles.footerContainer}>
        <Button disabled={isLoading} onClick={close} className={styles.footerButton}>
          {t("newWorkspaceFormCancel")}
        </Button>
        <Button
          type="submit"
          disabled={isLoading}
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
