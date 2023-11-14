import { root } from "@/store/api/api";
import React from "react";
import { SSEProvider } from "react-hooks-sse";

interface SseProviderWorkspaceActivitiesProps {
  workspaceId?: string;
  children: React.ReactNode;
}

const SseProviderWorkspaceActivities: React.FC<SseProviderWorkspaceActivitiesProps> = ({ workspaceId, children }) => {
  const getSource = () => {
    const url = `${root}v1/sse/workspace/activity/${workspaceId}`;
    return new EventSource(url, { withCredentials: true });
  };

  return workspaceId ? <SSEProvider source={getSource}>{children}</SSEProvider> : <>{children}</>;
};

export default SseProviderWorkspaceActivities;
