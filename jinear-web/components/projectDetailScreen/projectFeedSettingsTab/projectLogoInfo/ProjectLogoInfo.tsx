import React, { ChangeEvent, useRef, useState } from "react";
import styles from "./ProjectLogoInfo.module.css";
import cn from "classnames";
import useTranslation from "@/locals/useTranslation";
import { ProjectDto, ProjectFeedSettingsDto } from "@/be/jinear-core";
import ProfilePhoto from "@/components/profilePhoto";
import { LuBox, LuPen, LuPencil } from "react-icons/lu";
import { toast } from "react-hot-toast";
import { useUpdateProjectLogoMutation } from "@/api/projectOperationApi";

interface ProjectLogoInfoProps {
  project: ProjectDto;
  projectFeedSettings: ProjectFeedSettingsDto;
  isFetching?: boolean;
  editable?: boolean;
}

const ProjectLogoInfo: React.FC<ProjectLogoInfoProps> = ({
                                                           project,
                                                           projectFeedSettings,
                                                           isFetching,
                                                           editable = true
                                                         }) => {
  const { t } = useTranslation();
  const logoPickerButtonRef = useRef<HTMLInputElement>(null);
  const [updateProjectLogo, { isLoading }] = useUpdateProjectLogoMutation();

  const pickPhoto = () => {
    if (!editable) {
      return;
    }
    if (logoPickerButtonRef.current) {
      logoPickerButtonRef.current.value = "";
      logoPickerButtonRef.current.click();
    }
  };

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      const file = event.target?.files?.[0];
      if (file) {
        const projectId = project.projectId;
        const formData = new FormData();
        formData.append("file", file);
        updateProjectLogo({ projectId, formData });
      }
      return;
    }
  };

  return (
    <div className={cn(styles.container)}>
      <div className={styles.logoContainer} data-tooltip-top={t("projectLogoSectionText")}
           onClick={editable ? pickPhoto : undefined}>
        {project.logo ? <ProfilePhoto
            boringAvatarKey={project.projectId}
            url={project.logo?.url}
            wrapperClassName={styles.profilePic}
            imgClassName={styles.profilePicImg}
          /> :
          <div className={styles.profilePic}>
            <LuBox size={42} />
            {editable && <LuPencil size={18} className={styles.editButton} />}
          </div>
        }
      </div>
      {editable && <input
        ref={logoPickerButtonRef}
        id={"logo-picker"}
        type="file"
        accept="image/*"
        className={styles.photoInput}
        onChange={onSelectFile}
      />}
    </div>
  );
};
export default ProjectLogoInfo;