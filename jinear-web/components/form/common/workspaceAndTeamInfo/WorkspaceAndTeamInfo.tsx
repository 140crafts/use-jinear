import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility, popTeamPickerModal, popWorkspacePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { IoHomeOutline, IoPeopleOutline } from "react-icons/io5";
import styles from "./WorkspaceAndTeamInfo.module.css";

interface WorkspaceAndTeamInfoProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  workspaceTitle: string;
  onTeamChange?: (team: TeamDto) => void;
  readOnly?: boolean;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({
  workspace,
  team,
  workspaceTitle,
  onTeamChange,
  readOnly = false,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const [updatePreferredWorkspace, { isLoading, isSuccess }] = useUpdatePreferredWorkspaceMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isLoading }));
  }, [isLoading]);

  const onWorkspacePick = (workspace: WorkspaceDto) => {
    updatePreferredWorkspace({ workspaceId: workspace.workspaceId });
  };

  const onTeamPick = (team: TeamDto) => {
    onTeamChange?.(team);
  };

  const popChangeWorkspaceModal = () => {
    dispatch(popWorkspacePickerModal({ currentWorkspaceId, onPick: onWorkspacePick, visible: true }));
  };

  const popChangeTeamModal = () => {
    dispatch(popTeamPickerModal({ visible: true, workspaceId: workspace.workspaceId, onPick: onTeamPick }));
  };

  return (
    <div className={styles.container}>
      {workspaceTitle}
      <div className={styles.buttonContainer}>
        <Button
          //fixed
          disabled={true}
          className={styles.button}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={popChangeWorkspaceModal}
        >
          <IoHomeOutline />
          <b>{workspace.title}</b>
        </Button>
        <Button
          disabled={readOnly}
          className={styles.button}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={popChangeTeamModal}
        >
          <IoPeopleOutline />
          <b>{team.name}</b>
        </Button>
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
