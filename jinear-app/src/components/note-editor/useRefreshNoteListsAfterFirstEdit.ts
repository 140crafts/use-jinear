import {useEffect, useRef} from "react";
import {useAppDispatch} from "@/store";
import {api} from "@/api/api.ts";
import type {LiveTextStatus} from "@/components/tiptap/crdt/useLiveText.ts";

const PUSHED_STATUSES: LiveTextStatus[] = ["synced", "saved_locally"];

export const useRefreshNoteListsAfterFirstEdit = (status: LiveTextStatus | undefined) => {
    const dispatch = useAppDispatch();
    const previousStatusRef = useRef<LiveTextStatus | undefined>(status);
    const refreshedRef = useRef(false);

    useEffect(() => {
        const firstPushEnded = previousStatusRef.current === "syncing" && !!status && PUSHED_STATUSES.includes(status);
        previousStatusRef.current = status;
        if (firstPushEnded && !refreshedRef.current) {
            refreshedRef.current = true;
            dispatch(api.util.invalidateTags(["v1/note/filter"]));
        }
    }, [status, dispatch]);
};
