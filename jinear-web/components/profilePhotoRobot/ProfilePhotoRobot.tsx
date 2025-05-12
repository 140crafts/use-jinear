import React from "react";
import styles from "./ProfilePhotoRobot.module.css";
import { BsRobot } from "react-icons/bs";
import cn from "classnames";

interface ProfilePhotoRobotProps {
  wrapperClassName?: string;
}

const ProfilePhotoRobot: React.FC<ProfilePhotoRobotProps> = ({ wrapperClassName }) => {

  return (
    <div className={cn(styles.wrapper, wrapperClassName)}>
      <BsRobot className={styles.image} size={21}/>
    </div>
  );
};

export default ProfilePhotoRobot;