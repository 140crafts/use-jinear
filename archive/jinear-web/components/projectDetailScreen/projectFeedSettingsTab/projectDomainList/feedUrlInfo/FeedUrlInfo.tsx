import React from "react";
import styles from "./FeedUrlInfo.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { copyTextToClipboard } from "@/utils/clipboard";
import { ProjectDomainDto } from "@/be/jinear-core";
import { LuCopy, LuHourglass, LuTrash } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { closeDialogModal, popDialogModal } from "@/slice/modalSlice";
import { useDeleteProjectDomainMutation } from "@/api/projectDomainApi";
import toast from "react-hot-toast";

interface FeedUrlInfoProps {
  projectDomainDto: ProjectDomainDto;
  hasExplicitAdminAccess?: boolean;
}

const FeedUrlInfo: React.FC<FeedUrlInfoProps> = ({ projectDomainDto, hasExplicitAdminAccess }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [deleteProjectDomain, { isLoading: isDeleteLoading }] = useDeleteProjectDomainMutation();
  const url = `https://${projectDomainDto.domain}`;

  const copyLink = () => {
    copyTextToClipboard(url);
  };

  const deleteCustomDomain = () => {
    deleteProjectDomain({ projectDomainId: projectDomainDto.projectDomainId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDelete = () => {
    dispatch(popDialogModal({
      visible: true,
      title: t("deleteProjectCustomDomainAreYouSureTitle"),
      content: t("deleteProjectCustomDomainAreYouSureText"),
      confirmButtonLabel: t("deleteProjectCustomDomainAreYouSureDeleteButton"),
      onConfirm: deleteCustomDomain
    }));
  };

  const popWaitingInfoToast = ()=>{
    toast(t("projectFeedUrlCustomDomainWaitingDNSInfo"));
  }

  return (
    <div className={styles.container}>
      <a className={styles.urlInput} href={url} target={"_blank"} rel={"noreferrer"}>{url}</a>
      {projectDomainDto.domainType == "CUSTOM" && projectDomainDto.cnameCheckResult != "SETUP_COMPLETED" &&
        <Button
          disabled={isDeleteLoading}
          loading={isDeleteLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled}
          data-tooltip-right={t("projectFeedUrlCustomDomainWaitingDNS")}
          onClick={popWaitingInfoToast}
        >
          <LuHourglass className={"icon"} />
        </Button>
      }
      <div className={styles.actionButtonContainer}>
        {hasExplicitAdminAccess && projectDomainDto.domainType != "AUTO_GENERATED" &&
          <Button
            disabled={isDeleteLoading}
            loading={isDeleteLoading}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.filled}
            onClick={popAreYouSureModalForDelete}
            data-tooltip-right={t("projectFeedUrlDelete")}>
            <LuTrash className={"icon"} />
          </Button>
        }
        <Button
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled}
          onClick={copyLink}
          data-tooltip-right={t("projectFeedUrlCopy")}>
          <LuCopy className={"icon"} />
        </Button>
      </div>
    </div>
  );
};

export default FeedUrlInfo;