import React, { useRef } from "react";
import styles from "./NewMilestoneForm.module.css";
import cn from "classnames";
import useTranslation from "@/locals/useTranslation";
import { SubmitHandler, useForm } from "react-hook-form";
import { InitializeMilestoneRequest, ProjectDto } from "@/be/jinear-core";
import { useInitializeMilestoneMutation } from "@/api/projectMilestoneApi";
import Logger from "@/utils/logger";
import { LuBox, LuChevronRight } from "react-icons/lu";
import { ITiptapRef } from "@/components/tiptap/Tiptap";
import DescriptionInput from "@/components/form/newMilestoneForm/descriptionInput/DescriptionInput";
import TitleInput from "@/components/form/newMilestoneForm/titleInput/TitleInput";
import TargetDateInput from "@/components/form/newMilestoneForm/targetDateInput/TargetDateInput";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";

interface NewMilestoneFormProps {
  className?: string;
  project: ProjectDto;
  onClose: () => void;
}

const logger = Logger("NewMilestoneForm");

const NewMilestoneForm: React.FC<NewMilestoneFormProps> = ({ className, project, onClose }) => {
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    getValues,
    watch,
    reset,
    formState: { errors }
  } = useForm<InitializeMilestoneRequest>();
  const descriptionRef = useRef<ITiptapRef>(null);

  const [initializeMilestone, { isLoading: isInitializeMilestoneLoading }] = useInitializeMilestoneMutation();

  const submit: SubmitHandler<InitializeMilestoneRequest> = (data) => {
    const description = descriptionRef.current?.getHTML();
    logger.log({ submit: { ...data, description } });
    const req = { ...data };
    initializeMilestone(req);
    onClose?.();
  };

  const onMilestoneDateChange = (date?: Date | null) => {
    setValue("targetDate", date);
  };

  return (
    <form
      autoComplete="off"
      id={"new-project-form"}
      className={cn(styles.form, className)}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>

        <div className={styles.projectInfoContainer}>
          <span>{t("newMilestoneFormProjectInfo")}</span>
          <div className={styles.projectContainer}>
            <LuBox className={"icon"} />
            <span className={"flex-1 line-clamp"}>{project.title}</span>
            <LuChevronRight className={"icon"} />
          </div>
        </div>

        <input type="hidden" value={project.projectId} {...register("projectId")} />

        <TitleInput register={register} />

        <DescriptionInput tiptapRef={descriptionRef} register={register} />

        <TargetDateInput onMilestoneDateChange={onMilestoneDateChange} />
      </div>

      <div className={styles.footerContainer}>
        <div className="flex-1" />
        <div className={styles.footerActionButtonContainer}>
          <Button
            disabled={isInitializeMilestoneLoading}
            onClick={onClose}
            className={styles.footerButton}
            heightVariant={ButtonHeight.short}
          >
            {t("newMilestoneModalCancel")}
          </Button>
          <Button
            type="submit"
            disabled={isInitializeMilestoneLoading}
            loading={isInitializeMilestoneLoading}
            className={styles.footerButton}
            variant={ButtonVariants.contrast}
            heightVariant={ButtonHeight.short}
            progessClassname={styles.loadingButton}
          >
            {t("newMilestoneModalCreate")}
          </Button>
        </div>
      </div>

    </form>
  );
};

export default NewMilestoneForm;