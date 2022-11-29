import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import {
  PageDto,
  TeamMemberDto,
  WorkspaceMemberDto,
} from "@/model/be/jinear-core";
import { motion, usePresence } from "framer-motion";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd } from "react-icons/io5";
import styles from "./MenuMemberList.module.css";

interface MenuMemberListProps {
  page: PageDto<WorkspaceMemberDto> | PageDto<TeamMemberDto> | PageDto<any>;
  type: "workspace" | "team";
}

const SLICE_SIZE = 5;

const MenuMemberList: React.FC<MenuMemberListProps> = ({ page, type }) => {
  const { t } = useTranslation();
  const [isPresent, safeToRemove] = usePresence();

  const animations = {
    layout: true,
    initial: "out",
    animate: isPresent ? "in" : "out",
    variants: {
      in: {
        scale: 1,
        opacity: 1,
        zIndex: "unset",
      },
      out: {
        scale: 1,
        opacity: 0,
        zIndex: -1,
        transition: { duration: 0 },
      },
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isPresent && safeToRemove(),
  };

  const totalElements = page?.totalElements || 0;
  const remainingCount = totalElements - SLICE_SIZE;
  const moreButtonLabel = t("sideMenuWorkspaceMembersMore").replace(
    "${number}",
    `${remainingCount}`
  );

  return (
    <div className={styles.contentContainer}>
      <motion.div {...animations} className={styles.memberProfilePicList}>
        {page?.content?.slice(0, SLICE_SIZE)?.map?.((member, index) => (
          <div
            key={member.workspaceMemberId || member.teamMemberId}
            data-tooltip={member?.account?.username}
            style={{
              display: "flex",
              alignItems: "center",
              marginLeft: index != 0 ? -10 : 0,
              zIndex: index,
            }}
          >
            <ProfilePhoto
              boringAvatarKey={member.accountId}
              storagePath={member.accountDto?.profilePicture?.storagePath}
              wrapperClassName={styles.profilePic}
            />
          </div>
        ))}

        {remainingCount > 0 && (
          <Button
            variant={ButtonVariants.hoverFilled2}
            className={styles.moreButton}
            heightVariant={ButtonHeight.short}
          >
            {moreButtonLabel}
          </Button>
        )}
      </motion.div>
      <Button
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
      >
        <IoAdd />
      </Button>
    </div>
  );
};

export default MenuMemberList;
