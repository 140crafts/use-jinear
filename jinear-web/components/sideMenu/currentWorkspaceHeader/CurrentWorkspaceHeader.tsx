import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import cn from "classnames";
import React from "react";
import { IoEllipsisHorizontal } from "react-icons/io5";
import TitlePicture from "../titlePicture/TitlePicture";
import styles from "./CurrentWorkspaceHeader.module.scss";

interface CurrentWorkspaceHeaderProps {}

const CurrentWorkspaceHeader: React.FC<CurrentWorkspaceHeaderProps> = ({}) => {
  const preferredWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  return (
    <div className={styles.container}>
      <TitlePicture
        initials={
          preferredWorkspace?.title?.substring(0, 2).toLocaleUpperCase() || ""
        }
      />
      <div className={cn(styles.title, "single-line")}>
        {preferredWorkspace?.title}
      </div>
      <div className="flex-1" />
      <Button
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
      >
        <IoEllipsisHorizontal />
      </Button>
    </div>
  );
};

export default CurrentWorkspaceHeader;
