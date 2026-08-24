import {selectCurrentAccountIsInstanceAdmin} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";

/**
 * Whether the current account holds the instance wide ADMIN role, which is what
 * v1/admin/** requires. Distinct from useWorkspaceRoleIsAdminOrOwner, which reads the
 * account's role inside a single workspace.
 */
export const useIsInstanceAdmin = () => useTypedSelector(selectCurrentAccountIsInstanceAdmin);

export default useIsInstanceAdmin;
