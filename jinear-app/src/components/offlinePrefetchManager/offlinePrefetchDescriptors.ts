import {
    computeMonthViewPeriod,
    computeTwoDayViewPeriod,
    computeWeekViewPeriod,
    type ICalendarViewPeriod
} from "@/components/calendar/calendarUtils";
import type {ExtendedTaskFilterRequest} from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import type {
    TaskFilterRequest,
    WorkspacePrefetchDto,
    WorkspacePrefetchMaterialFilterRequest,
    WorkspacePrefetchRequest,
    WorkspacePrefetchTeamDto
} from "@/model/be/jinear-core";
import type {AppDispatch} from "@/store";
import type {DeviceTier} from "@/util/deviceProfile";
import {api} from "@/store/api/api";
import {calendarEventApi} from "@/store/api/calendarEventApi";
import {calendarMemberApi} from "@/store/api/calendarMemberApi";
import {materialListingApi} from "@/store/api/materialListingApi";
import {noteFilterApi} from "@/store/api/noteFilterApi";
import {notebookListingApi} from "@/store/api/notebookListingApi";
import {notificationEventApi} from "@/store/api/notificationEventApi";
import {taskListingApi} from "@/store/api/taskListingApi";
import {taskMediaApi} from "@/store/api/taskMediaApi";
import {teamApi} from "@/store/api/teamApi";
import {teamMemberApi} from "@/store/api/teamMemberApi";
import {teamWorkflowStatusApi} from "@/store/api/teamWorkflowStatusApi";
import {topicListingApi} from "@/store/api/topicListingApi";
import {workspaceActivityApi} from "@/store/api/workspaceActivityApi";
import {workspaceMediaApi} from "@/store/api/workspaceMediaApi";
import {addDays, addMonths, addWeeks, startOfDay, startOfMonth, startOfWeek} from "date-fns";

export interface PrefetchSubscription {
    unsubscribe: () => void;
}

export interface PrefetchSeed {
    entry: { endpointName: string; arg: unknown; value: unknown };
    subscribe: (dispatch: AppDispatch) => PrefetchSubscription;
}

interface SeedableEndpoint<TArg, TResult> {
    name: string;
    initiate: (arg: TArg) => any;
    Types: { QueryArg: TArg; ResultType: TResult };
}

const seed = <TArg, TResult>(
    endpoint: SeedableEndpoint<TArg, TResult>,
    arg: TArg,
    data: TResult | null | undefined
): PrefetchSeed[] =>
    data
        ? [{
            entry: {endpointName: endpoint.name, arg, value: data},
            subscribe: (dispatch) => dispatch(endpoint.initiate(arg))
        }]
        : [];

// Upsert all entries in one action so seeding costs one store update and one render pass.
// Subscribing afterwards finds fresh data and skips the network; the subscription keeps the
// entry alive through tag invalidation, which drops unsubscribed entries.
export const seedPrefetchCache = (dispatch: AppDispatch, seeds: PrefetchSeed[]): PrefetchSubscription[] => {
    dispatch(api.util.upsertQueryEntries(seeds.map(({entry}) => entry) as never));
    return seeds.map(({subscribe}) => subscribe(dispatch));
};

// Args must serialize to the exact same cache keys as the page components' queries.
// undefined fields drop out of the key, null fields do NOT (useQueryState yields null
// for absent params, so page filters carry explicit nulls).

// Calendar.tsx views default viewingDate to startOfDay(today); prev/next viewing dates
// mirror CalendarHeader navigation steps per view type.
const calendarViewingDates = (today: Date) => ({
    month: [addMonths(startOfMonth(today), -1), today, addMonths(startOfMonth(today), 1)],
    // Week periods also serve DayView, which fetches the whole containing week.
    week: [addWeeks(startOfWeek(today, {weekStartsOn: 1}), -1), today, addWeeks(startOfWeek(today, {weekStartsOn: 1}), 1)],
    twoDay: [addDays(today, -2), today, addDays(today, 2)]
});

const calendarPeriods = (tier: DeviceTier): ICalendarViewPeriod[] => {
    const today = startOfDay(new Date());
    const viewingDates = calendarViewingDates(today);
    // Low tier keeps only the period each view opens on; prev/next warmth is skipped.
    const pick = (dates: Date[]) => (tier === "low" ? [today] : dates);
    return [
        ...pick(viewingDates.month).map(computeMonthViewPeriod),
        ...pick(viewingDates.week).map(computeWeekViewPeriod),
        ...pick(viewingDates.twoDay).map(computeTwoDayViewPeriod)
    ];
};

// Root folder + the FilesSectionSideMenu quick filters (recent/images/documents/shared).
const materialFilters: WorkspacePrefetchMaterialFilterRequest[] = [
    {materialSearchSortType: null, materialSearchContentFilterType: null},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: null},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: "IMAGE"},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: "DOC"},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: "SHARED"}
];

// Low tier warms only the first few teams and skips their file listings; the team
// task list and workflow statuses stay because they back the default team route.
const LOW_TIER_TEAM_LIMIT = 3;

export const buildPrefetchRequest = ({workspaceId, tier = "high"}: {
    workspaceId: string;
    tier?: DeviceTier;
}): WorkspacePrefetchRequest => ({
    workspaceId,
    calendarTimespans: calendarPeriods(tier).map(({periodStart, periodEnd}) => ({
        timespanStart: periodStart,
        timespanEnd: periodEnd
    })),
    // Low tier warms only the root folder, the quick filters load on demand.
    materialFilters: tier === "low" ? materialFilters.slice(0, 1) : materialFilters,
    teamLimit: tier === "low" ? LOW_TIER_TEAM_LIMIT : null,
    includeTeamTaskMedia: tier !== "low"
});

const buildTeamSeeds = (workspaceId: string, team: WorkspacePrefetchTeamDto): PrefetchSeed[] => [
    // /tasks/:teamUsername/tasks (MultiViewTaskList); nulls mirror useQueryState defaults
    ...seed<ExtendedTaskFilterRequest, typeof team.tasks>(taskListingApi.endpoints.filterTasks, {
        page: 0,
        workspaceId,
        teamIdList: [team.teamId],
        topicIds: null,
        ownerIds: null,
        assigneeIds: null,
        collaboratorIds: null,
        workflowStatusIdList: null,
        workflowStateGroups: null,
        timespanStart: null,
        timespanEnd: null,
        hasPreciseFromDate: null,
        hasPreciseToDate: null
    }, team.tasks),
    ...seed(teamWorkflowStatusApi.endpoints.retrieveAllFromTeam, {teamId: team.teamId}, team.workflowStatuses),
    // team task list filter bar (assignee/owner/collaborator + topic pickers)
    ...seed(teamMemberApi.endpoints.retrieveTeamMembers, {teamId: team.teamId}, team.members),
    ...seed(topicListingApi.endpoints.searchTeamTopics, {teamId: team.teamId, nameOrTag: ""}, team.topics),
    // /tasks/:teamUsername/files (TeamFileList)
    ...seed(taskMediaApi.endpoints.retrieveTaskMediaListFromTeam, {teamId: team.teamId, page: 0}, team.taskMedia)
];

export const buildPrefetchSeeds = ({request, prefetch, accountId}: {
    request: WorkspacePrefetchRequest;
    prefetch: WorkspacePrefetchDto;
    accountId: string;
}): PrefetchSeed[] => {
    const {workspaceId} = request;
    return [
        // /tasks (WorkspaceTasksPage) redirects to the first team once this resolves
        ...seed(teamApi.endpoints.retrieveWorkspaceTeams, workspaceId, prefetch.workspaceTeams),
        ...seed(teamMemberApi.endpoints.retrieveMemberships, {workspaceId}, prefetch.teamMemberships),
        // /tasks/last-activities (LastWorkspaceActivitiesList)
        ...seed(workspaceActivityApi.endpoints.filterWorkspaceActivities, {workspaceId, page: 0}, prefetch.workspaceActivities),
        // /tasks/assigned-to-me (AssignedToMePage -> PrefilteredPaginatedTaskList)
        ...seed<TaskFilterRequest, typeof prefetch.assignedTasks>(taskListingApi.endpoints.filterTasks, {
            workspaceId,
            assigneeIds: [accountId],
            teamIdList: undefined,
            page: 0
        }, prefetch.assignedTasks),
        // /inbox + InboxButton badge
        ...seed(notificationEventApi.endpoints.retrieveNotifications, {workspaceId, page: 0}, prefetch.notifications),
        ...seed(notificationEventApi.endpoints.retrieveUnreadNotificationCount, {workspaceId}, prefetch.unreadNotificationCount),
        // calendar + files side menus
        ...seed(calendarMemberApi.endpoints.retrieveCalendarMemberships, {workspaceId}, prefetch.calendarMemberships),
        ...seed(workspaceMediaApi.endpoints.retrieveWorkspaceMediaLimits, {workspaceId}, prefetch.workspaceMediaLimits),
        ...request.calendarTimespans.flatMap(({timespanStart, timespanEnd}, index) =>
            seed(calendarEventApi.endpoints.filterCalendarEvents, {
                workspaceId,
                taskboardIds: [],
                assigneeIds: [],
                collaboratorIds: [],
                workflowStatusIdList: [],
                timespanStart,
                timespanEnd
            }, prefetch.calendarEvents[index])
        ),
        // /files root + quick filters (FilesPage -> WorkspaceFilesPage)
        ...request.materialFilters.flatMap((materialFilter, index) =>
            seed(materialListingApi.endpoints.searchMaterial, {
                workspaceId,
                page: 0,
                parentMaterialId: null,
                ...materialFilter
            }, prefetch.materials[index])
        ),
        ...prefetch.teams.flatMap((team) => buildTeamSeeds(workspaceId, team)),
        // /notebook (useFirstAvailableNote): drafts, then each notebook in order until one has a note;
        // the per-notebook keys also back NoteHierarchyList, whose undefined parentNoteId drops out
        ...seed(notebookListingApi.endpoints.listWorkspaceNotebooks, {workspaceId, page: 0}, prefetch.notebooks),
        ...seed(noteFilterApi.endpoints.filterNotes, {workspaceId, page: 0}, prefetch.draftNotes),
        ...prefetch.notebookNotes.flatMap(({notebookId, notes}) =>
            seed(noteFilterApi.endpoints.filterNotes, {workspaceId, notebookId, page: 0}, notes)
        )
    ];
};
