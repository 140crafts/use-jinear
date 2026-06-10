import React, { useMemo } from "react";
import styles from "./TeamTaskFiles.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuChevronDown, LuChevronRight, LuUsers } from "react-icons/lu";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "@/locals/useTranslation";
import { useRetrieveMembershipsQuery } from "@/api/teamMemberApi";
import { useToggle } from "@/hooks/useToggle";
import { useQueryState } from "@/hooks/useQueryState";

interface TeamTaskFilesProps {
  workspaceId: string;
  onTeamClick: (teamId: string) => void;
}

const TeamTaskFiles: React.FC<TeamTaskFilesProps> = ({ workspaceId, onTeamClick }) => {
  const { t } = useTranslation();
  const taskFilesTeamId = useQueryState<string>("taskFilesTeamId");
  const [archivedVisible, toggleArchivedVisible] = useToggle(false);
  const {
    data: membershipsResponse,
    isLoading
  } = useRetrieveMembershipsQuery({ workspaceId });

  const activeTeamMembershipList = useMemo(
    () => membershipsResponse?.data?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ACTIVE"),
    [membershipsResponse]
  );

  const archivedTeamMembershipList = useMemo(
    () => membershipsResponse?.data?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ARCHIVED"),
    [membershipsResponse]
  );

  return (
    <div className={styles.teamListContainer}>
      <MenuGroupTitle label={t("sideMenuTaskFilesTitle")} />

      {!isLoading && (
        <>
          {activeTeamMembershipList?.map((teamMemberDto) => (
            <Button
              key={teamMemberDto.teamId}
              className={styles.button}
              onClick={() => onTeamClick(teamMemberDto.teamId)}
              variant={teamMemberDto.teamId==taskFilesTeamId ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            >
              <LuUsers className={"icon"} />
              <span className={"single-line"}>
                {teamMemberDto.team.name}
              </span>
            </Button>
          ))}

          {archivedTeamMembershipList && archivedTeamMembershipList.length != 0 && (

            <div className={styles.archivedListContainer}>
              <Button heightVariant={ButtonHeight.short} className={styles.menuActionButton}
                      onClick={toggleArchivedVisible}>
                {archivedVisible ? <LuChevronDown /> : <LuChevronRight />}
                {t("sideMenuArchivedTeamsTitle").replace(
                  "${n}",
                  archivedTeamMembershipList.length > 99 ? "99+" : archivedTeamMembershipList.length + ""
                )}
              </Button>

              {archivedVisible && (
                <div className={styles.archivedList}>
                  {archivedTeamMembershipList.map((teamMemberDto) => (
                    <Button
                      key={teamMemberDto.teamId}
                      className={styles.button}
                      onClick={() => onTeamClick(teamMemberDto.teamId)}
                      variant={teamMemberDto.teamId==taskFilesTeamId ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                    >
                      <LuUsers className={"icon"} />
                      <span className={"single-line"}>
                        {teamMemberDto.team.name}
                      </span>
                    </Button>
                  ))}
                </div>
              )}

            </div>
          )}
        </>
      )}
    </div>
  );
};

export default TeamTaskFiles;