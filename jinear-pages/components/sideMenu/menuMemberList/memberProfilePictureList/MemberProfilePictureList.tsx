import ProfilePhoto from "@/components/profilePhoto";
import { AccountDto } from "@/model/be/jinear-core";
import { motion, usePresence } from "framer-motion";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./MemberProfilePictureList.module.css";

interface MemberProfilePictureListProps {
  accountList: AccountDto[];
  type: "workspace" | "team";
}

const SLICE_SIZE = 5;

const MemberProfilePictureList: React.FC<MemberProfilePictureListProps> = ({ accountList, type }) => {
  const { t } = useTranslation();
  const remainingCount = accountList.length - SLICE_SIZE;
  const [isPresent, safeToRemove] = usePresence();

  const animations = {
    layout: true,
    initial: "out",
    animate: isPresent ? "in" : "out",
    variants: {
      in: {
        scale: 1,
        opacity: 1,
        zIndex: "unset"
      },
      out: {
        scale: 1,
        opacity: 0,
        zIndex: -1,
        transition: { duration: 0 }
      }
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isPresent && safeToRemove()
  };

  const moreButtonLabel = t("sideMenuWorkspaceMembersMore").replace("${number}", `${remainingCount}`);

  return (
    <motion.div {...animations} className={styles.memberProfilePicList}>
      {accountList?.slice(0, SLICE_SIZE)?.map?.((account, index) => (
        <div
          key={`${type}-member-profile-list-account-${account.accountId}`}
          data-tooltip={index < SLICE_SIZE / 2 ? account?.username : undefined}
          data-tooltip-right={index >= SLICE_SIZE / 2 ? account?.username : undefined}
          style={{
            display: "flex",
            alignItems: "center",
            marginLeft: index != 0 ? -10 : 0,
            zIndex: index
          }}
        >
          <ProfilePhoto
            boringAvatarKey={account.accountId}
            url={account?.profilePicture?.url}
            wrapperClassName={styles.profilePic}
          />
        </div>
      ))}

      {remainingCount > 0 && <div className={styles.moreButton}>{moreButtonLabel}</div>}
    </motion.div>
  );
};

export default MemberProfilePictureList;
