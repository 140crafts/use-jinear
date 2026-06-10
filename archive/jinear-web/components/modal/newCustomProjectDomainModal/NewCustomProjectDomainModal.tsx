import React, { useEffect } from "react";
import styles from "./NewCustomProjectDomainModal.module.css";
import Modal from "@/components/modal/modal/Modal";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import {
  closeNewCustomProjectDomainModal,
  selectNewCustomProjectDomainModalProjectId,
  selectNewCustomProjectDomainModalVisible
} from "@/slice/modalSlice";
import { SubmitHandler, useForm } from "react-hook-form";
import { useInitializeProjectDomainMutation } from "@/api/projectDomainApi";
import { ProjectDomainInitializeRequest } from "@/be/jinear-core";
import { isValidDomainName } from "@/utils/validator";
import toast from "react-hot-toast";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";

interface NewCustomProjectDomainModalProps {

}

const NewCustomProjectDomainModal: React.FC<NewCustomProjectDomainModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewCustomProjectDomainModalVisible);
  const projectId = useTypedSelector(selectNewCustomProjectDomainModalProjectId);
  const [initializeProjectDomain, { isLoading: isInitializeLoading }] = useInitializeProjectDomainMutation();
  const {
    register,
    setValue,
    setFocus,
    reset,
    handleSubmit,
    formState: { errors }
  } = useForm<ProjectDomainInitializeRequest>();

  useEffect(() => {
    projectId && setValue("projectId", projectId);
  }, [setValue, projectId]);

  useEffect(() => {
    if (visible) {
      setTimeout(() => {
        setFocus("domain");
      }, 500);
    }
  }, [visible]);

  const close = () => {
    reset();
    dispatch(closeNewCustomProjectDomainModal());
  };

  const submit: SubmitHandler<ProjectDomainInitializeRequest> = (data) => {
    if (!isValidDomainName(data.domain)) {
      toast(t("newCustomProjectDomainModalDomainNotValid"));
      return;
    }
    initializeProjectDomain({ ...data });
    close();
  };

  return (
    <Modal
      visible={visible}
      title={t("newCustomProjectDomainModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <div className={styles.container}>
        <span>{t("newCustomProjectDomainModalText")}</span>

        <form autoComplete="off" id={"new-project-domain-form"} className={styles.form}
              onSubmit={handleSubmit(submit)} action="#">
          <input type={"hidden"} value={projectId} {...register("projectId")} />
          <label className={styles.label} htmlFor={"new-workspace-handle"}>
            {t("newCustomProjectDomainModalDomainLabel")}
            <div className={styles.urlInputsContainer}>
              <div className={styles.schemeLabel}>https://</div>
              <input
                className={styles.domainInput}
                type={"text"}
                minLength={1}
                maxLength={255}
                {...register("domain", { required: t("newEventFormTitleRequiredField") })}
              />
            </div>
          </label>

          <span>
               <a href={`https://forum.jinear.co/post/01jfn48prw90x0bt86yd4pdyje`}
                  target={"_blank"} rel={"noopener noreferrer"}>{t("newCustomProjectDomainModalBlogPostCheckOut")}</a>
            </span>

          <Button
            disabled={isInitializeLoading}
            loading={isInitializeLoading}
            type="submit"
            variant={ButtonVariants.contrast}
          >
            <div>{t("newCustomProjectDomainModalAddDomain")}</div>
          </Button>
        </form>
      </div>
    </Modal>
  );
};

export default NewCustomProjectDomainModal;