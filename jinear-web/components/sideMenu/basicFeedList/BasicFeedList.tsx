import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveFeedMembershipsQuery } from "@/store/api/feedMemberApi";
import { popNewMailIntegrationModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuMailCheck } from "react-icons/lu";
import MenuGroupTitle from "../menuGroupTitle/MenuGroupTitle";
import styles from "./BasicFeedList.module.css";
import FeedMenu from "./feedMenu/FeedMenu";

interface BasicFeedListProps {
  workspace: WorkspaceDto;
}

const BasicFeedList: React.FC<BasicFeedListProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { data: feedMembershipsResponse, isFetching } = useRetrieveFeedMembershipsQuery({ workspaceId: workspace.workspaceId });

  const popNewIntegrationModal = () => {
    dispatch(popNewMailIntegrationModal({ workspaceId: workspace?.workspaceId, visible: true }));
  };

  return (
    <div className={styles.container}>
      <div className="spacer-h-1" />
      <MenuGroupTitle label={t("sideMenuFeedsTitle")} hasAddButton={false} />
      {isFetching && <CircularLoading />}
      <div className="spacer-h-1" />
      {feedMembershipsResponse?.data.map((feedMember) => (
        <FeedMenu key={feedMember.feedMemberId} feedMember={feedMember} workspace={workspace} />
      ))}
      <Button
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.hoverFilled2}
        className={styles.addMailIntegrationButton}
        onClick={popNewIntegrationModal}
      >
        <LuMailCheck />
        {t("sideMenuAddMailIntegrationLabel")}
      </Button>
    </div>
  );
};

export default BasicFeedList;
