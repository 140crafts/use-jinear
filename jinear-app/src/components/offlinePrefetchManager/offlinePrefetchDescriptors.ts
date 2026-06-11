import {
    computeMonthViewPeriod,
    computeTwoDayViewPeriod,
    computeWeekViewPeriod,
    type ICalendarViewPeriod
} from "@/components/calendar/calendarUtils";
import type {ExtendedTaskFilterRequest} from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import type {
    MaterialSearchContentFilterType,
    MaterialSearchSortType,
    TaskFilterRequest,
    TeamDto
} from "@/model/be/jinear-core";
import type {AppDispatch} from "@/store";
import {calendarEventApi} from "@/store/api/calendarEventApi";
import {calendarMemberApi} from "@/store/api/calendarMemberApi";
import {materialListingApi} from "@/store/api/materialListingApi";
import {notificationEventApi} from "@/store/api/notificationEventApi";
import {taskListingApi} from "@/store/api/taskListingApi";
import {taskMediaApi} from "@/store/api/taskMediaApi";
import {teamMemberApi} from "@/store/api/teamMemberApi";
import {teamWorkflowStatusApi} from "@/store/api/teamWorkflowStatusApi";
import {workspaceActivityApi} from "@/store/api/workspaceActivityApi";
import {workspaceMediaApi} from "@/store/api/workspaceMediaApi";
import {addDays, addMonths, addWeeks, startOfDay, startOfMonth, startOfWeek} from "date-fns";

export interface PrefetchSubscription {
    unsubscribe: () => void;
}

export type PrefetchEntry = (dispatch: AppDispatch) => PrefetchSubscription;

const entry = <TArg>(endpoint: { initiate: (arg: TArg) => any }, arg: TArg): PrefetchEntry =>
    (dispatch) => dispatch(endpoint.initiate(arg));

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

const calendarEntries = (workspaceId: string): PrefetchEntry[] => {
    const today = startOfDay(new Date());
    const viewingDates = calendarViewingDates(today);
    const periods: ICalendarViewPeriod[] = [
        ...viewingDates.month.map(computeMonthViewPeriod),
        ...viewingDates.week.map(computeWeekViewPeriod),
        ...viewingDates.twoDay.map(computeTwoDayViewPeriod)
    ];
    return periods.map(({periodStart, periodEnd}) =>
        entry(calendarEventApi.endpoints.filterCalendarEvents, {
            workspaceId,
            taskboardIds: [],
            timespanStart: periodStart,
            timespanEnd: periodEnd
        })
    );
};

// Root folder + the FilesSectionSideMenu quick filters (recent/images/documents/shared).
const materialSearchFilterVariants: Array<{
    materialSearchSortType: MaterialSearchSortType | null;
    materialSearchContentFilterType: MaterialSearchContentFilterType | null;
}> = [
    {materialSearchSortType: null, materialSearchContentFilterType: null},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: null},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: "IMAGE"},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: "DOC"},
    {materialSearchSortType: "IDATE_DESC", materialSearchContentFilterType: "SHARED"}
];

export const buildWorkspaceEntries = ({workspaceId, accountId}: {
    workspaceId: string;
    accountId: string;
}): PrefetchEntry[] => [
    entry(teamMemberApi.endpoints.retrieveMemberships, {workspaceId}),
    // /tasks/last-activities (LastWorkspaceActivitiesList)
    entry(workspaceActivityApi.endpoints.filterWorkspaceActivities, {workspaceId, page: 0}),
    // /tasks/assigned-to-me (AssignedToMePage -> PrefilteredPaginatedTaskList)
    entry<TaskFilterRequest>(taskListingApi.endpoints.filterTasks, {
        workspaceId,
        assigneeIds: [accountId],
        teamIdList: undefined,
        page: 0
    }),
    // /inbox + InboxButton badge
    entry(notificationEventApi.endpoints.retrieveNotifications, {workspaceId, page: 0}),
    entry(notificationEventApi.endpoints.retrieveUnreadNotificationCount, {workspaceId}),
    // calendar + files side menus
    entry(calendarMemberApi.endpoints.retrieveCalendarMemberships, {workspaceId}),
    entry(workspaceMediaApi.endpoints.retrieveWorkspaceMediaLimits, {workspaceId}),
    ...calendarEntries(workspaceId),
    // /files root + quick filters (FilesPage -> WorkspaceFilesPage)
    ...materialSearchFilterVariants.map((variant) =>
        entry(materialListingApi.endpoints.searchMaterial, {
            workspaceId,
            page: 0,
            parentMaterialId: null,
            ...variant
        })
    )
];

export const buildTeamEntries = ({workspaceId, teams}: {
    workspaceId: string;
    teams: TeamDto[];
}): PrefetchEntry[] =>
    teams.flatMap((team) => [
        // /tasks/:teamUsername/tasks (MultiViewTaskList) — nulls mirror useQueryState defaults
        entry<ExtendedTaskFilterRequest>(taskListingApi.endpoints.filterTasks, {
            page: 0,
            workspaceId,
            teamIdList: [team.teamId],
            topicIds: null,
            ownerIds: null,
            assigneeIds: null,
            workflowStatusIdList: null,
            workflowStateGroups: null,
            timespanStart: null,
            timespanEnd: null,
            hasPreciseFromDate: null,
            hasPreciseToDate: null
        }),
        entry(teamWorkflowStatusApi.endpoints.retrieveAllFromTeam, {teamId: team.teamId}),
        // /tasks/:teamUsername/files (TeamFileList)
        entry(taskMediaApi.endpoints.retrieveTaskMediaListFromTeam, {teamId: team.teamId, page: 0})
    ]);
