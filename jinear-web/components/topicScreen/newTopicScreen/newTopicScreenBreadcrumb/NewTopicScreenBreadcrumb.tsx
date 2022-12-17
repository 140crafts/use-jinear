import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import {
  selectCurrentAccountsPreferredTeam,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface NewTopicScreenBreadcrumbProps {}

const NewTopicScreenBreadcrumb: React.FC<
  NewTopicScreenBreadcrumbProps
> = ({}) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const currentteam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <Breadcrumb>
      <BreadcrumbLink
        label={currentWorkspace?.title || ""}
        url={`/${currentWorkspace?.username}`}
      />
      <BreadcrumbLink
        label={currentteam?.name || ""}
        url={`/${currentWorkspace?.username}/${currentteam?.name}/topic/list`}
      />
      <BreadcrumbLink
        label={t("newTopicScreenTitle")}
        url={`/${currentWorkspace?.username}/${currentteam?.name}/topic/new`}
      />
    </Breadcrumb>
  );
};

export default NewTopicScreenBreadcrumb;
