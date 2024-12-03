"use client";
import React, { useEffect } from "react";
import styles from "./index.module.css";
import { useAppDispatch } from "@/store/store";
import { changeLoadingModalVisibility, popProjectAndMilestonePickerModal } from "@/slice/modalSlice";
import { getUnreadConversationCount } from "../../repository/IndexedDbRepository";
import Button from "@/components/button";
import ClientOnly from "@/components/clientOnly/ClientOnly";

interface DebugScreenProps {
}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  const dispatch = useAppDispatch();

  const pop = () => {
    dispatch(popProjectAndMilestonePickerModal({ workspaceId: "01j7tgp8s9v74xvtm60hcabwt2", visible: true }));
  };

  return <div className={styles.container}>
    <Button onClick={pop}>pop</Button>
    <ClientOnly>
      {process.env.NEXT_PUBLIC_POSTHOG_HOST}
      <br/>
      {process.env.NEXT_PUBLIC_PROJECT_PAGES}
    </ClientOnly>
  </div>;
};

export default DebugScreen;
