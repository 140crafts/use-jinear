import React, { useEffect, useState } from "react";
import OneSignal from "react-onesignal";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  const [oneSignalUserId, setOneSignalUserId] = useState<any>();

  useEffect(() => {
    getUserId();
  }, []);

  const getUserId = async () => {
    try {
      const userId = await OneSignal.getUserId();
      setOneSignalUserId(userId);
    } catch (error) {
      setOneSignalUserId(error);
    }
  };

  return <div className={styles.container}>{`oneSignalUserId: ${oneSignalUserId}`}</div>;
};

export default DebugScreen;
