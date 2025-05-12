import Button, { ButtonHeight } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useToggle } from "@/hooks/useToggle";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveMembershipsQuery } from "@/store/api/teamMemberApi";
import { popNewTeamModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import { LuChevronDown, LuChevronRight } from "react-icons/lu";
import MenuGroupTitle from "../menuGroupTitle/MenuGroupTitle";
import styles from "./BasicTeamList.module.css";
import BasicTeamMenu from "./basicTeamMenu/BasicTeamMenu";

interface BasicTeamListProps {
  workspace: WorkspaceDto;
}

const BasicTeamList: React.FC<BasicTeamListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [archivedVisible, toggleArchivedVisible] = useToggle(false);

  const {
    data: membershipsResponse,
    isSuccess,
    isFetching,
  } = useRetrieveMembershipsQuery({ workspaceId: workspace.workspaceId });

  const activeTeamMembershipList = useMemo(
    () => membershipsResponse?.data?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ACTIVE"),
    [membershipsResponse]
  );

  const archivedTeamMembershipList = useMemo(
    () => membershipsResponse?.data?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ARCHIVED"),
    [membershipsResponse]
  );

  const openNewTeamModal = () => {
    dispatch(popNewTeamModal({ visible: true, workspace }));
  };

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuYourTeamsTitle")} hasAddButton={true} onAddButtonClick={openNewTeamModal} />
      {isFetching && <CircularLoading />}
      <div className="spacer-h-1" />
      <div className={styles.teamListContainer}>
        {!isFetching && isSuccess && (
          <>
            {activeTeamMembershipList?.map((teamMemberDto) => (
              <BasicTeamMenu
                key={`basic-team-menu-${teamMemberDto.team.teamId}`}
                team={teamMemberDto.team}
                workspace={workspace}
                role={teamMemberDto.role}
              />
            ))}
            {archivedTeamMembershipList && archivedTeamMembershipList.length != 0 && (
              <div className={styles.archivedListContainer}>
                <Button heightVariant={ButtonHeight.short} className={styles.menuActionButton} onClick={toggleArchivedVisible}>
                  {archivedVisible ? <LuChevronDown /> : <LuChevronRight />}
                  {t("sideMenuArchivedTeamsTitle").replace(
                    "${n}",
                    archivedTeamMembershipList.length > 99 ? "99+" : archivedTeamMembershipList.length + ""
                  )}
                </Button>
                {archivedVisible && (
                  <div className={styles.archivedList}>
                    {archivedTeamMembershipList.map((teamMemberDto) => (
                      <BasicTeamMenu
                        key={`basic-team-menu-${teamMemberDto.team.teamId}`}
                        team={teamMemberDto.team}
                        workspace={workspace}
                        role={teamMemberDto.role}
                      />
                    ))}
                  </div>
                )}
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default BasicTeamList;
