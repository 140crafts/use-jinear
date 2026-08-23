import type {InstanceFlagType} from "@/model/be/jinear-core";
import {useRetrieveInstanceFlagsQuery} from "@/store/api/instanceFlagApi";

/**
 * Reads a per instance feature flag served by v1/instance-flag/list.
 *
 * A flag counts as enabled only on an explicit true. While the request is in
 * flight, when it fails (older backend without the endpoint) and when the key is
 * missing from the response the flag resolves to false, so the app never offers an
 * affordance the backend would reject.
 *
 * Not to be confused with useFeatureFlag, which is a client only localStorage toggle.
 */
export const useInstanceFlag = (flag: InstanceFlagType): boolean => {
    const {data} = useRetrieveInstanceFlagsQuery();
    const value = data?.data?.[flag];
    return value === true || `${value}`.toLowerCase() === "true";
};

export default useInstanceFlag;
