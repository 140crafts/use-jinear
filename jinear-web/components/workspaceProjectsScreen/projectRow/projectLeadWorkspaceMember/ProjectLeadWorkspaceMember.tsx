import React from "react";
import styles from "./ProjectLeadWorkspaceMember.module.css";
import { ProjectDto, WorkspaceMemberDto } from "@/be/jinear-core";
import Button, { ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import useTranslation from "@/locals/useTranslation";
import { LuUser } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { popWorkspaceMemberPickerModal } from "@/slice/modalSlice";
import { useUpdateProjectLeadMutation } from "@/api/projectOperationApi";
import Logger from "@/utils/logger";

interface ProjectLeadWorkspaceMemberProps {
  project: ProjectDto;
}

const logger = Logger("ProjectLeadWorkspaceMember");

const ProjectLeadWorkspaceMember: React.FC<ProjectLeadWorkspaceMemberProps> = ({ project }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { workspaceId, projectId, leadWorkspaceMember } = project;

  const tooltip = leadWorkspaceMember ?
    t("projectRowProjectLead").replace("${leadUserName}", leadWorkspaceMember?.account?.username ?? "") :
    t("projectRowProjectLeadPick");

  const [updateProjectLead, { isLoading }] = useUpdateProjectLeadMutation();

  const onPickerPicked = (pickedList: WorkspaceMemberDto[]) => {
    logger.log({ onPickerPicked: pickedList });
    updateProjectLead({
      projectId,
      body: { workspaceMemberId: pickedList?.[0]?.workspaceMemberId }
    });
  };

  const unPickLead = () => {
    updateProjectLead({
      projectId,
      body: { workspaceMemberId: null }
    });
  };

  const popWorkspaceMemberPicker = () => {
    dispatch(popWorkspaceMemberPickerModal({
      visible: true,
      workspaceId,
      multiple: false,
      initialSelectionOnMultiple: leadWorkspaceMember ? [leadWorkspaceMember] : [],
      onPick: onPickerPicked,
      deselectable: true,
      onDeselect: unPickLead
    }));
  };

  return (
    <Button className={styles.container}
            data-tooltip-right={tooltip}
            onClick={popWorkspaceMemberPicker}
            disabled={project.archived || isLoading}
            loading={isLoading}>
      {leadWorkspaceMember ?
        <ProfilePhoto
          boringAvatarKey={leadWorkspaceMember.account.accountId || ""}
          storagePath={leadWorkspaceMember.account.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        /> :
        <LuUser className={styles.icon} />
      }
    </Button>
  )
    ;
};

export default ProjectLeadWorkspaceMember;