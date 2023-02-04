import Button, { ButtonVariants } from "@/components/button";
import {
  TopicInitializeRequest,
  TopicUpdateRequest,
} from "@/model/be/jinear-core";
import {
  useInitializeTopicMutation,
  useUpdateTopicMutation,
} from "@/store/api/topicApi";
import {
  selectCurrentAccountsPreferredTeam,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import ColorInput from "./colorInput/ColorInput";
import NameInput from "./nameInput/NameInput";
import TagInput from "./tagInput/TagInput";
import styles from "./TopicForm.module.css";

export interface ITopicForm {
  workspaceId?: string;
  teamId?: string;
  topicId?: string;
  color?: string;
  name?: string;
  tag?: string;
}

interface TopicFormProps {
  workspaceId?: string;
  teamId?: string;
  topicId?: string;
  color?: string;
  taskName?: string;
  taskTag?: string;
}

const logger = Logger("TopicForm");

const TopicForm: React.FC<TopicFormProps> = ({
  workspaceId,
  teamId,
  topicId,
  color,
  taskName,
  taskTag,
}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();
  const preferredWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const { register, handleSubmit, control, setFocus, setValue, watch } =
    useForm<ITopicForm>();

  const name = watch("name");
  const tag = watch("tag");

  const [
    initializeTopic,
    {
      isLoading: isInitializeTopicLoading,
      isSuccess: isInitializeTopicSuccess,
    },
  ] = useInitializeTopicMutation();

  const [
    updateTopic,
    { isLoading: isUpdateTopicLoading, isSuccess: isUpdateTopicSuccess },
  ] = useUpdateTopicMutation();

  useEffect(() => {
    if (name) {
      const tag = name?.toUpperCase?.()?.split(" ")?.join("-")?.substring(0, 3);
      setValue("tag", tag);
    }
  }, [name]);

  useEffect(() => {
    if (tag) {
      setValue("tag", tag?.toUpperCase?.()?.split(" ")?.join("-"));
    }
  }, [tag]);

  useEffect(() => {
    logger.log({ color, taskName, taskTag });
    if (color) {
      setValue("color", `#${color}`);
    }
    if (taskName) {
      setValue("name", taskName);
    }
    if (taskTag) {
      setValue("tag", taskTag);
    }
  }, [color, taskName, taskTag]);

  useEffect(() => {
    if (isInitializeTopicSuccess || isUpdateTopicSuccess) {
      router.replace(
        `/${preferredWorkspace?.username}/${preferredTeam?.name}/topic/list`
      );
    }
  }, [isInitializeTopicSuccess, isUpdateTopicSuccess]);

  const submit: SubmitHandler<ITopicForm> = (data) => {
    logger.log({ data });
    const color = data.color?.replace("#", "").substring(0, 6);
    if (data.topicId) {
      updateTopic({ ...data, color } as TopicUpdateRequest);
    } else {
      initializeTopic({ ...data, color } as TopicInitializeRequest);
    }
  };

  return (
    <form
      autoComplete="off"
      id={"topic-form"}
      className={styles.form}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>
        {workspaceId && (
          <input
            type="hidden"
            value={workspaceId}
            {...register("workspaceId")}
          />
        )}
        {teamId && (
          <input type="hidden" value={teamId} {...register("teamId")} />
        )}
        {topicId && (
          <input type="hidden" value={topicId} {...register("topicId")} />
        )}

        <NameInput register={register} labelClass={styles.label} />
        <TagInput register={register} labelClass={styles.label} />
        <ColorInput
          register={register}
          watch={watch}
          labelClass={styles.label}
        />
      </div>

      <div className={styles.footerContainer}>
        <Button
          type="submit"
          disabled={isInitializeTopicLoading || isUpdateTopicLoading}
          loading={isInitializeTopicLoading || isUpdateTopicLoading}
          className={styles.footerButton}
          variant={ButtonVariants.contrast}
        >
          {t("topicFormSave")}
        </Button>
      </div>
    </form>
  );
};

export default TopicForm;
