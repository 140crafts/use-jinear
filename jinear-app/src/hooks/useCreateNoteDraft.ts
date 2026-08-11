import { useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { UUID } from "@/util/UUID";
import { addPendingDraft } from "@/store/slice/noteDraftsSlice";
import { useAppDispatch } from "@/store/hooks";
import { DRAFTS_NOTEBOOK_ID } from "@/components/tiptap/crdt/constants";
import type { WorkspaceDto } from "@/model/be/jinear-core";

/**
 * Starts a new note as a local draft and opens it. The draft id is minted here (not by the server) so the
 * editor has a url and an offline-capable doc key from the first keystroke; PendingDraftSubmitter creates
 * it server-side once it can.
 *
 * With a notebookId the note is born inside that notebook; the draft carries it through to the create,
 * so the content never has to enter the workspace-level drafts pool on its way in. Without one the draft
 * is workspace-level and lands in the drafts section, which is the behaviour every draft used to have.
 */
export const useCreateNoteDraft = (workspace?: WorkspaceDto, notebookId?: string) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  return useCallback(() => {
    if (!workspace) return;
    const draftId = UUID();
    dispatch(addPendingDraft({ draftId, workspaceId: workspace.workspaceId, notebookId }));
    navigate(`/${workspace.username}/notebook/${notebookId ?? DRAFTS_NOTEBOOK_ID}/note/${draftId}`);
  }, [dispatch, navigate, workspace, notebookId]);
};
