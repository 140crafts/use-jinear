import NewWorkspaceForm from "@/components/form/newWorkspaceForm/NewWorkspaceForm";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectCurrentAccountHasAPersonalWorkspace } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface NewWorkspaceScreenProps {}

const NewWorkspaceScreen: React.FC<NewWorkspaceScreenProps> = ({}) => {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const hasPersonalWorkspace = useTypedSelector(selectCurrentAccountHasAPersonalWorkspace);

  const routeHome = () => {
    router.replace(ROUTE_IF_LOGGED_IN);
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>Create new workspace</div>
      <ThemeToggle />
      <div className={styles.formContainer}>
        <NewWorkspaceForm onSuccess={routeHome} />
      </div>
    </div>
  );
};

export default NewWorkspaceScreen;
