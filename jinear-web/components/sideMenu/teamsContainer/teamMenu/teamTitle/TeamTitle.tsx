import Button, { ButtonVariants } from "@/components/button";
// import TitlePicture from "@/components/sideMenu/titlePicture/TitlePicture";
import { popTeamOptionsModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import React from "react";
import { IoChevronDownSharp } from "react-icons/io5";
import styles from "./TeamTitle.module.css";

interface TeamTitleProps {
  name: string;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ name }) => {
  const dispatch = useAppDispatch();

  const _popTeamOptionsModal = () => {
    dispatch(popTeamOptionsModal());
  };

  return (
    <div className={styles.teamNameContainer}>
      <Button
        variant={ButtonVariants.filled2}
        onClick={_popTeamOptionsModal}
        className={styles.teamSelectButton}
      >
        <div className={cn(styles.teamName, "single-line")}>{name}</div>
        <div className={styles.spacer} />
        <IoChevronDownSharp className={styles.icon} size={17} />
      </Button>
    </div>
  );
};

export default TeamTitle;
