import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface InboxScreenBreadcrumbsProps {}

const InboxScreenBreadcrumbs: React.FC<InboxScreenBreadcrumbsProps> = ({}) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const workspaceTitle = currentWorkspace?.title || "";
  const workspaceUsername = currentWorkspace?.username || "";

  return currentWorkspace?.isPersonal ? null : (
    <Breadcrumb>
      <BreadcrumbLink label={workspaceTitle} url={`/${workspaceUsername}`} />
      <BreadcrumbLink label={t("inboxScreenBreadcrumbLabel")} url={`/${workspaceUsername}/inbox`} />
    </Breadcrumb>
  );
};

export default InboxScreenBreadcrumbs;
