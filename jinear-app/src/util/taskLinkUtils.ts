import {HOST} from "@/util/constants";

const TASK_PATH_PATTERN = /^\/([^/]+)\/task\/([a-z0-9_.-]+-\d{1,9})\/?$/i;
const RELATIVE_LINK_BASE = "http://relative.invalid";

const toPathname = (href: string) => {
    try {
        return new URL(href, RELATIVE_LINK_BASE).pathname;
    } catch {
        return null;
    }
};

export const parseTaskLinkTag = (href: string | null | undefined, workspaceUsername: string | null | undefined) => {
    if (!href || !workspaceUsername) {
        return null;
    }
    const match = toPathname(href)?.match(TASK_PATH_PATTERN);
    if (!match || match[1].toLowerCase() !== workspaceUsername.toLowerCase()) {
        return null;
    }
    return match[2].toLowerCase();
};

export const buildTaskLink = (workspaceUsername: string, teamTag: string, teamTagNo: number) =>
    `${HOST}/${workspaceUsername}/task/${teamTag}-${teamTagNo}`;
