import { useAccountsFirstWorkspace } from "@/hooks/useAccountsFirstWorkspace";
import { useTypedSelector } from "@/store/store";

export const useAccountsPreferredWorkspaceIfLoggedIn = () => {
  const accountsFirstWorkspace = useAccountsFirstWorkspace();
  const preferedWorkspace = useTypedSelector((state) => state.account.current?.workspaceDisplayPreference?.workspace);
  return preferedWorkspace ? preferedWorkspace : accountsFirstWorkspace;
};