import Button, { ButtonVariants } from "@/components/button";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
// import TitlePicture from "@/components/sideMenu/titlePicture/TitlePicture";
import { popTeamOptionsModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import {
  IoChevronDownSharp,
  IoEllipsisHorizontal,
  IoHome,
} from "react-icons/io5";
import styles from "./TeamTitle.module.css";
interface TeamTitleProps {
  name: string;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ name }) => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const preferredWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );

  const _popTeamOptionsModal = () => {
    dispatch(popTeamOptionsModal());
  };

  const routeTeamSettings = () => {
    router.push(`/${preferredWorkspace?.username}/${name}/settings`);
  };

  const routeTeamHome = () => {
    router.push(`/${preferredWorkspace?.username}/${name}`);
  };

  return (
    <div className={styles.teamNameContainer}>
      <Button variant={ButtonVariants.hoverFilled2} onClick={routeTeamHome}>
        <IoHome />
      </Button>
      <Button
        variant={ButtonVariants.filled2}
        onClick={_popTeamOptionsModal}
        className={styles.teamSelectButton}
      >
        <div className={styles.teamName}>{name}</div>
        <div className="flex-1" />
        <IoChevronDownSharp size={17} />
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} onClick={routeTeamSettings}>
        <IoEllipsisHorizontal size={17} />
      </Button>
    </div>
  );
};

export default TeamTitle;
