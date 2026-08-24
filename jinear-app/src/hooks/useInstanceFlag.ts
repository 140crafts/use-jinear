import type {InstanceFlagType} from "@/model/be/jinear-core";
import {useRetrieveInstanceFlagsQuery} from "@/store/api/instanceFlagApi";

/**
 * Reads one raw value out of v1/instance-flag/list as a boolean.
 *
 * The backend stores flag values as text and serializes them untyped, so a flag can arrive
 * as either a real boolean or the string "true". Anything else, including a missing key,
 * counts as disabled.
 */
export const isInstanceFlagEnabled = (value: unknown): boolean =>
    value === true || `${value}`.toLowerCase() === "true";

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
    return isInstanceFlagEnabled(data?.data?.[flag]);
};

export default useInstanceFlag;
