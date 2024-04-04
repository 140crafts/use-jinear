import React from "react";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      {process.env.NEXT_PUBLIC_AXIOM_DATASET}
      <br></br>
      {process.env.NEXT_PUBLIC_AXIOM_TOKEN?.substring?.(0, 4)}
    </div>
  );
};

export default DebugScreen;
