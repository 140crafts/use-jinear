import React, { useEffect, useRef, useState } from "react";
import styles from "./ProjectFeedInfo.module.scss";
import { ProjectFeedSettingsDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import { useToggle } from "@/hooks/useToggle";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import cn from "classnames";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPencil } from "react-icons/lu";
import { CircularProgress } from "@mui/material";
import { useUpdateProjectFeedSettingsMutation } from "@/api/projectFeedSettingsApi";

interface ProjectFeedInfoProps {
  projectFeedSettings: ProjectFeedSettingsDto;
  isFetching?: boolean;
  editable?: boolean;
}

const ProjectFeedInfo: React.FC<ProjectFeedInfoProps> = ({ projectFeedSettings, isFetching, editable = true }) => {
  const { t } = useTranslation();
  const [readOnly, toggleReadOnly] = useToggle(true);
  const [initialValue, setInitialValue] = useState(projectFeedSettings?.info?.value);
  const [updateProjectFeedSettings, {
    isSuccess: isUpdateSuccess,
    isLoading: isUpdateLoading
  }] = useUpdateProjectFeedSettingsMutation();

  const tiptapRef = useRef<ITiptapRef>(null);

  useEffect(() => {
    if (projectFeedSettings?.info?.value) {
      setInitialValue(projectFeedSettings?.info?.value);
    }
  }, [projectFeedSettings?.info?.value]);

  const save = () => {
    const input: HTMLInputElement | null = document.getElementById(`${projectFeedSettings?.info?.richTextId}`) as HTMLInputElement;
    if (input) {
      const value = input?.value || "";
      const req = {
        projectId: projectFeedSettings.projectId,
        info: value
      };
      updateProjectFeedSettings(req);
    }
  };

  const toggle = () => {
    toggleReadOnly();
    if (!readOnly) {
      save();
    } else {
      tiptapRef?.current?.focus();
    }
  };

  const cancel = () => {
    toggleReadOnly();
    setInitialValue("");
    setTimeout(() => {
      setInitialValue(projectFeedSettings?.info?.value);
    }, 100);
  };

  return (
    <div className={cn(styles.container)}>
      <div className={styles.titleContainer}>
        <h3>{t("projectFeedInfoSectionTitle")}</h3>
        <span>{t("projectFeedInfoSectionText")}</span>
      </div>
      <div className={styles.inputContainer}>
        <Tiptap
          ref={tiptapRef}
          content={initialValue}
          editable={!readOnly}
          placeholder={t("projectFeedInfoPlaceholder")}
          htmlInputId={`${projectFeedSettings?.info?.richTextId}`}
        />
        {readOnly && editable && (
          <Button
            disabled={isFetching}
            onClick={toggle}
            className={styles.editButton}
            variant={ButtonVariants.contrast}
            heightVariant={ButtonHeight.short}
            data-tooltip-right={t("projectFeedInfoEdit")}
          >
            <LuPencil />
          </Button>
        )}
        <div className={styles.actionContainer}>
          {isUpdateLoading && (
            <div className={styles.loadingContainer}>
              <CircularProgress size={14} />
              <div>{t("projectFeedInfoSaving")}</div>
            </div>
          )}

          {!readOnly && (
            <Button
              disabled={isUpdateLoading || isFetching}
              loading={isUpdateLoading}
              heightVariant={ButtonHeight.mid}
              variant={readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast}
              onClick={toggle}
            >
              {t("projectFeedInfoSave")}
            </Button>
          )}
          {!readOnly && (
            <Button disabled={isUpdateLoading || isFetching}
                    heightVariant={ButtonHeight.mid}
                    variant={ButtonVariants.filled2}
                    onClick={cancel}>
              {t("projectFeedInfoCancel")}
            </Button>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProjectFeedInfo;