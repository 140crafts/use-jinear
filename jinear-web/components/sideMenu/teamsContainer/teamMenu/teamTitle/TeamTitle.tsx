import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TitlePicture from "@/components/sideMenu/titlePicture/TitlePicture";
import { popTeamOptionsModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import React from "react";
import { IoEllipsisHorizontal } from "react-icons/io5";
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
      <TitlePicture initials={name.substring(0, 2)} />
      <div className={styles.teamName}>{name}</div>
      <div className="flex-1" />
      <Button
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
        onClick={_popTeamOptionsModal}
      >
        <IoEllipsisHorizontal />
      </Button>
    </div>
  );
};

export default TeamTitle;
