import { useEffect, useState } from "react";
import { skipToken } from "@reduxjs/toolkit/query";
import { useFilterNotesQuery } from "@/store/api/noteFilterApi";
import { useAllWorkspaceNotebooks } from "@/hooks/useAllWorkspaceNotebooks";
import { useAppSelector } from "@/store/hooks";
import { selectPendingDraftsOrdered } from "@/store/slice/noteDraftsSlice";
import { DRAFTS_NOTEBOOK_ID } from "@/components/tiptap/crdt/constants";
import type { WorkspaceDto } from "@/model/be/jinear-core";

export interface FirstAvailableNote {
  /** Notebook segment for the note url. `"drafts"` for drafts, see DRAFTS_NOTEBOOK_ID. */
  notebookId?: string;
  /** Note id, or the local draft id for a not-yet-acked draft. */
  noteId?: string;
  isDraft: boolean;
  /** Ready-to-navigate path, undefined until a note is found. */
  path?: string;
  /** Nothing resolved yet and something is still in flight — show a loader, not the empty state. */
  isLoading: boolean;
  /** Any underlying request in flight (incl. background refetches). Rarely needed; prefer isLoading. */
  isFetching: boolean;
  /** Resolved and there is a note to go to. `!isLoading && !hasNote` ⇒ empty state. */
  hasNote: boolean;
}

/**
 * Picks the note to land on when the notebook section is opened without one, in priority order:
 * a local pending draft, then a server-side draft, then the first note of the first notebook that
 * has any (empty notebooks are skipped, one request per notebook until a note is found).
 */
export const useFirstAvailableNote = (workspace?: WorkspaceDto): FirstAvailableNote => {
  const workspaceId = workspace?.workspaceId;

  // Local drafts win: they only exist on this device, so nothing else can lead the user back to them.
  // Reading just the head keeps the selector's freshly built array out of the reference comparison.
  const pendingDraft = useAppSelector(state =>
    workspaceId ? selectPendingDraftsOrdered(workspaceId)(state)[0] : undefined
  );

  // Omitting notebookId (and noteId) is what the backend reads as "list my drafts".
  const drafts = useFilterNotesQuery(workspaceId ? { workspaceId, page: 0 } : skipToken);

  const { notebooks, isLoading: notebooksLoading, isFetching: notebooksFetching } = useAllWorkspaceNotebooks(workspaceId);

  const [notebookIndex, setNotebookIndex] = useState<number>(0);
  useEffect(() => setNotebookIndex(0), [workspaceId]);

  const scannedNotebook = notebooks[notebookIndex];
  const notebookNotes = useFilterNotesQuery(
    workspaceId && scannedNotebook
      ? { workspaceId, notebookId: scannedNotebook.notebookId, page: 0 }
      : skipToken
  );

  const draftNote = drafts.currentData?.data?.content?.[0];
  const notebookNote = notebookNotes.currentData?.data?.content?.[0];

  // currentData is undefined while the next notebook's request is in flight, so this only fires once
  // per settled notebook and can't run ahead of itself.
  useEffect(() => {
    if (!notebookNotes.currentData || notebookNote) return;
    setNotebookIndex(index => (index + 1 < notebooks.length ? index + 1 : index));
  }, [notebookNotes.currentData, notebookNote, notebooks.length]);

  const draftsSettled = drafts.currentData != null || drafts.isError;
  const notebookNotesSettled = notebookNotes.currentData != null || notebookNotes.isError;
  const scanning =
    !!workspaceId &&
    (notebooksLoading ||
      (!!scannedNotebook && !notebookNotesSettled) ||
      // settled but empty, with notebooks left to walk — the effect above is about to advance
      (!!scannedNotebook && notebookNotesSettled && !notebookNote && notebookIndex < notebooks.length - 1));

  const resolved: Pick<FirstAvailableNote, "notebookId" | "noteId" | "isDraft"> = pendingDraft
    ? { notebookId: DRAFTS_NOTEBOOK_ID, noteId: pendingDraft.draftId, isDraft: true }
    : draftNote
      ? { notebookId: DRAFTS_NOTEBOOK_ID, noteId: draftNote.noteId, isDraft: true }
      : notebookNote
        ? { notebookId: notebookNote.notebookId ?? scannedNotebook?.notebookId, noteId: notebookNote.noteId, isDraft: false }
        : { isDraft: false };

  const hasNote = !!(resolved.notebookId && resolved.noteId);

  return {
    ...resolved,
    path: hasNote ? `/${workspace?.username}/notebook/${resolved.notebookId}/note/${resolved.noteId}` : undefined,
    isLoading: !hasNote && !!workspaceId && (!draftsSettled || scanning),
    isFetching: drafts.isFetching || notebooksFetching || notebookNotes.isFetching,
    hasNote
  };
};
