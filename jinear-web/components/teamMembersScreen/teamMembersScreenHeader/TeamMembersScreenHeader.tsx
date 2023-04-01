import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import Button, { ButtonVariants } from "@/components/button";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamMembersScreenHeader.module.css";

interface TeamMembersScreenHeaderProps {}

const TeamMembersScreenHeader: React.FC<TeamMembersScreenHeaderProps> = ({}) => {
  const { t } = useTranslation();

  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const popInviteModal = () => {};

  return (
    <div className={styles.container}>
      <Breadcrumb>
        <BreadcrumbLink label={workspace?.title || ""} url={`/${workspace?.username || ""}`} />
        <BreadcrumbLink label={team?.name || ""} url={`/${workspace?.username || ""}/${team?.username || ""}`} />
        <BreadcrumbLink
          label={t("teamMemberScreenBreadcrumbTitle")}
          url={`/${workspace?.username || ""}/${team?.username || ""}/members`}
        />
      </Breadcrumb>
      <div className="spacer-h-4" />
      <div className={styles.actionBar}>
        <Button variant={ButtonVariants.contrast} onClick={popInviteModal}>
          {t("teamMemberScreenAddMember")}
        </Button>
      </div>
    </div>
  );
};

export default TeamMembersScreenHeader;
