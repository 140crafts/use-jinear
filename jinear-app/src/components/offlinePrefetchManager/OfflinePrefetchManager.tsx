import {useOnlineStatus} from "@/hooks/useOnlineStatus";
import {useAccountsPreferredWorkspaceIfLoggedIn} from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn";
import {selectCurrentAccountId, selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import {teamApi} from "@/store/api/teamApi";
import {getDeviceProfile, type DeviceTier} from "@/util/deviceProfile";
import Logger from "@/util/logger";
import React, {useEffect} from "react";
import {useLocation} from "react-router-dom";
import {
    buildTeamEntries,
    buildWorkspaceEntries,
    type PrefetchEntry,
    type PrefetchSubscription
} from "./offlinePrefetchDescriptors";

const logger = Logger("OfflinePrefetchManager");

const STAGGER_MS: Record<DeviceTier, number> = {high: 150, low: 600};
// Keeps the prefetch burst clear of first paint and the initial route's own queries.
const START_DELAY_MS: Record<DeviceTier, number> = {high: 750, low: 4_000};

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
        const cancelFns: (() => void)[] = [];

        const subscribeStaggered = (entries: PrefetchEntry[]) => {
            entries.forEach((prefetchEntry, index) => {
                const timeoutId = setTimeout(() => {
                    cancelFns.push(
                        scheduleIdle(() => {
                            if (!cancelled) {
                                subscriptions.push(prefetchEntry(dispatch));
                            }
                        })
                    );
                }, index * STAGGER_MS[tier]);
                cancelFns.push(() => clearTimeout(timeoutId));
            });
        };

        const start = () => {
            if (cancelled) {
                return;
            }
            logger.log({message: "Prefetching offline query set", workspaceId, tier});
            subscribeStaggered(buildWorkspaceEntries({workspaceId, accountId, tier}));

            const teamsSubscription = dispatch(teamApi.endpoints.retrieveWorkspaceTeams.initiate(workspaceId));
            subscriptions.push(teamsSubscription);
            teamsSubscription
                .unwrap()
                .then((teamsResponse) => {
                    if (!cancelled) {
                        subscribeStaggered(buildTeamEntries({workspaceId, teams: teamsResponse.data ?? [], tier}));
                    }
                })
                .catch(() => {
                    // Failed or went offline mid-flight; page hooks and refetchOnReconnect recover.
                });
        };

        const startTimeoutId = setTimeout(start, START_DELAY_MS[tier]);

        return () => {
            cancelled = true;
            clearTimeout(startTimeoutId);
            cancelFns.forEach((cancel) => cancel());
            subscriptions.forEach((subscription) => subscription.unsubscribe());
        };
    }, [accountId, workspaceId, online, dispatch]);

    return null;
};

export default OfflinePrefetchManager;
