import Button from "@/components/button";
import { TeamDto } from "@/model/be/jinear-core";
import { selectTeamPickerModalOnPick } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./BasicTeamButton.module.css";

interface BasicTeamButtonProps {
  team: TeamDto;
  close: () => void;
}

const BasicTeamButton: React.FC<BasicTeamButtonProps> = ({ team, close }) => {
  const onPick = useTypedSelector(selectTeamPickerModalOnPick);

  const onClick = () => {
    onPick?.(team);
    close();
  };

  return (
    <Button onClick={onClick} className={styles.container}>
      {team.name}
    </Button>
  );
};

export default BasicTeamButton;
