import React from "react";
import styles from "./ChannelMembers.module.css";
import { useRetrieveChannelMembersQuery } from "@/api/channelMemberApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import ProfilePhoto from "@/components/profilePhoto";
import useTranslation from "@/locals/useTranslation";
import { motion, usePresence } from "framer-motion";
import Button from "@/components/button";

interface ChannelMembersProps {
  channelId: string;
}

const SLICE_SIZE = 3;
const ChannelMembers: React.FC<ChannelMembersProps> = ({ channelId }) => {
  const { t } = useTranslation();
  const { data: retrieveChannelMembersResponse, isFetching } = useRetrieveChannelMembersQuery({ channelId });
  const remainingCount = (retrieveChannelMembersResponse?.data?.length || 0) - SLICE_SIZE;
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

  const moreButtonLabel = t("channelHeaderMembersMore").replace("${number}", `${remainingCount}`);

  return (
    <motion.div {...animations} className={styles.memberProfilePicList}>
      {isFetching && <CircularLoading />}
      {retrieveChannelMembersResponse?.data
        ?.slice(0, SLICE_SIZE)
        ?.map(member => member.account)
        ?.map((account, index) =>
          <div
            key={`channel-${channelId}-channel-member-profile-list-account-${account?.accountId}`}
            data-tooltip-right={account?.username}
            style={{
              display: "flex",
              alignItems: "center",
              marginLeft: index != 0 ? -10 : 0,
              zIndex: index
            }}
          >
            <ProfilePhoto
              boringAvatarKey={account?.accountId || ""}
              url={account?.profilePicture?.url}
              wrapperClassName={styles.profilePic}
            />
          </div>
        )}
      {remainingCount > 0 && <div className={styles.moreButton}>{moreButtonLabel}</div>}
    </motion.div>
  );
};

export default ChannelMembers;