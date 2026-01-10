import { useAccountsPreferredWorkspaceIfLoggedIn } from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";

export const useRouteIfLoggedIn = () => {
  const preferredWorkspace = useAccountsPreferredWorkspaceIfLoggedIn();
  return preferredWorkspace ? `/${preferredWorkspace?.username}` : ROUTE_IF_LOGGED_IN;
};