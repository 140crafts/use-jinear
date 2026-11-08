import type {PageDto, WorkspaceSearchDto, WorkspaceSearchType} from "@/model/be/jinear-core";
import type {StringKeys} from "@/locales/useTranslation";

export type GlobalSearchTab = "ALL" | WorkspaceSearchType;

export const SEARCH_TYPES: WorkspaceSearchType[] = ["TASK", "NOTE", "MATERIAL"];

export const SEARCH_TABS: GlobalSearchTab[] = ["ALL", ...SEARCH_TYPES];

export const TAB_LABELS: Record<GlobalSearchTab, StringKeys> = {
    ALL: "globalSearchModalTabAll",
    TASK: "globalSearchModalTabTasks",
    NOTE: "globalSearchModalTabNotes",
    MATERIAL: "globalSearchModalTabFiles"
};

export const sectionOf = (result: WorkspaceSearchDto | undefined, type: WorkspaceSearchType): PageDto<unknown> | null | undefined => {
    if (type == "TASK") {
        return result?.tasks;
    }
    if (type == "NOTE") {
        return result?.notes;
    }
    return result?.materials;
};

export const totalOf = (result: WorkspaceSearchDto | undefined, type: WorkspaceSearchType) => sectionOf(result, type)?.totalElements ?? 0;
