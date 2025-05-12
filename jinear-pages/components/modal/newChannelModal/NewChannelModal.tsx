import React, { useEffect } from "react";
import styles from "./NewChannelModal.module.scss";
import {
  changeLoadingModalVisibility,
  closeNewChannelModal,
  selectNewChannelModalVisible,
  selectNewChannelModalWorkspaceId
} from "@/slice/modalSlice";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useWindowSize from "@/hooks/useWindowSize";
import Modal from "@/components/modal/modal/Modal";
import { SubmitHandler, useForm } from "react-hook-form";
import { ChannelParticipationType, ChannelVisibilityType, InitializeChannelRequest } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import { useInitializeChannelMutation } from "@/api/channelApi";
import Button, { ButtonVariants } from "@/components/button";

interface NewChannelModalProps {

}

const logger = Logger("NewChannelModal");

const PARTICIPATION_TYPES: ChannelParticipationType[] = [
  "EVERYONE",
  "ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY",
  "READ_ONLY"
];

const VISIBILITY_TYPES: ChannelVisibilityType[] = [
  "EVERYONE",
  "MEMBERS_ONLY",
  "PUBLIC_WITH_GUESTS"
];

const NewChannelModal: React.FC<NewChannelModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const visible = useTypedSelector(selectNewChannelModalVisible);
  const workspaceId = useTypedSelector(selectNewChannelModalWorkspaceId);

  const [initializeChannel, {
    isLoading: isInitializeChannelLoading,
    isSuccess: isInitializeChannelSuccess
  }] = useInitializeChannelMutation();
  const { register, handleSubmit, setFocus, setValue, watch, reset } = useForm<InitializeChannelRequest>();
  const selectedParticipationType = watch("participationType");
  const selectedVisibilityType = watch("channelVisibilityType");

  useEffect(() => {
    if (workspaceId) {
      reset();
      setValue("workspaceId", workspaceId);
      setValue("participationType", PARTICIPATION_TYPES[0]);
      setValue("channelVisibilityType", VISIBILITY_TYPES[0]);
    }
  }, [reset, setValue, workspaceId]);

  const close = () => {
    dispatch(closeNewChannelModal());
  };

  const submit: SubmitHandler<InitializeChannelRequest> = (data) => {
    logger.log({ data });
    initializeChannel(data);
    close();
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "xxlarge"}
      title={t("newChannelModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
      containerClassName={styles.modalContainer}
    >
      <form autoComplete="off" id={"initialize-channel-form"} className={styles.form} onSubmit={handleSubmit(submit)}
            action="#">
        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <label className={styles.label} htmlFor={"new-channel-title"}>
          {t("newChannelModalFormChannelTitle")}
          <input id={"new-channel-title"} type={"text"} {...register("title")} />
        </label>

        <label className={styles.label} htmlFor={"new-channel-participation-type"}>
          {t("channelGeneralInfoChanelParticipation")}
          <select id="new-channel-participation-type" {...register("participationType")}>
            {PARTICIPATION_TYPES.map((pt) => (
              <option key={`new-channel-form-participation-type-${pt}`} value={pt}>
                {t(`channelGeneralInfoChanelParticipation_${pt}`)}
              </option>
            ))}
          </select>
          <div>
            {t(`channelGeneralInfoChanelParticipation_description_${selectedParticipationType}`)}
          </div>
        </label>

        <label className={styles.label} htmlFor={"new-channel-visibility-type"}>
          {t("channelGeneralInfoChanelVisibility")}
          <select id="new-channel-visibility-type" {...register("channelVisibilityType")}>
            {VISIBILITY_TYPES.map((vt) => (
              <option key={`new-channel-form-visibility-type-${vt}`} value={vt}>
                {t(`channelGeneralInfoChanelVisibility_${vt}`)}
              </option>
            ))}
          </select>
          <div>
            {t(`channelGeneralInfoChanelVisibility_description_${selectedVisibilityType}`)}
          </div>
        </label>

        <div className="spacer-h-1" />

        <Button
          disabled={isInitializeChannelLoading}
          loading={isInitializeChannelLoading}
          type="submit"
          className={styles.submitButton}
          variant={ButtonVariants.contrast}
        >
          <div>{t("newChannelModalFormSubmit")}</div>
        </Button>
      </form>
    </Modal>
  );
};

export default NewChannelModal;