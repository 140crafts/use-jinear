import { selectCurrentAccountHasAPersonalWorkspace } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface NewWorkspaceScreenProps {}

const NewWorkspaceScreen: React.FC<NewWorkspaceScreenProps> = ({}) => {
  const dispatch = useAppDispatch();
  const hasPersonalWorkspace = useTypedSelector(selectCurrentAccountHasAPersonalWorkspace);

  //   useEffect(() => {
  //     setTimeout(() => {
  //       dispatch(changeLoadingModalVisibility({ visible: true }));
  //     }, 1000);
  //   }, []);

  return <div className={styles.container}>NewWorkspaceScreen-{"hasPersonalWorkspace :" + hasPersonalWorkspace}</div>;
};

export default NewWorkspaceScreen;
