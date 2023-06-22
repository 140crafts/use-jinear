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
  personalWorkspaceTitle: string;
  workspaceTitle: string;
  personalWorkspaceLabel: string;
  onTeamChange?: (team: TeamDto) => void;
  readOnly?: boolean;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({
  workspace,
  team,
  personalWorkspaceTitle,
  workspaceTitle,
  personalWorkspaceLabel,
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
      {workspace.isPersonal ? personalWorkspaceTitle : workspaceTitle}
      <div className={styles.buttonContainer}>
        <Button
          disabled={readOnly}
          className={styles.button}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={popChangeWorkspaceModal}
        >
          <IoHomeOutline />
          <b>{workspace.isPersonal ? personalWorkspaceLabel : workspace.title}</b>
        </Button>
        {!workspace.isPersonal && (
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
        )}
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
