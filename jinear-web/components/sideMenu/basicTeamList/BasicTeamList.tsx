import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { popNewTeamModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import MenuGroupTitle from "../menuGroupTitle/MenuGroupTitle";
import styles from "./BasicTeamList.module.css";
import BasicTeamMenu from "./basicTeamMenu/BasicTeamMenu";

interface BasicTeamListProps {}

const BasicTeamList: React.FC<BasicTeamListProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const {
    data: teamsResponse,
    isSuccess,
    isFetching,
  } = useRetrieveWorkspaceTeamsQuery(preferredWorkspace?.workspaceId || "", {
    skip: preferredWorkspace == null,
  });

  const openNewTeamModal = () => {
    dispatch(popNewTeamModal());
  };

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <MenuGroupTitle label={t("sideMenuTeamsTitle")} hasAddButton={true} onAddButtonClick={openNewTeamModal} />
      {isFetching && <CircularLoading />}
      <div className="spacer-h-1" />
      <div className={styles.teamListContainer}>
        {preferredWorkspace &&
          teamsResponse?.data.map((teamDto) => (
            <BasicTeamMenu key={`basic-team-menu-${teamDto.teamId}`} team={teamDto} workspace={preferredWorkspace} />
          ))}
      </div>
    </div>
  );
};

export default BasicTeamList;
