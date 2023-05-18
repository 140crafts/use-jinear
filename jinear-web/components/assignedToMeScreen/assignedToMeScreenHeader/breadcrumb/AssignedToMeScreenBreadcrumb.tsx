import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface AssignedToMeScreenBreadcrumbProps {}

const AssignedToMeScreenBreadcrumb: React.FC<AssignedToMeScreenBreadcrumbProps> = ({}) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  return (
    <Breadcrumb>
      <BreadcrumbLink label={currentWorkspace?.title || ""} url={`/${currentWorkspace?.username || ""}`} />
      <BreadcrumbLink
        label={t("assignedToMeScreenBreadcrumbLabel")}
        url={`/${currentWorkspace?.username || ""}/assigned-to-me`}
      />
    </Breadcrumb>
  );
};

export default AssignedToMeScreenBreadcrumb;
