"use client";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import { useDeleteFeedMutation } from "@/store/api/feedApi";
import { changeLoadingModalVisibility, closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./DeleteFeedCard.module.css";

interface DeleteFeedCardProps {
  feedId: string;
}

const DeleteFeedCard: React.FC<DeleteFeedCardProps> = ({ feedId }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();
  const [deleteFeed, { isLoading, isSuccess }] = useDeleteFeedMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isLoading }));
    if (isSuccess) {
      router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [isLoading, isSuccess]);

  const popAreYouSure = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteFeedCardAreYouSureTitle"),
        content: t("deleteFeedCardAreYouSureText"),
        confirmButtonLabel: t("deleteFeedButton"),
        onConfirm: deleteThisFeed,
      })
    );
  };

  const deleteThisFeed = () => {
    deleteFeed({ feedId });
    dispatch(closeDialogModal());
  };

  return (
    <div className={styles.container}>
      <SectionTitle title={t("deleteFeedCardTitle")} description={t("deleteFeedCardText")} />
      <div className={styles.actionContainer}>
        <Button
          onClick={popAreYouSure}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          disabled={isLoading}
          loading={isLoading}
        >
          {t("deleteFeedButton")}
        </Button>
      </div>
    </div>
  );
};

export default DeleteFeedCard;
