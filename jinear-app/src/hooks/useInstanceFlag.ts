import type {InstanceFlagType} from "@/model/be/jinear-core";
import {useRetrieveInstanceFlagsQuery} from "@/store/api/instanceFlagApi";

export const isInstanceFlagEnabled = (value: unknown): boolean =>
    value === true || `${value}`.toLowerCase() === "true";

export const useInstanceFlag = (flag: InstanceFlagType): boolean => {
    const {data} = useRetrieveInstanceFlagsQuery();
    return isInstanceFlagEnabled(data?.data?.[flag]);
};

export default useInstanceFlag;
