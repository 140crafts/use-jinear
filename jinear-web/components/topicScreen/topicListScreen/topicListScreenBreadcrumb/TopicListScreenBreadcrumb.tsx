import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TopicListScreenBreadcrumbProps {}

const TopicListScreenBreadcrumb: React.FC<TopicListScreenBreadcrumbProps> = ({}) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const currentteam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <Breadcrumb>
      <BreadcrumbLink label={currentWorkspace?.title || ""} url={`/${currentWorkspace?.username}`} />
      <BreadcrumbLink label={currentteam?.name || ""} url={`/${currentWorkspace?.username}/${currentteam?.name}`} />
      <BreadcrumbLink label={t("topicListScreenTitle")} url={`/${currentWorkspace?.username}/${currentteam?.name}/topic/list`} />
    </Breadcrumb>
  );
};

export default TopicListScreenBreadcrumb;
