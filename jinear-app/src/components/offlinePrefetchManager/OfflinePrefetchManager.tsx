import {useOnlineStatus} from "@/hooks/useOnlineStatus";
import {useAccountsPreferredWorkspaceIfLoggedIn} from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn";
import {selectCurrentAccountId, selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import {getDeviceProfile, type DeviceTier} from "@/util/deviceProfile";
import Logger from "@/util/logger";
import React, {useEffect} from "react";
import {useLocation} from "react-router-dom";
import {
    buildPrefetchRequest,
    buildPrefetchSeeds,
    seedPrefetchCache,
    type PrefetchSubscription
} from "./offlinePrefetchDescriptors";
import {workspacePrefetchApi} from "@/store/api/workspacePrefetchApi";

const logger = Logger("OfflinePrefetchManager");

// Keeps the prefetch burst clear of first paint and the initial route's own queries.
const START_DELAY_MS: Record<DeviceTier, number> = {high: 500, low: 4_000};

// Safari has no requestIdleCallback; fall back to a short timeout there.
const scheduleIdle = (fn: () => void): (() => void) => {
    if (typeof requestIdleCallback === "function") {
        const id = requestIdleCallback(fn, {timeout: 10_000});
        return () => cancelIdleCallback(id);
    }
    const id = setTimeout(fn, 200);
    return () => clearTimeout(id);
};

const OfflinePrefetchManager: React.FC = () => {
    const dispatch = useAppDispatch();
    const {pathname} = useLocation();
    const online = useOnlineStatus();
    const accountId = useTypedSelector(selectCurrentAccountId);
    const workspaceNameFromUrl = pathname.split("/")?.[1];
    const workspaceFromUrl = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceNameFromUrl));
    const preferredWorkspace = useAccountsPreferredWorkspaceIfLoggedIn();
    const workspaceId = (workspaceFromUrl ?? preferredWorkspace)?.workspaceId;

    useEffect(() => {
        if (!accountId || !workspaceId || !online) {
            return;
        }
        const {tier, saveData, slowNetwork} = getDeviceProfile();
        if (saveData || slowNetwork) {
            logger.log({message: "Skipping offline prefetch", saveData, slowNetwork});
            return;
        }
        let cancelled = false;
        const subscriptions: PrefetchSubscription[] = [];
        let cancelIdle: (() => void) | undefined;

        const seedCache = async () => {
            const request = buildPrefetchRequest({workspaceId, tier});
            const response = await dispatch(
                workspacePrefetchApi.endpoints.prefetchWorkspace.initiate(request, {track: false})
            ).unwrap();
            if (cancelled) {
                return;
            }
            const seeds = buildPrefetchSeeds({request, prefetch: response.data, accountId});
            subscriptions.push(...seedPrefetchCache(dispatch, seeds));
        };

        const start = () => {
            if (cancelled) {
                return;
            }
            cancelIdle = scheduleIdle(() => {
                if (cancelled) {
                    return;
                }
                logger.log({message: "Prefetching offline query set", workspaceId, tier});
                seedCache().catch(() => {
                    // Failed or went offline mid-flight; page hooks and refetchOnReconnect recover.
                });
            });
        };

        const startTimeoutId = setTimeout(start, START_DELAY_MS[tier]);

        return () => {
            cancelled = true;
            clearTimeout(startTimeoutId);
            cancelIdle?.();
            subscriptions.forEach((subscription) => subscription.unsubscribe());
        };
    }, [accountId, workspaceId, online, dispatch]);

    return null;
};

export default OfflinePrefetchManager;
