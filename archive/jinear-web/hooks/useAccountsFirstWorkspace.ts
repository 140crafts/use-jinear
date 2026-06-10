import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";

export const useAccountsFirstWorkspace = () => {
  return useTypedSelector(selectCurrentAccountsWorkspaces)?.[0];
};
