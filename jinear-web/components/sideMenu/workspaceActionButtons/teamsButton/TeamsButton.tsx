import React from "react";
import styles from "./TeamsButton.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuUser } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import { WorkspaceDto } from "@/be/jinear-core";
import { usePathname } from "next/navigation";

interface TeamsButtonProps {
  workspace: WorkspaceDto;
}

const TeamsButton: React.FC<TeamsButtonProps> = ({ workspace }) => {
  const { t } = useTranslation();

  const pathname = usePathname();
  const teamsPath = `/${workspace.username}/tasks/teams`;

  return (
    <Button className={styles.button}
            heightVariant={ButtonHeight.short}
            variant={pathname == teamsPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            href={teamsPath}>
      <LuUser />
      {t("sideMenuTeamsTitle")}
    </Button>
  );
};

export default TeamsButton;