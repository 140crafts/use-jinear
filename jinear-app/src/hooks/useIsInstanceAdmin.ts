import {selectCurrentAccountIsInstanceAdmin} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";

export const useIsInstanceAdmin = () => useTypedSelector(selectCurrentAccountIsInstanceAdmin);

export default useIsInstanceAdmin;
