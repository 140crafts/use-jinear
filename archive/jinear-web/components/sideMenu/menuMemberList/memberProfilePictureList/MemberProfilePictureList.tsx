import ProfilePhoto from "@/components/profilePhoto";
import { AccountDto, PlainAccountProfileDto } from "@/model/be/jinear-core";
import { motion, usePresence } from "framer-motion";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./MemberProfilePictureList.module.css";
import cn from "classnames";

interface MemberProfilePictureListProps {
  accountList: AccountDto[] | PlainAccountProfileDto[];
  type: "workspace" | "team";
  profilePicClassname?: string;
  sliceSize?:number
}

const MemberProfilePictureList: React.FC<MemberProfilePictureListProps> = ({
                                                                             accountList,
                                                                             type,
                                                                             profilePicClassname,
                                                                             sliceSize = 5
                                                                           }) => {
  const { t } = useTranslation();
  const remainingCount = accountList.length - sliceSize;
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
      {accountList?.slice(0, sliceSize)?.map?.((account, index) => (
        <div
          key={`${type}-member-profile-list-account-${account.accountId}`}
          data-tooltip={index < sliceSize / 2 ? account?.username : undefined}
          data-tooltip-right={index >= sliceSize / 2 ? account?.username : undefined}
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
            wrapperClassName={cn(styles.profilePic, profilePicClassname)}
          />
        </div>
      ))}

      {remainingCount > 0 && <div className={styles.moreButton}>{moreButtonLabel}</div>}
    </motion.div>
  );
};

export default MemberProfilePictureList;
