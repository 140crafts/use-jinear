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
 */
export const useCreateNoteDraft = (workspace?: WorkspaceDto) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  return useCallback(() => {
    if (!workspace) return;
    const draftId = UUID();
    dispatch(addPendingDraft({ draftId, workspaceId: workspace.workspaceId }));
    navigate(`/${workspace.username}/notebook/${DRAFTS_NOTEBOOK_ID}/note/${draftId}`);
  }, [dispatch, navigate, workspace]);
};
