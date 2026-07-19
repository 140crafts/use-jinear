import { useEffect } from "react";
import { shallowEqual } from "react-redux";
import { skipToken } from "@reduxjs/toolkit/query";
import { listWorkspaceNotebooks, useListWorkspaceNotebooksQuery } from "@/store/api/notebookListingApi";
import { useAppDispatch, useAppSelector } from "@/store/hooks";
import type { NotebookDto } from "@/model/be/jinear-core";

const EMPTY: NotebookDto[] = [];

export const useAllWorkspaceNotebooks = (workspaceId?: string) => {
  const dispatch = useAppDispatch();

  // Page must always be explicit: {workspaceId} and {workspaceId, page: 0}
  // serialize to different cache keys, and the selectors below must match.
  const firstPage = useListWorkspaceNotebooksQuery(workspaceId ? { workspaceId, page: 0 } : skipToken);
  const totalPages = firstPage.currentData?.data?.totalPages ?? 0;

  // Keep pages 1..totalPages-1 subscribed. Active subscriptions are what make
  // the "v1/notebook/workspace/{workspaceId}" tag invalidation (fired by
  // notebookOperationApi mutations) refetch every page, not just page 0.
  useEffect(() => {
    if (!workspaceId || totalPages <= 1) return;
    const subscriptions = Array.from({ length: totalPages - 1 }, (_, i) =>
      dispatch(listWorkspaceNotebooks.initiate({ workspaceId, page: i + 1 }))
    );
    return () => subscriptions.forEach(subscription => subscription.unsubscribe());
  }, [dispatch, workspaceId, totalPages]);

  const notebooks = useAppSelector(state => {
    if (!workspaceId || totalPages === 0) return EMPTY;
    const all: NotebookDto[] = [];
    for (let page = 0; page < totalPages; page++) {
      const result = listWorkspaceNotebooks.select({ workspaceId, page })(state);
      result.data?.data?.content?.forEach(notebook => all.push(notebook));
    }
    return all;
  }, shallowEqual);

  return {
    notebooks,
    isLoading: firstPage.isLoading,
    isFetching: firstPage.isFetching,
    refetch: firstPage.refetch
  };
};
