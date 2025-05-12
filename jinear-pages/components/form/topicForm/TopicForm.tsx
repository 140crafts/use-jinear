import Button, { ButtonVariants } from "@/components/button";
import { TeamDto, TopicInitializeRequest, TopicUpdateRequest, WorkspaceDto } from "@/model/be/jinear-core";
import { useDeleteTopicMutation, useInitializeTopicMutation, useUpdateTopicMutation } from "@/store/api/topicApi";
import Logger from "@/utils/logger";
import { normalizeStrictly } from "@/utils/normalizeHelper";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import styles from "./TopicForm.module.css";
import ColorInput from "./colorInput/ColorInput";
import NameInput from "./nameInput/NameInput";
import TagInput from "./tagInput/TagInput";

export interface ITopicForm {
  workspaceId?: string;
  teamId?: string;
  topicId?: string;
  color?: string;
  name?: string;
  tag?: string;
}

interface TopicFormProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  topicId?: string;
  color?: string;
  taskName?: string;
  taskTag?: string;
  onSuccess?: () => void;
}

const logger = Logger("TopicForm");

const TopicForm: React.FC<TopicFormProps> = ({ workspace, team, topicId, color, taskName, taskTag, onSuccess }) => {
  const { t } = useTranslation();
  const router = useRouter();

  const { register, handleSubmit, control, setFocus, setValue, watch } = useForm<ITopicForm>();

  const name = watch("name");
  const tag = watch("tag");

  const [initializeTopic, { isLoading: isInitializeTopicLoading, isSuccess: isInitializeTopicSuccess }] =
    useInitializeTopicMutation();

  const [updateTopic, { isLoading: isUpdateTopicLoading, isSuccess: isUpdateTopicSuccess }] = useUpdateTopicMutation();

  const [deleteTopicCall, { isLoading: isDeleteLoading, isSuccess: isDeleteSuccess }] = useDeleteTopicMutation();

  useEffect(() => {
    if (name) {
      const tag = normalizeStrictly(name)?.substring(0, 3);
      setValue("tag", tag);
    }
  }, [name]);

  useEffect(() => {
    if (tag) {
      setValue("tag", normalizeStrictly(tag));
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
    if (isInitializeTopicSuccess || isUpdateTopicSuccess || isDeleteSuccess) {
      onSuccess ? onSuccess() : routeToList();
    }
  }, [isInitializeTopicSuccess, isUpdateTopicSuccess, isDeleteSuccess]);

  const submit: SubmitHandler<ITopicForm> = (data) => {
    logger.log({ data });
    const color = data.color?.replace("#", "").substring(0, 6);
    if (data.topicId) {
      updateTopic({ ...data, color } as TopicUpdateRequest);
    } else {
      initializeTopic({ ...data, color } as TopicInitializeRequest);
    }
  };

  const deleteTopic = () => {
    if (topicId) {
      deleteTopicCall(topicId);
    }
  };

  const routeToList = () => {
    router.replace(`/${workspace.username}/tasks/${team.username}/topic/list`);
  };

  return (
    <form autoComplete="off" id={"topic-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      <div className={styles.formContent}>
        <input type="hidden" value={workspace.workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={team.teamId} {...register("teamId")} />
        {topicId && <input type="hidden" value={topicId} {...register("topicId")} />}

        <NameInput register={register} labelClass={styles.label} />
        <TagInput register={register} labelClass={styles.label} />
        <ColorInput register={register} watch={watch} labelClass={styles.label} />
      </div>

      <div className={styles.footerContainer}>
        {topicId && (
          <Button
            type="button"
            disabled={isDeleteLoading}
            loading={isDeleteLoading}
            className={styles.footerButtonDelete}
            variant={ButtonVariants.contrast}
            onClick={deleteTopic}
          >
            {t("topicFormDelete")}
          </Button>
        )}
        <Button
          type="submit"
          disabled={isInitializeTopicLoading || isUpdateTopicLoading || isDeleteLoading}
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
