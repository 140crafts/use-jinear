import { useMemo } from "react";
import { useAllWorkspaceNotebooks } from "@/hooks/useAllWorkspaceNotebooks";
import { DRAFTS_NOTEBOOK_ID } from "@/components/tiptap/crdt/constants";
import type { NotebookDto } from "@/model/be/jinear-core";

/**
 * Resolves a notebook id to its dto out of the cached workspace listing, so a notebook id from the
 * url can be rendered (title, visibility) without waiting on, or even having, the note it belongs
 * to. Reads the persisted RTK cache, so it resolves offline for any notebook the user has seen.
 *
 * The drafts sentinel resolves to undefined: it isn't a notebook, it's the absence of one.
 */
export const useNotebookById = (workspaceId?: string, notebookId?: string): NotebookDto | undefined => {
  const { notebooks } = useAllWorkspaceNotebooks(workspaceId);

  return useMemo(
    () =>
      !notebookId || notebookId === DRAFTS_NOTEBOOK_ID
        ? undefined
        : notebooks.find(notebook => notebook.notebookId === notebookId),
    [notebooks, notebookId]
  );
};
