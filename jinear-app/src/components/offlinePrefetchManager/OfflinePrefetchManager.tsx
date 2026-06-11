import {useOnlineStatus} from "@/hooks/useOnlineStatus";
import {useAccountsPreferredWorkspaceIfLoggedIn} from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn";
import {selectCurrentAccountId, selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import {teamApi} from "@/store/api/teamApi";
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

const STAGGER_MS = 150;

// Warms the persisted RTK Query cache with the canonical query set of the active
// workspace (tasks, calendar spans, files, inbox, activities) so default routes render
// offline even if they were never visited. Subscriptions are kept alive while the app
// runs: tag invalidations then refetch these entries instead of removing them.
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
        let cancelled = false;
        const subscriptions: PrefetchSubscription[] = [];
        const timeoutIds: ReturnType<typeof setTimeout>[] = [];

        const subscribeStaggered = (entries: PrefetchEntry[]) => {
            entries.forEach((prefetchEntry, index) => {
                timeoutIds.push(
                    setTimeout(() => {
                        if (!cancelled) {
                            subscriptions.push(prefetchEntry(dispatch));
                        }
                    }, index * STAGGER_MS)
                );
            });
        };

        logger.log({message: "Prefetching offline query set", workspaceId});
        subscribeStaggered(buildWorkspaceEntries({workspaceId, accountId}));

        const teamsSubscription = dispatch(teamApi.endpoints.retrieveWorkspaceTeams.initiate(workspaceId));
        subscriptions.push(teamsSubscription);
        teamsSubscription
            .unwrap()
            .then((teamsResponse) => {
                if (!cancelled) {
                    subscribeStaggered(buildTeamEntries({workspaceId, teams: teamsResponse.data ?? []}));
                }
            })
            .catch(() => {
                // Failed or went offline mid-flight; page hooks and refetchOnReconnect recover.
            });

        return () => {
            cancelled = true;
            timeoutIds.forEach(clearTimeout);
            subscriptions.forEach((subscription) => subscription.unsubscribe());
        };
    }, [accountId, workspaceId, online, dispatch]);

    return null;
};

export default OfflinePrefetchManager;
